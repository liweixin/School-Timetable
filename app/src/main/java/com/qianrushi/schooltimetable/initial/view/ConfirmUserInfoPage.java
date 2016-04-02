package com.qianrushi.schooltimetable.initial.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.model.UserInfo;
import com.qianrushi.schooltimetable.initial.presenter.ConfirmInfo;
import com.qianrushi.schooltimetable.viewpager.HomeActivity;

/**
 * Created by lwx on 2016/3/27.
 */
public class ConfirmUserInfoPage extends BaseActivity implements IConfirmUserInfoPage{
    @Override
    public void onCreate(Bundle savedInstace){
        super.onCreate(savedInstace);
        setContentView(R.layout.view_confirm_info);
        ConfirmInfo reference = new ConfirmInfo();
        final UserInfo userInfo = reference.getUserInfo();
        TextView school = (TextView) findViewById(R.id.school);
        TextView college = (TextView) findViewById(R.id.college);
        TextView admission = (TextView) findViewById(R.id.admission_time);
        TextView education = (TextView) findViewById(R.id.education);
        school.setText("学校：" + userInfo.getSchool());
        college.setText("院系：" + userInfo.getCollege());
        admission.setText("入学时间：" + userInfo.getAdmissionTime());
        education.setText("学历：" + userInfo.getEducation());
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("school", userInfo.getSchool());
                editor.putString("college", userInfo.getCollege());
                editor.putString("admissionTime", userInfo.getAdmissionTime());
                editor.putString("education", userInfo.getEducation());
                editor.putBoolean("isRemember", true);
                editor.commit();
                startActivity(new Intent(ConfirmUserInfoPage.this, HomeActivity.class));
            }
        });
    }
}
