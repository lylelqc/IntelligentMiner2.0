package com.sly.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.DeviceBean;

import java.util.List;

import vip.devkit.library.ListUtil;

public class DeviceAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private int groupResId, childResId;
    private List<DeviceBean> mGroupList;

    public DeviceAdapter(Context context, int groupResId, int childResId, List<DeviceBean> groups) {
        this.mContext = context;
        this.groupResId = groupResId;
        this.childResId = childResId;
        this.mGroupList = groups;
    }

    @Override
    public int getGroupCount() {
        return ListUtil.isEmpty(mGroupList) ? 0 : mGroupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (!ListUtil.isEmpty(mGroupList) && !ListUtil.isEmpty(mGroupList.get(i).getChildBeans())) {
            return mGroupList.get(i).getChildBeans().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return mGroupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mGroupList.get(i).getChildBeans().get(i1);
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
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(final int i, final boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder = ViewHolder.get(mContext, view, viewGroup, groupResId, i);
//        holder.setText(R.id.tv_group_name, mGroupList.get(i).getGroupName());
//        final ImageView imageView = holder.getView(R.id.iv_group_expand);
//        if (b) {
//            imageView.setImageResource(R.drawable.ic_address_delete);
//        } else {
//            imageView.setImageResource(R.drawable.ic_address_delete);
//        }
        return holder.getConvertView();
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder = ViewHolder.get(mContext, view, viewGroup, childResId, i1);
//        TextView textView = holder.getView(R.id.tv_child_name);
//        textView.setText(mGroupList.get(i).getChildBeans().get(i1).getChildName());
        if (mGroupList.get(i).getChildBeans().get(i1).isStudy()) {
//            textView.setTextColor(Color.parseColor("#fb5780"));
        } else {
//            textView.setTextColor(Color.parseColor("#333333"));
        }
        return holder.getConvertView();
    }

}
