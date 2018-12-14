package com.sly.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.sly.app.R;
import com.sly.app.model.master.MasterAllFreeBean;
import com.sly.app.model.master.MasterAllPowerBean;
import com.sly.app.utils.AppUtils;

import java.util.List;

public class BarMarkerView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;
    private List<MasterAllFreeBean> mPicDataList;
    private List<MasterAllPowerBean> masterAllPowerBeanList;

    public BarMarkerView(Context context, List<MasterAllFreeBean> list) {
        super(context, R.layout.my_bar_marker_view);
        this.mPicDataList = list;
        tvDate = findViewById(R.id.tv_date_time);
    }

    public BarMarkerView(List<MasterAllPowerBean> list, Context context) {
        super(context, R.layout.my_bar_marker_view);
        this.masterAllPowerBeanList = list;
        tvDate = findViewById(R.id.tv_date_time);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容

        if(!AppUtils.isListBlank(mPicDataList)){
            int position = (int)e.getX();
            MasterAllFreeBean bean = mPicDataList.get(position);
            tvDate.setText(bean.getDate().split(" ")[0]);
        }else{
            int position = (int)e.getX()-1;
            MasterAllPowerBean bean = masterAllPowerBeanList.get(position);
            tvDate.setText(bean.getDate().split(" ")[0]);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(int)(getHeight()*1.1));
    }
}
