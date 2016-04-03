package com.qianrushi.schooltimetable.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwx on 2016/4/4.
 */
public class MyTestInfoList {
    List<TestInfo> list;
    String xn;
    int xq;
    private static MyTestInfoList myTestInfoList;
    private MyTestInfoList(){
        list = new ArrayList<>();
    }
    public static MyTestInfoList getInstance(){
        if(myTestInfoList==null){
            myTestInfoList = new MyTestInfoList();
        }
        return myTestInfoList;
    }

    public List<TestInfo> getList() {
        return list;
    }

    public void setList(List<TestInfo> list) {
        this.list = list;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public int getXq() {
        return xq;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }

    public static MyTestInfoList getMyTestInfoList() {
        return myTestInfoList;
    }

    public static void setMyTestInfoList(MyTestInfoList myTestInfoList) {
        MyTestInfoList.myTestInfoList = myTestInfoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyTestInfoList)) return false;

        MyTestInfoList that = (MyTestInfoList) o;

        if (getXq() != that.getXq()) return false;
        if (!getList().equals(that.getList())) return false;
        return getXn().equals(that.getXn());

    }

    @Override
    public int hashCode() {
        int result = getList().hashCode();
        result = 31 * result + getXn().hashCode();
        result = 31 * result + getXq();
        return result;
    }

    @Override
    public String toString() {
        return "MyTestInfoList{" +
                "list=" + list +
                ", xn='" + xn + '\'' +
                ", xq=" + xq +
                '}';
    }
}
