package com.qianrushi.schooltimetable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.event.RefreshTimeTableEvent;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.model.EncodeAndDecode;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lwx on 2016/4/18.
 */
public class EditCourseActivity extends BaseActivity{
    EditText day, startWeek, endWeek, startNum, endNum, courseName, location;
    Button courseSubmit;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_course);
        initView();
    }
    private void initView(){
        day = (EditText) findViewById(R.id.course_day_content);
        startWeek = (EditText) findViewById(R.id.start_week_content);
        endWeek = (EditText) findViewById(R.id.end_week_content);
        startNum = (EditText) findViewById(R.id.course_start_num_content);
        endNum = (EditText) findViewById(R.id.course_end_num_content);
        courseName = (EditText) findViewById(R.id.course_name_content);
        location = (EditText) findViewById(R.id.course_location_content);
        courseSubmit = (Button) findViewById(R.id.course_submit);
        if(getIntent().getSerializableExtra("CourseInfo")!=null){
            CourseInfo courseInfo = (CourseInfo)getIntent().getSerializableExtra("CourseInfo");
            day.setText(courseInfo.getDay()+"");  //setText should be applied to string, not int.
            startWeek.setText(courseInfo.getStartWeek()+"");
            endWeek.setText(courseInfo.getEndWeek()+"");
            startNum.setText(courseInfo.getStartNum()+"");
            endNum.setText(courseInfo.getEndNum()+"");
            courseName.setText(courseInfo.getName());
            location.setText(courseInfo.getLocation());
        }
        courseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseInfo courseInfo = new CourseInfo.Builder()
                        .day(Integer.parseInt(day.getText().toString()))
                        .location(location.getText().toString())
                        .startWeek(Integer.parseInt(startWeek.getText().toString()))
                        .endWeek(Integer.parseInt(endWeek.getText().toString()))
                        .starNum(Integer.parseInt(startNum.getText().toString()))
                        .endNum(Integer.parseInt(endNum.getText().toString()))
                        .name(courseName.getText().toString()).build();
                if(1==1/*courseInfo内容合法*/){
                    MyCourseinfo.getInstace().add(courseInfo);
                    EncodeAndDecode.saveProduct(MyCourseinfo.getInstace());
                    EventBus.getDefault().post(new RefreshTimeTableEvent(true));
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.getInstance().toast("您输入的课程信息有误，请检查后重新输入");
                        }
                    });
                }
            }
        });
    }
}
