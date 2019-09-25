package com.darkhorse.baseframe.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author chenzhenjie
 * @time 2019/8/12 16:29
 **/
public class KeyboardUtils {
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static Boolean isHideInput(View view, MotionEvent ev) {
        if (view instanceof EditText) {
            int[] l = new int[2];
            view.getLocationInWindow(l);
            int left = l[0];
            int top = l[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();

            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }
}
