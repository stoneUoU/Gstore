package com.Tools;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToastUtils {

    private static Toast toast;

    private static View view;

    private ToastUtils() {}

    private static void getToast(Context context) {
        if (toast == null) {
            toast = new Toast(context);
        }
        if (view == null) {
            view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        }
        toast.setView(view);
    }

    public static void showShortToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
    }

    /**
     *
     * @param context 上下文
     * @param msg 字符串
     * @param duration 时间 Toast.LENGTH_SHORT; Toast.LENGTH_LONG
     * gravity 位置 Gravity.CENTER; Gravity.BOTTOM; Gravity.TOP
     */
    private static void showToast(Context context, CharSequence msg, int duration) {
        try {
            getToast(context);
            toast.setText(msg);
            toast.setDuration(duration);
            toast.setGravity(Gravity.BOTTOM, 0, 64);
            toast.show();
        } catch (Exception e) {
        }
    }

    /**
     * 基本的设置Toast
     * @param context
     * @param resId 资源ID
     * @param duration 时间 Toast.LENGTH_SHORT; Toast.LENGTH_LONG
     * gravity 位置 Gravity.CENTER; Gravity.BOTTOM; Gravity.TOP
     */
    private static void showToast(Context context, int resId, int duration) {
        try {
            if (resId == 0) {
                return;
            }
            getToast(context);
            toast.setText(resId);
            toast.setDuration(duration);
            toast.setGravity(Gravity.BOTTOM, 0, 64);
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}