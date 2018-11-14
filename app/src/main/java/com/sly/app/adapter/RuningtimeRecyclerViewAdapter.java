package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.MinerMasterChartBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class RuningtimeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<MinerMasterChartBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public RuningtimeRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public RuningtimeRecyclerViewAdapter(Context context, List<MinerMasterChartBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public RuningtimeRecyclerViewAdapter(Context context, List<MinerMasterChartBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public RuningtimeRecyclerViewAdapter(Context context, List<MinerMasterChartBean> beanList, boolean isAddFootView) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isAddFootView = isAddFootView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == EMPTY_VIEW) {
//            View view = LayoutInflater.from(context).inflate(R.layout.view_list_footer, parent, false);
//            if (isAddFootView == false) {
//                EmptyViewHolder holder = new EmptyViewHolder(view, false);
//                return holder;
//            } else {
//                EmptyViewHolder holder = new EmptyViewHolder(view, true);
//                return holder;
//            }
//        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_runtime_chart, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof EmptyViewHolder) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//            p.setFullSpan(true);
//        } else {
            final MinerMasterChartBean gcBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
//            if (!CommonUtils.isBlank(gcBean.getDescript())) {
//                Glide.with(context)
//                        .load(gcBean.getDescript().replace("40-40", "400-400"))
//                        .error(R.drawable.seat_head_portrait)
//                        .into(eholder.mImg);
//            }
//            else if (gcBean.getImgList().size()>0){
//                Glide.with(context)
//                        .load(gcBean.getImgList().get(0).replace("40-40", "400-400"))
//                        .error(R.drawable.seat_head_portrait)
//                        .into(eholder.mImg);
//            }else {
//                Glide.with(context)
//                        .load("")
//                        .error(R.drawable.common_details_carousel_placeholder)
//                        .into(eholder.mImg);
//            }
//            eholder.mTyep.setText(CommonUtils.getDoubleStr(Double.valueOf(gcBean.getMartPrice())));
//            eholder.mTime.setText(gcBean.getComTitle());
//            eholder.mNumber.setText("100");
//            eholder.mP.setText("新疆");
//            eholder.mPalce.setText("电价：0.01-0.04元/千瓦时");
            eholder.tv2.setText(gcBean.getMineName()+"运行设备(台)  : ");
            eholder.tvRunCount.setText(gcBean.getMachineCount()+"");
            eholder.iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            double rate = gcBean.getRunRate()*100;
            eholder.tvRunPresent.setText(String.format("%.2f",rate)+"%");

//            eholder.tvRunWeekPresent.setText(gcBean.getRunRate()*100+"%");
//            eholder.tvRunMonthPresent.setText(gcBean.getRunRate()*100+"%");
//            List<Double> data  = new ArrayList<>();
//            List<String> Xdata  = new ArrayList<>();
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));
//            data.add(Double.parseDouble(gcBean.getRunRate()*100+""));

//            Xdata.add("运行率");
//            Xdata.add("运行率");
//            Xdata.add("运行率");
//            Xdata.add("运行率");
//            Xdata.add("运行率");
//            Xdata.add("运行率");
//            Xdata.add("运行率");
//
//        date.add(getDate(gcBean.getDate1()));
//        date.add(getDate(gcBean.getDate2()));
//        date.add(getDate(gcBean.getDate3()));
//        date.add(getDate(gcBean.getDate4()));
//        date.add(getDate(gcBean.getDate5()));
//        date.add(getDate(gcBean.getDate6()));
//        date.add(getDate(gcBean.getDate7()));
//            eholder.barChart.setYData(100,0,20,0);
//            eholder.barChart.setData(data);
//            eholder.barChart.setXData(Xdata);
//            eholder.barChart.refreshChart();
//            eholder.itemView.setTag(position);
//        }
    }
    private String getDate(String date){
        String[] split = date.split("-");
        String[] split1 = split[2].split(" ");
        return split[1] +"/"+ split1[0];
    }

    @Override
    public int getItemCount() {
//        return beanList.size() > 0 ? beanList.size() + 1 : 1;
        return beanList.size() ;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv2;
        TextView tvRunCount;
        ImageView iv2;
        TextView tvRunPresent;
        TextView tv3;
//        TextView tvRunWeekPresent;
//        TextView tvWeek1;
//        TextView tvRunMonthPresent;
//        TextView tvMonth2;
//        BarChart03View barChart;
      public ViewHolder(View view) {
            super(view);
            tv2 = view.findViewById(R.id.tv_2);
            tvRunCount = view.findViewById(R.id.tv_run_count);
            iv2 = view.findViewById(R.id.iv_2);
            tvRunPresent = view.findViewById(R.id.tv_run_present);
            tv3 = view.findViewById(R.id.tv_3);
//            tvRunWeekPresent = (TextView) view.findViewById(R.id.tv_run_week_present);
//            tvWeek1 = (TextView) view.findViewById(R.id.tv_week_1);
//            tvRunMonthPresent = (TextView) view.findViewById(R.id.tv_run_month_present);
//            tvMonth2 = (TextView) view.findViewById(R.id.tv_month_2);
//            barChart = (BarChart03View) view.findViewById(R.id.bar_chart);

        }
    }
}
