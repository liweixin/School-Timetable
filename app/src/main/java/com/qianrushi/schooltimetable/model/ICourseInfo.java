package com.qianrushi.schooltimetable.model;

/**
 * Created by lwx on 2016/3/27.
 */
public interface ICourseInfo {
    String getName();
    void setName(String s);
    int getStartWeek();
    void setStartWeek(int i);
    int getEndWeek();
    void setEndWeek(int i);
    String getLocation();
    void setLocation(String s);
    int getStartNum();
    void setStartNum(int i);
    int getEndNum();
    void setEndNum(int i);
    int getDay();
    void setDay(int i);
}
