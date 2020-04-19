package com.demo.webview.mainprocess;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.demo.webview.IWebAidlCallback;
import com.demo.webview.IWebAidlInterface;
import com.demo.webview.command.mainprocess.MainProcessCommandsManager;
import com.google.gson.Gson;

import java.util.Map;

public class MainProAidlInterface extends IWebAidlInterface.Stub {

    private Context context;

    public MainProAidlInterface(Context context) {
        this.context = context;
    }

    @Override
    public void handleWebAction(int level, String actionName, String jsonParams, IWebAidlCallback callback) throws RemoteException {
        int pid = android.os.Process.myPid();
        Log.d("webli", String.format("MainProAidlInterface: 进程ID（%d）， WebView请求（%s）, 参数 （%s）", pid, actionName, jsonParams));
        try {
            handleRemoteAction(level, actionName, new Gson().fromJson(jsonParams, Map.class), callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRemoteAction(int level, final String actionName, Map paramMap, final IWebAidlCallback callback) throws Exception {
        MainProcessCommandsManager.getInstance().findAndExecRemoteCommand(
                context, level, actionName, paramMap,
                (int status, String action, Object result) -> {
                    try {
                        if (callback != null) {
                            callback.onResult(status, actionName, new Gson().toJson(result));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
