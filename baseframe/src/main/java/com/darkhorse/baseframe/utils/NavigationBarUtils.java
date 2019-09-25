package com.darkhorse.baseframe.utils;

import android.os.Build;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.lang.reflect.Field;

/**
 * @author chenzhenjie
 * @time 2019/8/7 15:09
 **/
public class NavigationBarUtils {

    /**
     * 关闭底部导航条
     */
    public static void closeBar() {
        try {
            // 需要root 权限
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79";
            if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
                ProcID = "42"; // ICS AND NEWER
            }
            // 需要root 权限
            Process proc = Runtime.getRuntime().exec(
                    new String[]{
                            "su",
                            "-c",
                            "service call activity " + ProcID
                                    + " s16 com.android.systemui"}); // WAS 79
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示底部导航条
     */
    public static void openBar() {
        try {
            Process proc = Runtime.getRuntime().exec(
                    new String[]{"am", "startservice", "-n", "com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideBottomUIMenu(View v) {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        v.setSystemUiVisibility(uiOptions);
    }

    //调起输入法 用于隐藏输入法后隐藏导航栏
    public static void hideWhenSystemUiVisible(View v) {
        v.setOnSystemUiVisibilityChangeListener(visibility -> {
            if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                hideBottomUIMenu(v);
            }
        });
    }

    /**
     * spinner里也有popwindow 会调起导航栏
     *
     * @param spinner 尝试隐藏spinner弹出时的导航栏
     */
    public static void hideSpinnerSystemUi(Spinner spinner) {
        Field mPopup = null;
        try {
            mPopup = spinner.getClass().getDeclaredField("mPopup");
            mPopup.setAccessible(true);
            ListPopupWindow listPopupWindow = (ListPopupWindow) mPopup.get(spinner);
            Field mPopup1 = listPopupWindow.getClass().getSuperclass().getDeclaredField("mPopup");
            mPopup1.setAccessible(true);
            PopupWindow popupWindow = (PopupWindow) mPopup1.get(listPopupWindow);
            popupWindow.setFocusable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
