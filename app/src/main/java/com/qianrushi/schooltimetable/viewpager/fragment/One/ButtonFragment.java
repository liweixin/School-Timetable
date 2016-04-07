package com.qianrushi.schooltimetable.viewpager.fragment.One;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.activity.SimulateLoginAcitivity;

/**
 * Created by lwx on 2016/3/28.
 */
public class ButtonFragment extends Fragment implements View.OnClickListener{
    private View mView;
    public static final int LOGIN = 512;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        mView = inflater.inflate(R.layout.fragment_button, container, false);
        mView.findViewById(R.id.import_course).setOnClickListener(this);
        mView.findViewById(R.id.add_course).setOnClickListener(this);
        mView.findViewById(R.id.delete_course).setOnClickListener(this);
        mView.findViewById(R.id.edit_course).setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.import_course:
                Toast.makeText(getActivity().getApplicationContext(), "Import course", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getActivity(), WebViewActivity.class);
                Intent intent = new Intent(getActivity(), SimulateLoginAcitivity.class);
                getActivity().startActivityForResult(intent, LOGIN);
                break;
            case R.id.add_course:
                Toast.makeText(getActivity().getApplicationContext(), "Add course.", Toast.LENGTH_LONG).show();
                break;
            case R.id.delete_course:
                Toast.makeText(getActivity().getApplicationContext(), "Delete course.", Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_course:
                Toast.makeText(getActivity().getApplicationContext(), "Edit course.", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
