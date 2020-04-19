package com.demo.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.demo.base.BaseApp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 尉迟涛
 * create time : 2020/3/18 22:00
 * description :
 */
public class SpUtils {

    private static final Map<String, SpUtils> SP_UTILS_MAP = new HashMap<>();

    /**
     * Return the single {@link SpUtils} instance
     *
     * @return the single {@link SpUtils} instance
     */
    public static SpUtils getInstance() {
        return getInstance("", Context.MODE_PRIVATE);
    }

    /**
     * Return the single {@link SpUtils} instance
     *
     * @param spName The name of sp.
     * @return the single {@link SpUtils} instance
     */
    public static SpUtils getInstance(String spName) {
        return getInstance(spName, Context.MODE_PRIVATE);
    }

    /**
     * Return the single {@link SpUtils} instance
     *
     * @param spName The name of sp.
     * @param mode   Operating mode.
     * @return the single {@link SpUtils} instance
     */
    public static SpUtils getInstance(String spName, final int mode) {
        if (isSpace(spName)) spName = "SpUtils";
        SpUtils spUtils = SP_UTILS_MAP.get(spName);
        if (spUtils == null) {
            synchronized (SpUtils.class) {
                spUtils = SP_UTILS_MAP.get(spName);
                if (spUtils == null) {
                    spUtils = new SpUtils(spName, mode);
                    SP_UTILS_MAP.put(spName, spUtils);
                }
            }
        }
        return spUtils;
    }

    /**
     * SharedPreferences 名字不要有空格喔
     */
    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 网络缓存专用
     */
    public static SpUtils getNetSp() {
        return getInstance("NET");
    }

    /**
     * 基类专用
     */
    public static SpUtils getBaseSp() {
        return getInstance("Base");
    }

    /**
     * 随意使用
     */
    public static SpUtils getSp() {
        return getInstance("Default");
    }

    //***************************************************************************


    private SharedPreferences sp;

    private SpUtils(final String spName) {
        sp = BaseApp.app.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    private SpUtils(final String spName, final int mode) {
        sp = BaseApp.app.getSharedPreferences(spName, mode);
    }


    /**
     * 如果对提交的结果不关心的话，建议用apply，如果要确保提交成功且有后续操作的话，简易用commit。
     * String
     */
    public void put(@NonNull final String key, final String value) {
        sp.edit().putString(key, value).apply();
    }

    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    public String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }


    /**
     * Boolean
     */
    public void put(@NonNull final String key, final boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }
}
