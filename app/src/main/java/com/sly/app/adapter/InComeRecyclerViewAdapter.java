package com.sly.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.viewHolder.EmptyViewHolder;
import com.sly.app.model.sly.InComeChartBean;
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
public class InComeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<InComeChartBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public InComeRecyclerViewAdapter(Context context, int[] mImgList, String[] mTag, LayoutInflater mLayoutInflater) {
        this.mImgList = mImgList;
        this.mTag = mTag;
        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
    }

    public InComeRecyclerViewAdapter(Context context, List<InComeChartBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public InComeRecyclerViewAdapter(Context context, List<InComeChartBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public InComeRecyclerViewAdapter(Context context, List<InComeChartBean> beanList, boolean isAddFootView) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_power_b, parent, false);
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
            final InComeChartBean gcBean = beanList.get(position);
            ViewHolder eholder = (ViewHolder) holder;
            eholder.chart_name.setText(gcBean.getMineName());
            eholder.tv2.setText("周总收益: ");
            eholder.tvRunCount.setText(String.format("%.2f", gcBean.getWeekIncome()));
            eholder.tvRunPresent.setText(String.format("%.2f", gcBean.getMonthIncome())+"");
            eholder.tv3.setText("月总收益：");
            eholder.tvRunWeekPresent.setText(String.format("%.2f", gcBean.getWeekIncome()));
            eholder.tvRunMonthPresent.setTextColor(Color.parseColor("#f29211"));
            eholder.tvRunMonthPresent.setText(String.format("%.2f", gcBean.getMonthIncome()));
            eholder.tvWeek1.setText("周总收益 ( ¥ ) :");
            eholder.tvMonth2.setText("月总收益 ( ¥ ):");
            eholder.iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            List<Double> data  = new ArrayList<>();
            List<String> date = new ArrayList<>();
            data.add(gcBean.getDate1Income());
            data.add(gcBean.getDate2Income());
            data.add(gcBean.getDate3Income());
            data.add(gcBean.getDate4Income());
            data.add(gcBean.getDate5Income());
            data.add(gcBean.getDate6Income());
            data.add(gcBean.getDate7Income());

            date.add(getDate(gcBean.getDate1()));
            date.add(getDate(gcBean.getDate2()));
            date.add(getDate(gcBean.getDate3()));
            date.add(getDate(gcBean.getDate4()));
            date.add(getDate(gcBean.getDate5()));
            date.add(getDate(gcBean.getDate6()));
            date.add(getDate(gcBean.getDate7()));
            eholder.barChart.setYData(
                    Integer.parseInt(new java.text.DecimalFormat("0").format(gcBean.getWeekIncome())),
                    0,
                    Integer.parseInt(new java.text.DecimalFormat("0").format(gcBean.getWeekIncome()/100)),
                    0);
            eholder.barChart.hideYLable();
            eholder.barChart.setData(data);
            eholder.barChart.setXData(date);
            eholder.barChart.refreshChart();
            eholder.itemView.setTag(position);
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
    public interface OnItemClickListener {
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
          chart_name = view.findViewById(R.id.chart_name);
          llSecoond = view.findViewById(R.id.ll_second);
            tv2 = view.findViewById(R.id.tv_2);
            tvRunCount = view.findViewById(R.id.tv_run_count);
            iv2 = view.findViewById(R.id.iv_2);
            tvRunPresent = view.findViewById(R.id.tv_run_present);
            tv3 = view.findViewById(R.id.tv_3);
            tvRunWeekPresent = view.findViewById(R.id.tv_run_week_present);
            tvWeek1 = view.findViewById(R.id.tv_week_1);
            tvRunMonthPresent = view.findViewById(R.id.tv_run_month_present);
            tvMonth2 = view.findViewById(R.id.tv_month_2);
            barChart = view.findViewById(R.id.bar_chart);

        }
    }
}
