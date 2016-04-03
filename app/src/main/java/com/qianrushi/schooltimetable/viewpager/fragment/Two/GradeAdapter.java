package com.qianrushi.schooltimetable.viewpager.fragment.Two;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.model.GradeInfo;
import com.qianrushi.schooltimetable.utils.Util;

import java.util.List;

/**
 * Created by lwx on 2016/1/31.
 */
public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> implements View.OnClickListener {

    private GradeOnItemClickListener mOnItemClickListener = null;
    public void setmOnItemClickListener(GradeOnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public List<GradeInfo> datas;
    public GradeAdapter(List<GradeInfo> datas){
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_info_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GradeInfo gradeInfo = datas.get(position);
        holder.courseName.setText(gradeInfo.getCourseName());
        holder.credit.setText(gradeInfo.getCredit()+""); //attention, must call setText(String) instead of setText(int resourceId)
        holder.grade.setText(gradeInfo.getGrade()+"");
        holder.itemView.setTag(gradeInfo);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v){
        if (mOnItemClickListener!=null) {
            mOnItemClickListener.onItemClick(v, (GradeInfo) v.getTag());
        }
    }

    public void addItem(int position, GradeInfo item){
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
        public TextView credit;
        public TextView grade;
        public ViewHolder (View view){
            super(view);
            courseName = (TextView) view.findViewById(R.id.course_name);
            credit = (TextView) view.findViewById(R.id.credit);
            grade = (TextView) view.findViewById(R.id.grade);
        }
    }
}