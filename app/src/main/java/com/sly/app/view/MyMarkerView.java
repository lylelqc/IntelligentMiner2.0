package com.sly.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineDetailsPicBean;

import java.util.List;

public class MyMarkerView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;
    private List<?> mPicDataList;

    public MyMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, List<MachineDetailsPicBean> list) {
        super(context, R.layout.my_marker_view);
        this.mPicDataList = list;

        tvValue = findViewById(R.id.txt_tips);
        tvDate = findViewById(R.id.tv_date_time);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
//        tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
        int position = (int)e.getX();
        MachineDetailsPicBean bean = (MachineDetailsPicBean) mPicDataList.get(position);
        tvValue.setText(bean.getNowCalcForce() +"T");
        tvDate.setText(bean.getDataTime());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
