package com.demo.webview.basefragment;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.demo.webview.IWebAidlCallback;
import com.demo.webview.IWebAidlInterface;
import com.demo.webview.command.webviewprocess.WebviewProcessCommandsManager;
import com.demo.webview.mainprocess.RemoteWebBinderPool;
import com.demo.webview.remotewebview.BaseWebView;
import com.demo.webview.utils.MainLooper;
import com.demo.webview.utils.WebConstants;
import com.google.gson.Gson;

import java.util.Map;

/**
 * WebView所有请求分发
 * <p>
 * 规则：
 * <p>
 * 1、先处理UI依赖
 * 2、再判断是否是跨进程通信，跨进程通信需要通过AIDL方式分发数据
 */
public class CommandDispatcher {

    private static CommandDispatcher instance;
    private Gson gson = new Gson();

    // 实现跨进程通信的接口
    protected IWebAidlInterface webAidlInterface;

    public static CommandDispatcher getInstance() {
        if (instance == null) {
            synchronized (CommandDispatcher.class) {
                if (instance == null) {
                    instance = new CommandDispatcher();
                }
            }
        }
        return instance;
    }

    public void initAidlConnect(final Context context) {
        if (webAidlInterface != null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("AIDL", "Begin to connect with main process");
                RemoteWebBinderPool binderPool = RemoteWebBinderPool.getInstance(context);
                IBinder iBinder = binderPool.queryBinder(RemoteWebBinderPool.BINDER_WEB_AIDL);
                webAidlInterface = IWebAidlInterface.Stub.asInterface(iBinder);
                Log.i("AIDL", "Connect success with main process");
            }
        }).start();
    }

    public void exec(Context context, int commandLevel, String cmd, String params, WebView webView,
                     DispatcherCallBack dispatcherCallBack) {
        Log.i("CommandDispatcher", "command: " + cmd + " params: " + params);
        try {
            if (WebviewProcessCommandsManager.getInstance().checkHitLocalCommand(commandLevel, cmd)) {
                execLocalCommand(context, commandLevel, cmd, params, webView, dispatcherCallBack);
            } else {
                execRemoteCommand(commandLevel, cmd, params, webView, dispatcherCallBack);
            }
        } catch (Exception e) {
            Log.e("CommandDispatcher", "Command exec error!!!!", e);
        }
    }

    private void execLocalCommand(final Context context, final int commandLevel, final String cmd, final String params,
                                  final WebView webView, final DispatcherCallBack dispatcherCallBack) throws Exception {
        Map mapParams = gson.fromJson(params, Map.class);
        WebviewProcessCommandsManager.getInstance().findAndExecLocalCommnad(
                context, commandLevel, cmd, mapParams,
                (int status, String action, Object result) -> {
                    try {
                        if (status == WebConstants.CONTINUE) {
                            execRemoteCommand(commandLevel, action, gson.toJson(result), webView, dispatcherCallBack);
                        } else {
                            handleCallback(status, action, gson.toJson(result), webView, dispatcherCallBack);
                        }
                    } catch (Exception e) {
                        Log.e("CommandDispatcher", "Command exec error!!!!", e);
                    }
                }
        );
    }

    private void execRemoteCommand(int commandLevel, String cmd, String params, final WebView webView, final DispatcherCallBack dispatcherCallBack) throws Exception {
        if (webAidlInterface != null) {
            webAidlInterface.handleWebAction(commandLevel, cmd, params, new IWebAidlCallback.Stub() {
                @Override
                public void onResult(int responseCode, String actionName, String response) throws RemoteException {
                    handleCallback(responseCode, actionName, response, webView, dispatcherCallBack);
                }
            });
        }
    }

    private void handleCallback(final int responseCode, final String actionName, final String response,
                                final WebView webView, final DispatcherCallBack dispatcherCallBack) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response));
        MainLooper.runOnUiThread(() -> {
            if (dispatcherCallBack != null) {
                dispatcherCallBack.preHandleBeforeCallback(responseCode, actionName, response);
            }

            Map params = new Gson().fromJson(response, Map.class);
            if (params.get(WebConstants.NATIVE2WEB_CALLBACK) != null && !TextUtils.isEmpty(params.get(WebConstants.NATIVE2WEB_CALLBACK).toString())) {
                if (webView instanceof BaseWebView) {
                    ((BaseWebView) webView).handleCallback(response);
                }
            }
        });
    }

    /**
     * Dispatcher 过程中的回调介入
     */
    public interface DispatcherCallBack {
        boolean preHandleBeforeCallback(int responseCode, String actionName, String response);
    }
}
