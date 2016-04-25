package com.qianrushi.schooltimetable.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by lwx on 2016/3/31.
 */
public class Util {
    private Util(){};
    static Util utils;
    Context context = null;
    public final static int EDIT_COURSE = 1;
    public final static int ADD_COURSE = 2;
    public static final String APP_ID = "wxb11ce21158ec3e80";

    public static Util getInstance(){
        if (utils==null){
            utils = new Util();
        }
        return utils;
    }
    public void init(Context context) {
        this.context = context;
    }
    public void toast(String info) {
        if(context==null){
            throw new NullPointerException("Utils class should be init in mainactivity.");
        } else {
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        }
    }
    public void toast(String info, int time) {
        if(context==null){
            throw new NullPointerException("Utils class should be init in mainactivity.");
        } else {
            Toast.makeText(context, info, time).show();
        }
    }
    public Context getContext() {
        return context;
    }
    public static int getCurrentWeek(){
        return 9;
    }
    public static int getCurrentWeekDay(){
        return 1;
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
