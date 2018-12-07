package com.sly.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.model.balance.BusinessInfo;

import java.util.List;

/**
 * Created by 01 on 2017/6/9.
 */
public class BusinessAdapter extends BaseAdapter {

    private List<BusinessInfo> mlist;
    private Context mContext;
    public BusinessAdapter(List<BusinessInfo> list, Context context){
        this.mlist=list;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder mHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_business, parent, false);
            mHolder=new ViewHolder();
            mHolder.tv_data=(TextView) convertView.findViewById(R.id.tv_data);
            mHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_sum= (TextView) convertView.findViewById(R.id.tv_sum);
            mHolder.tv_remark= (TextView) convertView.findViewById(R.id.tv_remark);
            mHolder.iv_status= (ImageView) convertView.findViewById(R.id.iv_status);
            mHolder.iv= (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.tv_data.setText(mlist.get(position).getP002_Time());
        String[] data=mlist.get(position).getP002_Time().split(" ");
        mHolder.tv_data.setText(data[0]);
        mHolder.tv_time.setText(data[1]);
        mHolder.tv_sum.setText(mlist.get(position).getP002_Sum()+"");
        mHolder.tv_remark.setText(mlist.get(position).getP002_NotesSystem());
        if(mlist.get(position).getP002_BalanceBefore()<mlist.get(position).getP002_BalanceAfter()){
            Glide.with(mContext).load(R.drawable.my_wallet1_recordnumber_on).into(mHolder.iv_status);
        }else{
            Glide.with(mContext).load(R.drawable.my_wallet2_recordnumber_unde).into(mHolder.iv_status);
        }

//        BusinessResquetBean.DataBean dataBean = mlist.get(position);
//        mHolder.tv_sum.setText(dataBean.getPay08_Sum()+"");
//        mHolder.tv_remark.setText(dataBean.getPay08_Remark());
//        if(dataBean.getPay08_Sum()<=0){
//            Glide.with(mContext).load(R.drawable.my_down).into(mHolder.iv_status);
//        }else{
//            Glide.with(mContext).load(R.drawable.my_upward).into(mHolder.iv_status);
//        }
//
//        String[] data=dataBean.getPay08_Time().split(" ");
//        mHolder.tv_data.setText(data[0]);
//        mHolder.tv_time.setText(data[1]);
        return convertView;
    }
    class ViewHolder {
        TextView tv_data;
        TextView tv_time;
        TextView tv_sum;
        TextView tv_remark;
        ImageView iv_status;
        ImageView iv;
    }
}
