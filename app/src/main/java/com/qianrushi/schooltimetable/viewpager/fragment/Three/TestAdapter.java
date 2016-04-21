package com.qianrushi.schooltimetable.viewpager.fragment.Three;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.GradeInfo;
import com.qianrushi.schooltimetable.model.TestInfo;
import com.qianrushi.schooltimetable.utils.Util;

import java.util.List;

/**
 * Created by lwx on 2016/1/31.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> implements View.OnClickListener {

    private TestOnItemClickListener mOnItemClickListener = null;
    public void setmOnItemClickListener(TestOnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public List<TestInfo> datas;
    public TestAdapter(List<TestInfo> datas){
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_info_new_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TestInfo testInfo = datas.get(position);
        holder.courseName.setText(testInfo.getCourseName());
        holder.testTime.setText(testInfo.getTime() + ""); //attention, must call setText(String) instead of setText(int resourceId)
        holder.testLocation.setText(testInfo.getLocation()+"");
        holder.itemView.setTag(testInfo);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v){
        if (mOnItemClickListener!=null) {
            mOnItemClickListener.onItemClick(v, (TestInfo) v.getTag());
        }
    }

    public void addItem(int position, TestInfo item){
        datas.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(GradeInfo item){
        int position = datas.indexOf(item);
        if(position>=0) {
            datas.remove(position);
            notifyItemRemoved(position);
        } else {
            Util.getInstance().toast("列表中不存在" + item.getCourseName() + "了哟" + "(´・∀・｀)", Toast.LENGTH_SHORT);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView courseName;
        public TextView testTime;
        public TextView testLocation;
        public ViewHolder (View view){
            super(view);
            courseName = (TextView) view.findViewById(R.id.course_name);
            testTime = (TextView) view.findViewById(R.id.test_time);
            testLocation = (TextView) view.findViewById(R.id.test_location);
        }
    }
}