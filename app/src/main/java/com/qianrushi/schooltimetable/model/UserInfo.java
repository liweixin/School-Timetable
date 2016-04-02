package com.qianrushi.schooltimetable.model;

import java.util.List;

/**
 * Created by lwx on 2016/3/27.
 */
public class UserInfo implements IUserInfo{
    private String school;
    private String college;
    private String major;
    private String admissionTime;
    private String education;
    private List<CourseInfo> courseInfoList;
    private static UserInfo userInfo;

    private UserInfo(){}

    public static UserInfo getInstance(){
        if(userInfo==null){
            userInfo = new UserInfo();
        }
         return userInfo;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(String admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<CourseInfo> getCourseInfoList() {
        return courseInfoList;
    }

    public void setCourseInfoList(List<CourseInfo> list) {
        this.courseInfoList = list;
    }
}
