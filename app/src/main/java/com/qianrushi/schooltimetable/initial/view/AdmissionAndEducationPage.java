package com.qianrushi.schooltimetable.initial.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.initial.presenter.ISetAdmissionTimeAndEducation;
import com.qianrushi.schooltimetable.initial.presenter.SetAdmissionTimeAndEducation;;

/**
 * Created by lwx on 2016/3/27.
 */
public class AdmissionAndEducationPage extends BaseActivity implements ISetAdmissionTimeAndEducationPage {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.view_set_admission_education);
        final ISetAdmissionTimeAndEducation admissionTimeAndEducation = new SetAdmissionTimeAndEducation();
        final EditText edit1 = (EditText) findViewById(R.id.input_text);
        final EditText edit2 = (EditText) findViewById(R.id.input_text_2);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st1 = edit1.getText().toString();
                String st2 = edit2.getText().toString();
                if (st1.equals("")||st2.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.not_complete, Toast.LENGTH_LONG).show();
                } else {
                    admissionTimeAndEducation.setAdmissionTime(st1);
                    admissionTimeAndEducation.setEducation(st2);
                    startActivity(new Intent(AdmissionAndEducationPage.this, ConfirmUserInfoPage.class));
                }
            }
        });
    }
}
