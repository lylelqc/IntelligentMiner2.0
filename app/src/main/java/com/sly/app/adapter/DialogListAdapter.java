package com.sly.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.yunw.repair.RepairSparesBean;

import java.util.List;

public class DialogListAdapter extends BaseAdapter implements View.OnClickListener{

    private Activity mContext;
    private ListView mListView;
    private List<RepairSparesBean> mList;

    public DialogListAdapter(Activity context, List<RepairSparesBean> list, ListView lv){
        this.mContext = context;
        this.mList = list;
        this.mListView = lv;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = mContext.getLayoutInflater().inflate(R.layout.list_item_goods, viewGroup, false);

            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv_select_goods);
            holder.imageCheck = convertView.findViewById(R.id.iv_select_icon);
            holder.dialogRl = convertView.findViewById(R.id.rl_dialog_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mList.get(position).getPartName());
        holder.dialogRl.setTag(position);
        holder.dialogRl.setOnClickListener(this);

        //选中的item设置状态
        if (mListView.isItemChecked(position)){
            holder.imageCheck.setImageResource(R.drawable.select_weixiu_icon);
        } else {
            holder.imageCheck.setImageResource(R.drawable.no_select_weixiu_icon);
        }

        return convertView;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public void setOnItemClickListener(DialogListAdapter.OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    private DialogListAdapter.OnItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private static class ViewHolder
    {
        TextView textView;
        ImageView imageCheck;
        RelativeLayout dialogRl;
    }
}
