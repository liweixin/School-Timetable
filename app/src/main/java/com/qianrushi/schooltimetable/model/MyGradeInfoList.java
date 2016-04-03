package com.qianrushi.schooltimetable.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwx on 2016/4/3.
 */
public class MyGradeInfoList {
    List<GradeInfo> list;
    String xn;
    int xq;
    private static MyGradeInfoList myGradeInfoList;

    public MyGradeInfoList(){
        list = new ArrayList<GradeInfo>();
        //list.add(new GradeInfo("123", "操作系统", 3.0, 80.0, "最终成绩", "100分", true));
        //list.add(new GradeInfo("234", "计算机病毒原理", 2.0, 83.0, "最终成绩", "100分", true));
        xn = "2015-2016";
        xq = 1;
    }
    public static MyGradeInfoList getInstance(){
        if(myGradeInfoList==null){
            myGradeInfoList = new MyGradeInfoList();
        }
        return myGradeInfoList;
    }

    public int getXq() {
        return xq;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyGradeInfoList)) return false;

        MyGradeInfoList that = (MyGradeInfoList) o;

        if (xq != that.xq) return false;
        if (!getList().equals(that.getList())) return false;
        return xn.equals(that.xn);

    }

    @Override
    public int hashCode() {
        int result = getList().hashCode();
        result = 31 * result + xn.hashCode();
        result = 31 * result + xq;
        return result;
    }

    public List<GradeInfo> getList() {
        return list;
    }

    public void setList(List<GradeInfo> list) {
        this.list = list;
    }

}
