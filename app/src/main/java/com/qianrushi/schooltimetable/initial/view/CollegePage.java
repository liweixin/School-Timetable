package com.qianrushi.schooltimetable.initial.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.initial.presenter.ISetCollege;
import com.qianrushi.schooltimetable.initial.presenter.SetCollege;

/**
 * Created by lwx on 2016/3/27.
 */
public class CollegePage extends BaseActivity implements ISetCollegePage{
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.view_set_college);
        final ISetCollege college = new SetCollege();
        final EditText edit = (EditText) findViewById(R.id.input_text);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = edit.getText().toString();
                if (st.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.not_complete, Toast.LENGTH_LONG).show();
                } else {
                    college.setCollege(edit.getText().toString());
                    startActivity(new Intent(CollegePage.this, AdmissionAndEducationPage.class));
                }
            }
        });
    }
}
