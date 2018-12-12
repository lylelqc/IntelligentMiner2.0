package com.sly.app.adapter.master;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.master.MasterPersonListBean;

import java.util.List;

public class PersonFrameExpandListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private Context mContext;
    private List<MasterPersonListBean> firstData;  //一级数据
    private List<List<MasterPersonListBean>> childData; //二级数据
    private SparseArray<ImageView> mIndicators;

    public PersonFrameExpandListAdapter(Context context,
                             List<MasterPersonListBean> firstData, List<List<MasterPersonListBean>> childData) {
        this.mContext = context;
        this.firstData = firstData;
        this.childData = childData;
        this.mIndicators=new SparseArray<>();
    }

    public void delete(int groupPosition) {
        firstData.remove(groupPosition);
        childData.remove(groupPosition);
        notifyDataSetInvalidated();
    }

    public void delete(int groupPosition, int childPosition){
        childData.get(groupPosition).remove(childPosition);
        notifyDataSetInvalidated();
    }

    @Override
    public int getGroupCount() {
        return firstData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childData.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return childData.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_frame_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.ivOpenStatus = convertView.findViewById(R.id.iv_open_status);
            groupViewHolder.tvGroupName = convertView.findViewById(R.id.tv_manager_name);
            groupViewHolder.btnDelete1 = convertView.findViewById(R.id.delete);

            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder= (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.tvGroupName.setText(firstData.get(groupPosition).getMineName());

        groupViewHolder.btnDelete1.setTag(groupPosition + "-null");
        groupViewHolder.btnDelete1.setOnClickListener(this);
        //把要随着状态改变的imageView 指示器添加到集合里面
        mIndicators.put(groupPosition, groupViewHolder.ivOpenStatus);
        //改变指示器展开或者关闭的显示
        setIndicatorState(groupPosition, isExpanded);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_frame_child, parent,false);
            childViewHolder=new ChildViewHolder();
            childViewHolder.tvChildName = convertView.findViewById(R.id.tv_manager_child_name);
            childViewHolder.btnDelete2 = convertView.findViewById(R.id.delete);
            convertView.setTag(childViewHolder);

        }else{
            childViewHolder= (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvChildName.setText(childData.get(groupPosition).get(childPosition).getMineName());

        childViewHolder.btnDelete2.setTag(groupPosition + "-" + childPosition);
        childViewHolder.btnDelete2.setOnClickListener(this);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setOnClickDeleteListenter(OnClickDeleteListenter listener) {
        this.onClickDeleteListenter = listener;
    }

    @Override
    public void onClick(View v) {
        if (onClickDeleteListenter != null) {
            onClickDeleteListenter.onItemClick(v, (String) v.getTag());
        }
    }

    private OnClickDeleteListenter onClickDeleteListenter = null;

    //define interface
    public static interface OnClickDeleteListenter {
        void onItemClick(View view, String position);
    }

    class GroupViewHolder {
        ImageView ivOpenStatus;
        TextView tvGroupName;
        Button btnDelete1;
    }

    class ChildViewHolder {
        TextView tvChildName;
        Button btnDelete2;
    }

    //设置展开收起的指示器
    public void setIndicatorState(int position, boolean isExpanded){
        if (isExpanded){
            //从集合中取出指示器的imageview,改变图片的显示
            mIndicators.get(position).setImageResource(R.drawable.miners_open);
        }else{
            mIndicators.get(position).setImageResource(R.drawable.miners_retract);
        }
    }
}
