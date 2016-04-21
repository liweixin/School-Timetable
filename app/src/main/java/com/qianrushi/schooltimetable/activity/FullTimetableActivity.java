package com.qianrushi.schooltimetable.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;
import com.qianrushi.schooltimetable.view.SlideView;

import java.util.concurrent.TimeUnit;

/**
 * Created by lwx on 2016/4/20.
 */
public class FullTimetableActivity extends BaseActivity{
    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    boolean isFirst = true;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_full_timetabe);
        LinearLayout layout = (LinearLayout)findViewById(R.id.timetable);
        layout.setBackgroundColor(Color.parseColor("#00000000"));
        //setContentView(R.layout.fragment_timetable);
        SlideView slideView = (SlideView) findViewById(R.id.slider);
        slideView.setSlideListener(new SlideView.SlideListener() {
            @Override
            public void onDone() {
                Intent intent = new Intent(FullTimetableActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        getSize();
    }
    @Override
    public void onStart(){
        super.onStart();
        if(MyCourseinfo.getInstace()==null){
            //没有课程，进入另一个欢迎界面
        }
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addCourseView();
                    }
                });
            }
        }).start();*/
    }
    private void getSize(){
        tmpLayout = (RelativeLayout) findViewById(R.id.Monday);
        ViewTreeObserver observer = tmpLayout.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (isFirst) {
                    gridHeight = tmpLayout.getMeasuredHeight() / 13;
                    gridWidth = tmpLayout.getMeasuredWidth();
                    isFirst = false;
                    addCourseView(); //must be called after gridHeight&gridWidth has been measured.
                }
                return true;
            }
        });
    }
    private void addCourseView(){
        if(MyCourseinfo.getInstace()==null) return;
        MyCourseinfo.setShown(true);
        for(CourseInfo course:MyCourseinfo.getInstace()){
            int currentWeek = Util.getCurrentWeek();
            if(course.getStartWeek()<=currentWeek&&currentWeek<=course.getEndWeek()){  //如果当前周要上课
                Log.e("add", "course");
                addView(course.getDay(), course.getStartNum(), course.getEndNum(), course.getName() + course.getLocation(), course);
            }
        }
    }
    private TextView createTv(int start,int end,String text){
        TextView tv = new TextView(this);
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
    public void addView(int i,int start,int end,String text,CourseInfo courseInfo){
        TextView tv;
        switch (i){
            case 1:
                layout = (RelativeLayout) findViewById(R.id.Monday);
                break;
            case 2:
                layout = (RelativeLayout) findViewById(R.id.Tuesday);
                break;
            case 3:
                layout = (RelativeLayout) findViewById(R.id.Wednesday);
                break;
            case 4:
                layout = (RelativeLayout) findViewById(R.id.Thursday);
                break;
            case 5:
                layout = (RelativeLayout) findViewById(R.id.Friday);
                break;
            case 6:
                layout = (RelativeLayout) findViewById(R.id.Saturday);
                break;
            case 7:
                layout = (RelativeLayout) findViewById(R.id.Sunday);
                break;
        }
        tv = createTv(start,end,text);
        tv.setTag(courseInfo);
        tv.setBackgroundResource(R.drawable.corner_view);
        GradientDrawable myGrad = (GradientDrawable)tv.getBackground();
        myGrad.setColor(Color.argb(100, start * 5, (start + end) * 20, 0));
        layout.addView(tv);
        Log.e(gridWidth+"", gridHeight+"");
    }
}
