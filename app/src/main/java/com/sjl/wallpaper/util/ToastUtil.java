package com.sjl.wallpaper.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by sjl on 2015/8/11.
 */
public class ToastUtil {
    private static Context mContext;
    private static final int SHOW = 0;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    showToast(mContext, msg.getData().getString("TEXT"));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示提示文本
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (!(text == null || text.equals(""))) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    public static void showToastLong(Context context, String text) {
        if (!(text == null || text.equals(""))) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    /**
     * 显示提示文本,可自定义方向
     *
     * @param context
     * @param text
     * @param gravity
     */
    public static void showToast(Context context, String text, int gravity) {
        if (!(text == null || text.equals(""))) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getText(resId) + "");
    }

    public static void showToastInThread(Context context, String text) {
        mContext = context;
        Message msg = handler.obtainMessage(SHOW);
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", text);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public static void showToastInThread(Context context, int resId) {
        showToastInThread(context, context.getResources().getText(resId) + "");
    }
}
