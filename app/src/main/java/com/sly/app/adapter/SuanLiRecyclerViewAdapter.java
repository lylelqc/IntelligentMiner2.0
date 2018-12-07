package com.sly.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.sly.SuanLiChartBean;
import com.sly.app.utils.CommonUtils;
import com.sly.app.view.chartView.BarChart03View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class SuanLiRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<SuanLiChartBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public SuanLiRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public SuanLiRecyclerViewAdapter(Context context, List<SuanLiChartBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public SuanLiRecyclerViewAdapter(Context context, List<SuanLiChartBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public SuanLiRecyclerViewAdapter(Context context, List<SuanLiChartBean> beanList, boolean isAddFootView) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_power_c, parent, false);
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
            final SuanLiChartBean gcBean = beanList.get(position);
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
            eholder.chart_name.setText(gcBean.getMineName());
            eholder.tv2.setText("24小时平均算力（TH/S）: ");
            eholder.tvRunCount.setText(String.format("%.2f", gcBean.getHour24Calc()));
            eholder.tvRunPresent.setText("");
            eholder.tv3.setText("");
            eholder.tvRunWeekPresent.setText(String.format("%.2f", gcBean.getWeekCalc()));
            eholder.tvRunMonthPresent.setText(String.format("%.2f", gcBean.getMonthCalc()));
            eholder.tvWeek1.setText("周平均算力（TH/S）");
            eholder.tvMonth2.setText("月平均算力（TH/S）");
            eholder.iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            List<Double> data = new ArrayList<>();
            List<String> date = new ArrayList<>();
            data.add(gcBean.getDate1Calc());
            data.add(gcBean.getDate2Calc());
            data.add(gcBean.getDate3Calc());
            data.add(gcBean.getDate4Calc());
            data.add(gcBean.getDate5Calc());
            data.add(gcBean.getDate6Calc());
            data.add(gcBean.getDate7Calc());

            date.add(getDate(gcBean.getDate1()));
            date.add(getDate(gcBean.getDate2()));
            date.add(getDate(gcBean.getDate3()));
            date.add(getDate(gcBean.getDate4()));
            date.add(getDate(gcBean.getDate5()));
            date.add(getDate(gcBean.getDate6()));
            date.add(getDate(gcBean.getDate7()));

            eholder.barChart.setYData(100,0,1,0);
            eholder.barChart.hideYLable();
            eholder.barChart.setData(data);
            eholder.barChart.setXData(date);
            eholder.barChart.refreshChart();
            eholder.itemView.setTag(position);
//        }
    }

    private String getDate(String date) {
        if(!CommonUtils.isBlank(date)){
            String[] split = date.split("-");
            String[] split1 = split[2].split(" ");
            return split[1] + "/" + split1[0];
        }
        return "";
    }

    @Override
    public int getItemCount() {
//        return beanList.size() > 0 ? beanList.size() + 1 : 1;
        return beanList.size();
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
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv2;
        TextView tvRunCount;
        ImageView iv2;
        TextView tvRunPresent;
        TextView tv3;
        TextView tvRunWeekPresent;
        TextView tvWeek1;
        TextView tvRunMonthPresent;
        TextView tvMonth2;
        TextView chart_name;
        BarChart03View barChart;
        LinearLayout llSecoond;

        public ViewHolder(View view) {
            super(view);
            chart_name = (TextView) view.findViewById(R.id.chart_name);
            llSecoond = (LinearLayout) view.findViewById(R.id.ll_second);
            tv2 = (TextView) view.findViewById(R.id.tv_2);
            tvRunCount = (TextView) view.findViewById(R.id.tv_run_count);
            iv2 = (ImageView) view.findViewById(R.id.iv_2);
            tvRunPresent = (TextView) view.findViewById(R.id.tv_run_present);
            tv3 = (TextView) view.findViewById(R.id.tv_3);
            tvRunWeekPresent = (TextView) view.findViewById(R.id.tv_run_week_present);
            tvWeek1 = (TextView) view.findViewById(R.id.tv_week_1);
            tvRunMonthPresent = (TextView) view.findViewById(R.id.tv_run_month_present);
            tvMonth2 = (TextView) view.findViewById(R.id.tv_month_2);
            barChart = (BarChart03View) view.findViewById(R.id.bar_chart);

        }
    }
}
