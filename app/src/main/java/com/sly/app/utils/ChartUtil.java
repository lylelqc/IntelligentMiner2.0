package com.sly.app.utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartUtil {

    private static void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(2.f);
        lineDataSet.setCircleRadius(2.f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        //是否画高亮线
        lineDataSet.setHighlightEnabled(true);
        //画高亮线的横线
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        //画高亮线的竖线
        lineDataSet.setDrawVerticalHighlightIndicator(true);
//        lineDataSet.setValueTextSize(10f);
        lineDataSet.setHighlightLineWidth(2.0f);
        lineDataSet.setHighLightColor(color);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(10.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
        //设置是否显示值
        lineDataSet.setDrawValues(false);
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param lineChart  曲线图表
     * @param color    曲线颜色
     */
    public static void showLineChart(List<?> dataList, LineChart lineChart, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
//            MDetailsPicDataBean data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
//            Entry entry = new Entry(i, (float) data.getMine63_NowCalcForce());
//            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        initLineDataSet(lineDataSet, color, null);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

}
