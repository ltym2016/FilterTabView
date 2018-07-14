package com.samluys.filtertab;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class Utils {

    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";

    private static String sMiuiVersionName;
    /**
     * 屏幕密度
     */
    public static float sDensity = 0f;

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度 这个方法获取的高度是不带底部虚拟按键的，如果有虚拟按键的话
     *
     * @return
     */
    public static int getScreenHeightNoBar(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取屏幕的真实高度 如果有虚拟按键会算上虚拟按键的高度
     * @param context
     * @return
     */
    public static int getScreenHeightWidthBar(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;

        return realHeight;
    }

    /**
     * 获取 DisplayMetrics
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 单位转换: dp to px
     *
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) (getDensity(context) * dp + 0.5);
    }

    public static float getDensity(Context context) {
        if (sDensity == 0f) {
            sDensity = getDisplayMetrics(context).density;
        }
        return sDensity;
    }

    /**
     * 获取状态栏的高度。
     */
    public static int getStatusBarHeight(Context context) {
        int mStatusHeight = StatusBarHelper.getStatusbarHeight(context);
        if (mStatusHeight == 0) {
            mStatusHeight = DisplayHelper.getStatusBarHeight(context);
        }

        return mStatusHeight;
    }

    /**
     * 获取手机屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        int screenHeight = 0;
        try {
            if(deviceHasNavigationBar()) {
                if (isMiuiSystem()) {
                    // 此时是不是切换为小米全面屏
                    if (isXIAOMIFullScreen(context)) {
                        screenHeight = getScreenHeightWidthBar(context);
                    } else {
                        screenHeight = getScreenHeightNoBar(context);
                    }
                } else {
                    screenHeight = getScreenHeightNoBar(context);
                }
            } else {
                // 如果不带虚拟按键 此时getScreenHeightWidthBar和getScreenHeightNoBar获取到的高度是一样的
                screenHeight = getScreenHeightWidthBar(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return screenHeight;
    }

    /**
     * 判断设备是否存在NavigationBar
     *
     * @return true 存在, false 不存在
     */
    public static boolean deviceHasNavigationBar() {
        boolean haveNav = false;
        try {
            //1.通过WindowManagerGlobal获取windowManagerService
            // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
            Class<?> windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal");
            Method getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService");
            getWmServiceMethod.setAccessible(true);
            //getWindowManagerService是静态方法，所以invoke null
            Object iWindowManager = getWmServiceMethod.invoke(null);

            //2.获取windowMangerService的hasNavigationBar方法返回值
            // 反射方法：haveNav = windowManagerService.hasNavigationBar();
            Class<?> iWindowManagerClass = iWindowManager.getClass();
            Method hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar");
            hasNavBarMethod.setAccessible(true);
            haveNav = (Boolean) hasNavBarMethod.invoke(iWindowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return haveNav;
    }

    /**
     * 判断小米手机是否为全面屏模式
     * @param context
     * @return
     */
    public static boolean isXIAOMIFullScreen (Context context) {
        if (Build.VERSION.SDK_INT > 16) {
            boolean isFull = Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;
            return isFull;
        }
        return false;
    }

    @Nullable
    private static String getLowerCaseName(Properties p, Method get, String key) {
        String name = p.getProperty(key);
        if (name == null) {
            try {
                name = (String) get.invoke(null, key);
            } catch (Exception ignored) {}
        }
        if (name != null) name = name.toLowerCase();
        return name;
    }

    /**
     * 判断是否为小米手机
     * @return
     */
    public static boolean isMiuiSystem() {
        Properties properties = new Properties();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                properties.load(fileInputStream);
            } catch (Exception e) {
                Log.e("FilterTabView", "read file error");
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Class<?> clzSystemProperties = null;
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = clzSystemProperties.getDeclaredMethod("get", String.class);
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME);

            if (!TextUtils.isEmpty(sMiuiVersionName)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("FilterTabView", "read SystemProperties error");
        }

        return false;
    }

}
