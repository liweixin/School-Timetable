package com.qianrushi.schooltimetable.function;

import android.util.Log;

/**
 * Created by lwx on 2016/3/20.
 */
public class AnalyseContent {
    String courseName, location;
    int startWeek, endWeek;
    String content;
    int divider;
    public AnalyseContent(String content){
        this.content = content;
    }
    public String getCourseName(){
        if(courseName!=null) return courseName;
        if(divider<=0){
            getEndWeek();
        }
        courseName = content.substring(0, divider);
        return courseName;
    }
    public int getStartWeek(){
        if(startWeek<=0) getEndWeek();
        return startWeek;
    }
    public int getEndWeek(){
        if(endWeek>0) return endWeek;
        int i = content.indexOf("周）");
        if(i<0) throw new IllegalArgumentException("Can't resolve end week.");
        int j = content.lastIndexOf("（");
        divider = j;
        int k = content.indexOf("-", j);
        Log.d("ijk", i+" "+j +" "+k);
        startWeek = Integer.parseInt(content.substring(j+1, k));
        endWeek = Integer.parseInt(content.substring(k+1, i));
        return endWeek;
    }
    public String getLocation(){
        Log.d("String", content);
        if(location!=null) return location;
        int i = content.lastIndexOf("[");
        int j = content.lastIndexOf("]");
        if(i==-1||j==-1||i>j) throw new IllegalArgumentException("Can't resolve location.");
        location = content.substring(i+1,j );
        return location;
    }
}
