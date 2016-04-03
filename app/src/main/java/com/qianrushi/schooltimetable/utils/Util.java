package com.qianrushi.schooltimetable.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by lwx on 2016/3/31.
 */
public class Util {
    private Util(){};
    static Util utils;
    Context context = null;

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
        return 6;
    }
    public static int getCurrentWeekDay(){
        return 7;
    }
}
