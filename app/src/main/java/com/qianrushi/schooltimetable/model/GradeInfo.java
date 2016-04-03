package com.qianrushi.schooltimetable.model;

import java.io.Serializable;

/**
 * Created by lwx on 2016/4/3.
 */
public class GradeInfo implements Serializable {
    String num, courseName, gradeName, fullmarks;
    boolean shown;
    double grade, credit;

    public GradeInfo(String num, String courseName, double credit, double grade, String gradeName, String fullmarks, boolean shown){
        this.num = num;
        this.courseName = courseName;
        this.credit = credit;
        this.grade = grade;
        this.gradeName = gradeName;
        this.fullmarks = fullmarks;
        this.shown = shown;
    }

    @Override
    public String toString() {
        return "GradeInfo{" +
                "num='" + num + '\'' +
                ", courseName='" + courseName + '\'' +
                ", gradeName='" + gradeName + '\'' +
                ", fullmarks='" + fullmarks + '\'' +
                ", shown=" + shown +
                ", grade=" + grade +
                ", credit=" + credit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeInfo)) return false;

        GradeInfo gradeInfo = (GradeInfo) o;

        if (isShown() != gradeInfo.isShown()) return false;
        if (Double.compare(gradeInfo.getGrade(), getGrade()) != 0) return false;
        if (Double.compare(gradeInfo.getCredit(), getCredit()) != 0) return false;
        if (!getNum().equals(gradeInfo.getNum())) return false;
        if (!getCourseName().equals(gradeInfo.getCourseName())) return false;
        if (!getGradeName().equals(gradeInfo.getGradeName())) return false;
        return getFullmarks().equals(gradeInfo.getFullmarks());

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getNum().hashCode();
        result = 31 * result + getCourseName().hashCode();
        result = 31 * result + getGradeName().hashCode();
        result = 31 * result + getFullmarks().hashCode();
        result = 31 * result + (isShown() ? 1 : 0);
        temp = Double.doubleToLongBits(getGrade());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCredit());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getFullmarks() {
        return fullmarks;
    }

    public void setFullmarks(String fullmarks) {
        this.fullmarks = fullmarks;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}
