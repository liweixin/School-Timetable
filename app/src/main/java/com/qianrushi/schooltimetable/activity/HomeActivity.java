package com.qianrushi.schooltimetable.activity;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.event.RefreshTimeTableEvent;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.EncodeAndDecode;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.viewpager.fragment.One.ButtonFragment;
import com.qianrushi.schooltimetable.viewpager.fragment.Three.TestFragment;
import com.qianrushi.schooltimetable.viewpager.fragment.Two.GradeFragment;
import com.qianrushi.schooltimetable.viewpager.fragment.Four.MoreFragment;
import com.qianrushi.schooltimetable.viewpager.fragment.One.TimeTableFragment;

import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends FragmentActivity implements OnClickListener, AppCompatCallback {

    /**
     * 四个导航按钮
     */
    Button buttonOne;
    Button buttonTwo;
    Button buttonThree;
    Button buttonFour;

    /**
     * 作为页面容器的ViewPager
     */
    ViewPager mViewPager;
    /**
     * 页面集合
     */
    List<Fragment> fragmentList;

    /**
     * 四个Fragment（页面）
     */
    TimeTableFragment timeTableFragment;
    GradeFragment gradeFragment;
    TestFragment testFragment;
    MoreFragment moreFragment;

    //覆盖层
    ImageView imageviewOvertab;

    //屏幕宽度
    int screenWidth;
    //当前选中的项
    int currenttab=-1;

    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        delegate.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setOnMenuItemClickListener(listener);

        buttonOne=(Button)findViewById(R.id.btn_one);
        buttonTwo=(Button)findViewById(R.id.btn_two);
        buttonThree=(Button)findViewById(R.id.btn_three);
        buttonFour=(Button)findViewById(R.id.btn_four);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);

        mViewPager=(ViewPager) findViewById(R.id.viewpager);

        fragmentList=new ArrayList<Fragment>();
        timeTableFragment =new TimeTableFragment();
        gradeFragment=new GradeFragment();
        testFragment =new TestFragment();
        moreFragment=new MoreFragment();

        fragmentList.add(timeTableFragment);
        fragmentList.add(gradeFragment);
        fragmentList.add(testFragment);
        fragmentList.add(moreFragment);

        screenWidth=getResources().getDisplayMetrics().widthPixels;

        buttonTwo.measure(0, 0);
        imageviewOvertab=(ImageView) findViewById(R.id.imgv_overtab);
        RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(screenWidth/4, buttonTwo.getMeasuredHeight());
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageviewOvertab.setLayoutParams(imageParams);

        mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    /**
     * 定义自己的ViewPager适配器。
     * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
     */
    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
    {

        public MyFrageStatePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container)
        {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem=mViewPager.getCurrentItem();
            if (currentItem==currenttab)
            {
                return ;
            }
            imageMove(mViewPager.getCurrentItem());
            currenttab=mViewPager.getCurrentItem();
        }

    }

    /**
     * 移动覆盖层
     * @param moveToTab 目标Tab，也就是要移动到的导航选项按钮的位置
     * 第一个导航按钮对应0，第二个对应1，以此类推
     */
    private void imageMove(int moveToTab)
    {
        int startPosition=0;
        int movetoPosition=0;

        startPosition=currenttab*(screenWidth/4);
        movetoPosition=moveToTab*(screenWidth/4);
        //平移动画
        TranslateAnimation translateAnimation=new TranslateAnimation(startPosition,movetoPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        imageviewOvertab.startAnimation(translateAnimation);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_one:
                changeView(0);
                break;
            case R.id.btn_two:
                changeView(1);
                gradeFragment.onSelect();
                break;
            case R.id.btn_three:
                changeView(2);
                testFragment.onSelect();
                break;
            case R.id.btn_four:
                changeView(3);
                break;
            default:
                break;
        }
    }
    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        mViewPager.setCurrentItem(desTab, true);
    }

    private static boolean shown = false;

    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_add:
                    if(fragmentList.get(0)!=null){
                        TimeTableFragment fragment = (TimeTableFragment)fragmentList.get(0);
                        if(!shown){
                            fragment.addFragment();
                            shown = true;
                        } else {
                            fragment.removeFragment();
                            shown = false;
                        }
                    }
                    break;
                default:break;
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为了让 Toolbar 的 Menu 有作用，此处代码不可省略
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        Log.e("requestCode", requestCode+"");
        Log.e("resultCode", resultCode+"");
        switch (requestCode){
            case ButtonFragment.LOGIN:
                List<CourseInfo> courseInfoList = (List<CourseInfo>) intent.getSerializableExtra("courseList");
                MyCourseinfo.setCourseInfo(courseInfoList);//update my courseInfo
                EventBus.getDefault().post(new RefreshTimeTableEvent(true));
                EncodeAndDecode.saveProduct(courseInfoList);
                break;
            case MoreFragment.SCAN_QRCODE:
                ((MoreFragment)fragmentList.get(3)).onScanResult(resultCode, intent);
                break;
        }
    }
}