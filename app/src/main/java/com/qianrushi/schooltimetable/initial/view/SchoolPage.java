package com.qianrushi.schooltimetable.initial.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.initial.presenter.ISetSchool;
import com.qianrushi.schooltimetable.initial.presenter.SetSchool;
import com.qianrushi.schooltimetable.viewpager.HomeActivity;

/**
 * Created by lwx on 2016/3/27.
 */
public class SchoolPage extends BaseActivity implements ISetSchoolPage {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.view_set_school);
        isFirstLogin();
        final ISetSchool setSchool = new SetSchool();
        final EditText edit = (EditText) findViewById(R.id.input_text);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = edit.getText().toString();
                if (st.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.not_complete, Toast.LENGTH_LONG).show();
                } else {
                    setSchool.setSchool(edit.getText().toString());
                    startActivity(new Intent(SchoolPage.this, CollegePage.class));
                }
            }
        });
    }
    private void isFirstLogin(){
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("isRemember", false);
        if(isRemember){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}