package com.qianrushi.schooltimetable.viewpager.fragment.Two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.function.ParseGradeHtml;
import com.qianrushi.schooltimetable.function.SimulateLogin;
import com.qianrushi.schooltimetable.model.GradeInfo;
import com.qianrushi.schooltimetable.model.MyGradeInfoList;
import com.qianrushi.schooltimetable.utils.Util;

import java.util.List;

/**
 * Created by lwx on 2016/3/24.
 */
public class GradeFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    View rootView;
    GradeAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.fragment_grade, container, false);
        return rootView;
    }
    @Override
    public void onStart(){
        super.onStart();
        initRecyclerView();
    }
    @Override
    public void onResume(){
        super.onResume();
        if(SimulateLogin.hasLogin()){
            ParseGradeHtml.getInstance().init(this);
        }
    }
    public void notifyDataSetChanged(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void initRecyclerView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.grade_info);;
        layoutManager = new LinearLayoutManager(Util.getInstance().getContext());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        setAdapter();
    }
    public void setAdapter() {
        MyGradeInfoList myGradeInfoList = MyGradeInfoList.getInstance();
        List<GradeInfo> gradeInfoList = myGradeInfoList.getList();
        Log.e("gradeInfoList.size()", gradeInfoList.size()+"");
        recyclerView.setAdapter(adapter = new GradeAdapter(gradeInfoList));
        adapter.setmOnItemClickListener(new GradeOnItemClickListener() {
            @Override
            public void onItemClick(View view, GradeInfo item) {
                Util.getInstance().toast(item.getCourseName() + " pressed.", Toast.LENGTH_SHORT);
                //int i = myGradeInfoList.getList().indexOf(item);
            }
        });
    }
}
