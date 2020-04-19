package com.demo.base.utils;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.demo.base.BaseApp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 尉迟涛
 * create time : 2020/3/17 22:19
 * description :
 */
public class ActivityLifecycleUtil implements Application.ActivityLifecycleCallbacks {

    private static final ActivityLifecycleUtil ACTIVITY_LIFECYCLE = new ActivityLifecycleUtil();

    private final LinkedList<Activity> mActivityList = new LinkedList<>();
    private final List<OnAppStatusChangedListener> mStatusListeners = new ArrayList<>();
    private final Map<Activity, List<OnActivityDestroyedListener>> mDestroyedListenerMap = new HashMap<>();

    private int mForegroundCount = 0;
    private int mConfigCount = 0;
    private boolean mIsBackground = false;

    static ActivityLifecycleUtil getLifecycle() {
        return ACTIVITY_LIFECYCLE;
    }

    static LinkedList<Activity> getActivityList() {
        return ACTIVITY_LIFECYCLE.mActivityList;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LanguageUtils.applyLanguage(activity);
        setAnimatorsEnabled();
        setTopActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (!mIsBackground) {
            setTopActivity(activity);
        }
        if (mConfigCount < 0) {
            ++mConfigCount;
        } else {
            ++mForegroundCount;
        }
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        setTopActivity(activity);
        if (mIsBackground) {
            mIsBackground = false;
            postStatus(activity, true);
        }
        processHideSoftInputOnActivityDestroy(activity, false);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity.isChangingConfigurations()) {
            --mConfigCount;
        } else {
            --mForegroundCount;
            if (mForegroundCount <= 0) {
                mIsBackground = true;
                postStatus(activity, false);
            }
        }
        processHideSoftInputOnActivityDestroy(activity, true);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {/**/}

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityList.remove(activity);
        consumeOnActivityDestroyedListener(activity);
        KeyboardUtils.fixSoftInputLeaks(activity.getWindow());
    }

    static Context getTopActivityOrApp() {
        if (Utils.isAppForeground()) {
            Activity topActivity = ACTIVITY_LIFECYCLE.getTopActivity();
            return topActivity == null ? BaseApp.app : topActivity;
        } else {
            return BaseApp.app;
        }
    }

    Activity getTopActivity() {
        if (!mActivityList.isEmpty()) {
            for (int i = mActivityList.size() - 1; i >= 0; i--) {
                Activity activity = mActivityList.get(i);
                if (activity == null
                        || activity.isFinishing()
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
                    continue;
                }
                return activity;
            }
        }
        Activity topActivityByReflect = getTopActivityByReflect();
        if (topActivityByReflect != null) {
            setTopActivity(topActivityByReflect);
        }
        return topActivityByReflect;
    }

    void addOnAppStatusChangedListener(final OnAppStatusChangedListener listener) {
        mStatusListeners.add(listener);
    }

    void removeOnAppStatusChangedListener(final OnAppStatusChangedListener listener) {
        mStatusListeners.remove(listener);
    }

    void removeOnActivityDestroyedListener(final Activity activity) {
        if (activity == null) return;
        mDestroyedListenerMap.remove(activity);
    }

    void addOnActivityDestroyedListener(final Activity activity,
                                        final OnActivityDestroyedListener listener) {
        if (activity == null || listener == null) return;
        List<OnActivityDestroyedListener> listeners = mDestroyedListenerMap.get(activity);
        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<>();
            mDestroyedListenerMap.put(activity, listeners);
        } else {
            if (listeners.contains(listener)) return;
        }
        listeners.add(listener);
    }

    /**
     * To solve close keyboard when activity onDestroy.
     * The preActivity set windowSoftInputMode will prevent
     * the keyboard from closing when curActivity onDestroy.
     */
    private void processHideSoftInputOnActivityDestroy(final Activity activity, boolean isSave) {
        if (isSave) {
            final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            final int softInputMode = attrs.softInputMode;
            activity.getWindow().getDecorView().setTag(-123, softInputMode);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            final Object tag = activity.getWindow().getDecorView().getTag(-123);
            if (!(tag instanceof Integer)) return;
            Utils.runOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    Window window = activity.getWindow();
                    if (window != null) {
                        window.setSoftInputMode(((Integer) tag));
                    }
                }
            }, 100);
        }
    }

    private void postStatus(final Activity activity, final boolean isForeground) {
        if (mStatusListeners.isEmpty()) return;
        for (OnAppStatusChangedListener statusListener : mStatusListeners) {
            if (isForeground) {
                statusListener.onForeground(activity);
            } else {
                statusListener.onBackground(activity);
            }
        }
    }

    private void setTopActivity(final Activity activity) {
//            if (TransActivity.class == activity.getClass()) return;
        if (mActivityList.contains(activity)) {
            if (!mActivityList.getLast().equals(activity)) {
                mActivityList.remove(activity);
                mActivityList.addLast(activity);
            }
        } else {
            mActivityList.addLast(activity);
        }
    }

    private void consumeOnActivityDestroyedListener(Activity activity) {
        Iterator<Map.Entry<Activity, List<OnActivityDestroyedListener>>> iterator
                = mDestroyedListenerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Activity, List<OnActivityDestroyedListener>> entry = iterator.next();
            if (entry.getKey() == activity) {
                List<OnActivityDestroyedListener> value = entry.getValue();
                for (OnActivityDestroyedListener listener : value) {
                    listener.onActivityDestroyed(activity);
                }
                iterator.remove();
            }
        }
    }

    private Activity getTopActivityByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field mActivityListField = activityThreadClass.getDeclaredField("mActivityList");
            mActivityListField.setAccessible(true);
            Map activities = (Map) mActivityListField.get(currentActivityThreadMethod);
            if (activities == null) return null;
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            Log.e("Utils", e.getMessage());
        }
        return null;
    }

    /**
     * Set animators enabled.
     */
    private static void setAnimatorsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ValueAnimator.areAnimatorsEnabled()) {
            return;
        }
        try {
            //noinspection JavaReflectionMemberAccess
            Field sDurationScaleField = ValueAnimator.class.getDeclaredField("sDurationScale");
            sDurationScaleField.setAccessible(true);
            float sDurationScale = (Float) sDurationScaleField.get(null);
            if (sDurationScale == 0f) {
                sDurationScaleField.set(null, 1f);
                Log.i("Utils", "setAnimatorsEnabled: Animators are enabled now!");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //****************************************************************************************
    // 接口
    //****************************************************************************************

    public interface OnAppStatusChangedListener {
        void onForeground(Activity activity);

        void onBackground(Activity activity);
    }

    public interface OnActivityDestroyedListener {
        void onActivityDestroyed(Activity activity);
    }
}
