package com.sly.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.sly.app.R;
import com.sly.app.activity.master.MasterAllFreeActivity;
import com.sly.app.model.master.MasterAllFreeBean;
import com.sly.app.model.master.MasterAllPowerBean;
import com.sly.app.model.yunw.machine.MachineDetailsPicBean;
import com.sly.app.view.BarMarkerView;
import com.sly.app.view.MyMarkerView;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BarUtil {

    private BarChart barChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例

    public BarUtil(BarChart barChart) {
        this.barChart = barChart;
        leftAxis = this.barChart.getAxisLeft();
        rightAxis = this.barChart.getAxisRight();
        xAxis = this.barChart.getXAxis();
        legend = barChart.getLegend();
    }

    /**
     * 图初始化
     * @param context
     * @param barChart
     * @param list
     */
    public void initCalPowerPic(Context context, BarChart barChart, List<?> list, boolean showLengend) {
        /***图表设置***/
        //背景颜色
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        // 不显示边界
        barChart.setDrawBorders(false);

        //设置不能缩放
        barChart.setScaleEnabled(false);
//        lineChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);

        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //设置动画效果
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        // 设置Y轴字体大小、颜色
        int color = context.getResources().getColor(R.color.sly_text_999999);
        leftAxis.setTextColor(color);
        leftAxis.setTextSize(12f);

        // 设置网格线参数
        int color1 = context.getResources().getColor(R.color.sly_line_ececec);
        xAxis.setGridColor(color1);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        leftAxis.setGridColor(color1);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        BarMarkerView mkv;
        /***折线图例 标签 设置***/
        if(showLengend){
            int color2 = context.getResources().getColor(R.color.sly_text_333333);
            legend.setFormSize(6);
            legend.setForm(Legend.LegendForm.SQUARE);
            legend.setTextSize(12f);
            legend.setTextColor(color2);
            //显示位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            //是否绘制在图表里面
            legend.setDrawInside(false);

            List<MasterAllFreeBean> lists = (List<MasterAllFreeBean>) list;
            mkv = new BarMarkerView(context, lists);
        }else{
            legend.setEnabled(showLengend);
            List<MasterAllPowerBean> lists = (List<MasterAllPowerBean>) list;
            mkv = new BarMarkerView(lists, context);
        }

        //点击显示详情框
        mkv.setChartView(barChart);
        barChart.setMarker(mkv);
        barChart.invalidate();
    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     * @param color
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);

        initBarDataSet(barDataSet, color);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
//        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                return xValues.get((int) Math.abs(value) % xValues.size());
                return "";
            }
        });
        barChart.setData(data);
    }

    /**
     * 展示柱状图(隔开)
    /**
     * @param xValues   X轴的值
     * @param dataLists LinkedHashMap<String, List<Float>>
     *                  key对应柱状图名字  List<Float> 对应每类柱状图的Y值
     * @param colors
     */
    public void showBarChart(final List<String> xValues, LinkedHashMap<String, List<Float>> dataLists,
                             @ColorRes List<Integer> colors) {

        List<IBarDataSet> dataSets = new ArrayList<>();
        int currentPosition = 0;//用于柱状图颜色集合的index

        for (LinkedHashMap.Entry<String, List<Float>> entry : dataLists.entrySet()) {
            String name = entry.getKey();
            List<Float> yValueList = entry.getValue();

            List<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < yValueList.size(); i++) {
                /**
                 *  如果需要添加TAG标志 可使用以下构造方法
                 *  BarEntry(float x, float y, Object data)
                 *  e.getData()
                 */
                entries.add(new BarEntry(i, yValueList.get(i)));
            }
            // 每一个BarDataSet代表一类柱状图
            BarDataSet barDataSet = new BarDataSet(entries, name);
            initBarDataSet(barDataSet, colors.get(currentPosition));
            dataSets.add(barDataSet);

            currentPosition++;
        }

        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                return xValues.get((int) Math.abs(value) % xValues.size());
                return "";
            }
        });

        //右侧Y轴自定义值
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) (value) + "";
            }
        });

        BarData data = new BarData(dataSets);

        /**
         * float groupSpace = 0.3f;   //柱状图组之间的间距
         * float barSpace =  0.05f;  //每条柱状图之间的间距  一组两个柱状图
         * float barWidth = 0.3f;    //每条柱状图的宽度     一组两个柱状图
         * (barWidth + barSpace) * 2 + groupSpace = (0.3 + 0.05) * 2 + 0.3 = 1.00
         * 3个数值 加起来 必须等于 1 即100% 按照百分比来计算 组间距 柱状图间距 柱状图宽度
         */
        int barAmount = dataLists.size(); //需要显示柱状图的类别 数量
        //设置组间距占比30% 每条柱状图宽度占比 70% /barAmount  柱状图间距占比 0%
        float groupSpace = 0.3f; //柱状图组之间的间距
        float barWidth = (1f - groupSpace) / barAmount;
        float barSpace = 0f;
        //设置柱状图宽度
        data.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0f, groupSpace, barSpace);
        barChart.setData(data);
    }

    /**
     * 展示柱状图(多条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param labels
     * @param colors
     */
    public void showBarChart(List<Float> xAxisValues, List<List<Float>> yAxisValues, List<String> labels, List<Integer> colors) {
        BarData data = new BarData();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {

                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            initBarDataSet(barDataSet, colors.get(i));
            data.addDataSet(barDataSet);
        }

        int amount = yAxisValues.size(); //需要显示柱状图的类别 数量
        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        //柱状图宽度
        data.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0, groupSpace, barSpace);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                return xValues.get((int) Math.abs(value) % xValues.size());
                return "";
            }
        });

        barChart.setData(data);
    }


    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     *
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(8.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(color);
        barDataSet.setHighLightAlpha(1);
    }
}
