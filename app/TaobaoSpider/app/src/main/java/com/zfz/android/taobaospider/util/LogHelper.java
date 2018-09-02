package com.zfz.android.taobaospider.util;


//import android.util.Log;

import android.util.Log;

import java.io.ByteArrayOutputStream;

public class LogHelper {
    private static final String TAG = "TaobaoHelper";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
    public static void e(Exception e) {
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            e.printStackTrace(new java.io.PrintWriter(buf, true));
            String  expMessage = buf.toString();
            buf.close();
            Log.e(TAG, expMessage);
        }
        catch (Exception ee){
            ee.printStackTrace();
        }

    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }
}
