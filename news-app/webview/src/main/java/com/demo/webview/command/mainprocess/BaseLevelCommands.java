package com.demo.webview.command.mainprocess;

import com.demo.webview.command.base.Commands;
import com.demo.webview.utils.WebConstants;

public class BaseLevelCommands extends Commands {

    public BaseLevelCommands() {
    }

    @Override
    protected int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
