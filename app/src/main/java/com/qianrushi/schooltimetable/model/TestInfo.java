package com.qianrushi.schooltimetable.model;

/**
 * Created by lwx on 2016/4/4.
 */
public class TestInfo {
    String courseName, courseNum, time, location, teacher;
    public TestInfo(String courseName, String courseNum, String time, String location, String teacher){
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.time = time;
        this.location = location;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "TestInfo{" +
                "courseName='" + courseName + '\'' +
                ", courseNum='" + courseNum + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestInfo)) return false;

        TestInfo testInfo = (TestInfo) o;

        if (!getCourseName().equals(testInfo.getCourseName())) return false;
        if (!getCourseNum().equals(testInfo.getCourseNum())) return false;
        if (!getTime().equals(testInfo.getTime())) return false;
        if (!getLocation().equals(testInfo.getLocation())) return false;
        return getTeacher().equals(testInfo.getTeacher());

    }

    @Override
    public int hashCode() {
        int result = getCourseName().hashCode();
        result = 31 * result + getCourseNum().hashCode();
        result = 31 * result + getTime().hashCode();
        result = 31 * result + getLocation().hashCode();
        result = 31 * result + getTeacher().hashCode();
        return result;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
