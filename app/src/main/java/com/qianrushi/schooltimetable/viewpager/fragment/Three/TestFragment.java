package com.qianrushi.schooltimetable.viewpager.fragment.Three;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.qianrushi.schooltimetable.activity.SimulateLoginAcitivity;
import com.qianrushi.schooltimetable.event.TestParseEvent;
import com.qianrushi.schooltimetable.function.ParseTestHtml;
import com.qianrushi.schooltimetable.function.SimulateLogin;
import com.qianrushi.schooltimetable.model.MyTestInfoList;
import com.qianrushi.schooltimetable.model.TestInfo;
import com.qianrushi.schooltimetable.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by lwx on 2016/3/24.
 */
public class TestFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    View rootView;
    TestAdapter adapter;
    EventBus eventBus;
    public TestFragment(){
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEvent(TestParseEvent event){
        notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.fragment_test, container, false);
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
            //启动时执行查询考试信息函数
            SimulateLogin.getInstance().getTest();
        }
    }
    public void notifyDataSetChanged(){
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
    public void initRecyclerView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.test_info);
        recyclerView.addItemDecoration(new TestDecoration(getContext(), TestDecoration.HORIZONTAL_LIST, 10));
        layoutManager = new LinearLayoutManager(Util.getInstance().getContext());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        setAdapter();
    }
    public void setAdapter() {
        MyTestInfoList myTestInfoList = MyTestInfoList.getInstance();
        List<TestInfo> testInfoList = myTestInfoList.getList();
        Log.e("gradeInfoList.size()", testInfoList.size()+"");
        recyclerView.setAdapter(adapter = new TestAdapter(testInfoList));
        adapter.setmOnItemClickListener(new TestOnItemClickListener() {
            @Override
            public void onItemClick(View view, TestInfo item) {
                Util.getInstance().toast(item.getCourseName() + " pressed.", Toast.LENGTH_SHORT);
                //int i = myGradeInfoList.getList().indexOf(item);
            }
        });
    }
    public void onSelect(){
        if(SimulateLogin.hasLogin()){
            SimulateLogin.getInstance().getGrade();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("无法获取考试信息");
            dialog.setMessage("请保证网络通畅并且已经使用jaccount登录");
            dialog.setCancelable(true);
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.setPositiveButton("jaccount登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getContext(), SimulateLoginAcitivity.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        }
    }
}
