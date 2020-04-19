package com.demo.common.webview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.demo.common.R;
import com.demo.webview.AccountWebFragment;
import com.demo.webview.CommonWebFragment;
import com.demo.webview.basefragment.BaseWebviewFragment;
import com.demo.webview.command.base.Command;
import com.demo.webview.command.base.ResultBack;
import com.demo.webview.command.webviewprocess.WebviewProcessCommandsManager;
import com.demo.webview.utils.WebConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WebActivity extends AppCompatActivity {
    private String title;
    private String url;

    BaseWebviewFragment webviewFragment;

    public static void startCommonWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebConstants.INTENT_TAG_TITLE, title);
        intent.putExtra(WebConstants.INTENT_TAG_URL, url);
        intent.putExtra("level", WebConstants.LEVEL_BASE);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startAccountWeb(Context context, String title, String url, @NonNull HashMap<String, String> headers) {
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebConstants.INTENT_TAG_TITLE, title);
        bundle.putString(WebConstants.INTENT_TAG_URL, url);
        bundle.putSerializable(WebConstants.INTENT_TAG_HEADERS, headers);
        bundle.putInt("level", WebConstants.LEVEL_ACCOUNT);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        title = getIntent().getStringExtra(WebConstants.INTENT_TAG_TITLE);
        url = getIntent().getStringExtra(WebConstants.INTENT_TAG_URL);
        setTitle(title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        WebviewProcessCommandsManager.getInstance().registerCommand(WebConstants.LEVEL_LOCAL, titleUpdateCommand);
        int level = getIntent().getIntExtra("level", WebConstants.LEVEL_BASE);
        webviewFragment = null;
        if (level == WebConstants.LEVEL_BASE) {
            webviewFragment = CommonWebFragment.newInstance(url);
        } else {
            if (getIntent() != null && getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                Serializable s = bundle.getSerializable(WebConstants.INTENT_TAG_HEADERS);
                if (s != null) {
                    webviewFragment = AccountWebFragment.newInstance(url, (HashMap<String, String>) s, true);
                }
            }

        }
        transaction.replace(R.id.web_view_fragment, webviewFragment).commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webviewFragment != null /*&& webviewFragment instanceof BaseWebviewFragment*/) {
            boolean flag = webviewFragment.onKeyDown(keyCode, event);
            if (flag) {
                return flag;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 页面路由
     */
    private final Command titleUpdateCommand = new Command() {
        @Override
        public String name() {
            return Command.COMMAND_UPDATE_TITLE;
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            if (params.containsKey(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE)) {
                setTitle((String) params.get(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE));
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            default:
                // case blocks for other MenuItems (if any)
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
