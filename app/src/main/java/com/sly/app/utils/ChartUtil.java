package com.sly.app.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sly.app.R;
import com.sly.app.model.yunw.machine.MachineDetailsPicBean;
import com.sly.app.view.MyMarkerView;

import java.util.ArrayList;
import java.util.List;

public class ChartUtil {


    /**
     * 曲线图初始化
     * @param context
     * @param lineChart
     * @param list
     */
    public static void initCalPowerPic(Context context, LineChart lineChart, List<MachineDetailsPicBean> list) {
        //显示边界
        /***图表设置***/
        //右下角标题设无
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //设置背景色
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        //设置不能缩放
        lineChart.setScaleEnabled(false);
//        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
//        lineChart.setPinchZoom(true);

//        float ratio = (float) mPicDataList.size()/(float) 10;
//        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
//        lineChart.zoom(ratio,1f,0,0);

        // 向左偏移15dp，抵消y轴向右偏移的30dp
//        lineChart.setExtraLeftOffset(-30);

        /***XY轴的设置***/
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYaxis = lineChart.getAxisRight();

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);
        // 设置Y轴字体大小、颜色
        int color = context.getResources().getColor(R.color.sly_text_999999);
        leftYAxis.setTextColor(color);
        leftYAxis.setTextSize(12f);
//        leftYAxis.setXOffset(-45);

        //禁止网格线,设置线条颜色
        int color1 = context.getResources().getColor(R.color.sly_line_ececec);
        xAxis.setGridColor(color1);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        leftYAxis.setGridColor(color1);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setDrawAxisLine(false);
        //设置网格虚线
//        leftYAxis.enableGridDashedLine(10f, 0f, 0f);
        rightYaxis.setDrawGridLines(false);
        rightYaxis.setEnabled(false);

        //自定义X轴的值
        // 设置X轴分割数量
        xAxis.setLabelCount(6,false);
//        formatXValues(list);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });

        //自定义y轴的值
        leftYAxis.setLabelCount(6);
        leftYAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (value > 1 ? Math.round(value) : value)  + "T";
            }
        });

        /***折线图例 标签 设置***/
        Legend legend = lineChart.getLegend();
        //设置图例
//        legend.setEnabled(false);
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(0f);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        //点击显示详情框
        MyMarkerView mkv = new MyMarkerView(context, xAxis.getValueFormatter(), list);
        mkv.setChartView(lineChart);
        lineChart.setMarker(mkv);
        lineChart.invalidate();
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param lineChart  曲线图表
     * @param lineColor    曲线颜色
     * @param bgColor    背景颜色
     */
    public static void showLineChart(List<MachineDetailsPicBean> dataList, LineChart lineChart, int lineColor, int bgColor) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            MachineDetailsPicBean data = dataList.get(i);
            float yValue = Float.parseFloat(data.getCalcForce());
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, yValue);
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        initLineDataSet(lineDataSet, lineColor, bgColor);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    private static void initLineDataSet(LineDataSet lineDataSet, int lineColor, int bgColor) {
        lineDataSet.setColor(lineColor);
        lineDataSet.setCircleColor(lineColor);
        lineDataSet.setLineWidth(2.f);
        lineDataSet.setCircleRadius(1.f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        //是否画高亮线
        lineDataSet.setHighlightEnabled(true);
        //画高亮线的横线
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        //画高亮线的竖线
        lineDataSet.setDrawVerticalHighlightIndicator(true);
//        lineDataSet.setValueTextSize(10f);
        lineDataSet.setHighlightLineWidth(1.5f);
        lineDataSet.setHighLightColor(lineColor);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(bgColor);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(0f);
        //设置曲线条
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //设置是否显示值
        lineDataSet.setDrawValues(false);
    }

}
