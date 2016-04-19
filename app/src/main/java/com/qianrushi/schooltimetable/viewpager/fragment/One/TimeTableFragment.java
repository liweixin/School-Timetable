package com.qianrushi.schooltimetable.viewpager.fragment.One;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.activity.EditCourseActivity;
import com.qianrushi.schooltimetable.event.RefreshTimeTableEvent;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.EncodeAndDecode;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lwx on 2016/3/24.
 */
public class TimeTableFragment extends Fragment implements View.OnClickListener{
    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;
    private View mView;
    public ButtonFragment fragment;
    boolean set;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshTimeTableEvent refresh){
        if(refresh.isRefresh()){
            //clearCourseLayout();
            //addCourseView();
        }
    }
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        if(!set){
            EventBus.getDefault().register(this);
            set = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        mView = inflater.inflate(R.layout.fragment_timetable, container, false);
        getSize();
        return mView;
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
    public void clearCourseLayout(){
        TextView blank = new TextView(getContext());
        blank.setText("");
        RelativeLayout layout = (RelativeLayout)mView.findViewById(R.id.Sunday);
        layout.removeAllViews();
        //layout.addView(blank);
        layout = (RelativeLayout)mView.findViewById(R.id.Saturday);
        layout.removeAllViews();
        //layout.addView(blank);
        layout = (RelativeLayout)mView.findViewById(R.id.Friday);
        layout.removeAllViews();
        //layout.addView(blank);
        layout = (RelativeLayout)mView.findViewById(R.id.Thursday);
        layout.removeAllViews();
        //layout.addView(blank);
        layout = (RelativeLayout)mView.findViewById(R.id.Wednesday);
        layout.removeAllViews();
        //layout.addView(blank);
        layout = (RelativeLayout)mView.findViewById(R.id.Tuesday);
        layout.removeAllViews();
        //layout.addView(blank);
        layout = (RelativeLayout)mView.findViewById(R.id.Monday);
        layout.removeAllViews();
        //layout.addView(blank);
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
                }
                return true;
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(MyCourseinfo.getInstace()!=null){
            new Thread(new Runnable() { //需要这样写才能显示课程。为什么？
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clearCourseLayout();
                            addCourseView();
                        }
                    });
                }
            }).start();
        }
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
    public void addView(int i,int start,int end,String text,CourseInfo courseInfo){
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
        tv = createTv(start,end,text);
        tv.setTag(courseInfo);
        tv.setBackgroundResource(R.drawable.corner_view);
        GradientDrawable myGrad = (GradientDrawable)tv.getBackground();
        myGrad.setColor(Color.argb(100, start * 5, (start + end) * 20, 0));
        tv.setClickable(true);
        tv.setOnClickListener(this);
        layout.addView(tv);
    }

    @Override
    public void onClick(final View v) {
        Log.e("view", v.toString());
        showPopupWindow(v);
        initButton(v);
    }
    View view;
    PopupWindow popupWindow;
    private void showPopupWindow(View v){
        View contentView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.pop_window, null);
        view = contentView;
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        //popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, location[0] + v.getWidth(), location[1]);
    }
    private void initButton(final View courseView){
        //View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.pop_window, null);  如果重新获取一次，点击按钮无响应
        Button addCourseButton = (Button) view.findViewById(R.id.add_course_button);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null) popupWindow.dismiss();
                Log.e("tag", "click");
                Intent intent = new Intent(getActivity(), EditCourseActivity.class);
                startActivity(intent);
            }
        });
        Button editCourseButton = (Button) view.findViewById(R.id.edit_course_button);
        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null) popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), EditCourseActivity.class);
                intent.putExtra("CourseInfo", (CourseInfo)courseView.getTag());
                startActivity(intent);
            }
        });
        Button deleteCourseButton = (Button) view.findViewById(R.id.delete_course_button);
        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null) popupWindow.dismiss();
                ((RelativeLayout) (courseView.getParent())).removeView(courseView);
                CourseInfo courseInfo = (CourseInfo) courseView.getTag();
                MyCourseinfo.getInstace().remove(courseInfo);
                EncodeAndDecode.saveProduct(MyCourseinfo.getInstace());
                EventBus.getDefault().post(new RefreshTimeTableEvent(true));
            }
        });
    }
    @Override
    public void onStop(){
        super.onStop();
        /*if(popupWindow!=null){
            popupWindow.dismiss();
        }*/
    }
}

