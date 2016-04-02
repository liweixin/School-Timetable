package com.qianrushi.schooltimetable.viewpager.fragment.timetable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;

import java.util.List;

/**
 * Created by lwx on 2016/3/24.
 */
public class TimeTableFragment extends Fragment {
    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;
    private View mView;
    public ButtonFragment fragment;

    List<CourseInfo> courseInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        mView = inflater.inflate(R.layout.fragment_timetable, container, false);
        getSize();
        addCourseView();
        return mView;
    }
    private void addCourseView(){
        if(MyCourseinfo.getInstace()==null) return;
        for(CourseInfo course:MyCourseinfo.getInstace()){
                int currentWeek = Util.getCurrentWeek();
                if(course.getStartWeek()<=currentWeek&&currentWeek<=course.getEndWeek()){  //如果当前周要上课
                    addView(course.getDay(), course.getStartNum(), course.getEndNum(), course.getName() + course.getLocation());
                }
        }
    }

    public void addFragment(){
        if(fragment==null){
            fragment = new ButtonFragment();
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.
                beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.commit();
    }

    public void removeFragment(){
        BlankFragment fragment = new BlankFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.
                beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.commit();
    }

    private void getSize(){
        tmpLayout = (RelativeLayout) mView.findViewById(R.id.Monday);
        ViewTreeObserver observer = tmpLayout.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (isFirst) {
                    gridHeight = tmpLayout.getMeasuredHeight() / 13;
                    gridWidth = tmpLayout.getMeasuredWidth();
                    isFirst = false;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //addCourse();
                        }
                    });
                }
                return true;
            }
        });
    }
    private void addCourse(){
            String text="算法设计基础@W3502";
            addView(1,1,2,text);
            addView(7,2,3,text);
            addView(5, 9, 10, text);
            addView(4, 2, 3, text);
            addView(3, 5, 5, text);
            addView(4, 10, 12, text);
    }
    /*@Override  activity中测量布局宽和高的部分
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirst) {
            isFirst = false;
            gridWidth = tmpLayout.getWidth();
            gridHeight = tmpLayout.getHeight()/12;
        }
    }*/
    @Override
    public void onResume(){
        super.onResume();
        addCourseView();
    }
    private TextView createTv(int start,int end,String text){
        TextView tv = new TextView(getContext());
        /*
         指定高度和宽度
         */
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth,gridHeight*(end-start+1));
        /*
        指定位置
         */
        tv.setY(gridHeight * (start - 1));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        return tv;
    }
    public void addView(int i,int start,int end,String text){
        TextView tv;
        switch (i){
            case 1:
                layout = (RelativeLayout) mView.findViewById(R.id.Monday);
                break;
            case 2:
                layout = (RelativeLayout) mView.findViewById(R.id.Tuesday);
                break;
            case 3:
                layout = (RelativeLayout) mView.findViewById(R.id.Wednesday);
                break;
            case 4:
                layout = (RelativeLayout) mView.findViewById(R.id.Thursday);
                break;
            case 5:
                layout = (RelativeLayout) mView.findViewById(R.id.Friday);
                break;
            case 6:
                layout = (RelativeLayout) mView.findViewById(R.id.Saturday);
                break;
            case 7:
                layout = (RelativeLayout) mView.findViewById(R.id.Sunday);
                break;
        }
        tv= createTv(start,end,text);
        tv.setBackgroundColor(Color.argb(100, start * 5, (start + end) * 20, 0));
        layout.addView(tv);
    }
}

