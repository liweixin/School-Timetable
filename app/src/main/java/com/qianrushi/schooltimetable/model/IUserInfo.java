package com.qianrushi.schooltimetable.model;

import java.util.List;

/**
 * Created by lwx on 2016/3/27.
 */
public interface IUserInfo {
    String getSchool();
    String getCollege();
    String getMajor();
    String getAdmissionTime();
    String getEducation();
    List<CourseInfo> getCourseInfoList();
    void setSchool(String s);
    void setCollege(String s);
    void setMajor(String s);
    void setAdmissionTime(String s);
    void setEducation(String s);
    void setCourseInfoList(List<CourseInfo> list);
}
