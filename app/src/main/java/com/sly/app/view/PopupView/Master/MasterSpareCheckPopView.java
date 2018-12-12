package com.sly.app.view.PopupView.Master;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.master.MasterSpareCheckRecyclerViewAdapter;
import com.sly.app.adapter.yunw.machine.MachineTypeRecyclerViewAdapter;
import com.sly.app.model.yunw.repair.RepairSparesBean;
import com.sly.app.utils.ToastUtils;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

public class MasterSpareCheckPopView extends PopupWindow implements View.OnClickListener {

    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private TextView tvBeginTime;
    private TextView tvEndTime;

    private LinearLayout llBeginTime;
    private LinearLayout llEndTime;
    private final RecyclerView recyclerView;
    private final MasterSpareCheckRecyclerViewAdapter adapter;
    private final List<RepairSparesBean> mResultList;

    private Dialog dialog;
    private DatePicker picker;
    private String defaultDate;
    private int mTimeCount = 0;

    public MasterSpareCheckPopView(final Activity context, List<RepairSparesBean> beanList) {
        this.mContext = context;
        this.mResultList = beanList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_master_spare_check, null);

        llBeginTime = contentView.findViewById(R.id.ll_master_spare_begin_time);
        llEndTime = contentView.findViewById(R.id.ll_master_spare_end_time);

        tvBeginTime = contentView.findViewById(R.id.tv_master_spare_begin_time);
        tvEndTime = contentView.findViewById(R.id.tv_master_spare_end_time);

        recyclerView = contentView.findViewById(R.id.recycler_view);
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(4, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(lineVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MasterSpareCheckRecyclerViewAdapter(mContext, beanList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tvSearch = contentView.findViewById(R.id.tv_master_spare_confirm);
        tvReSet = contentView.findViewById(R.id.tv_master_spare_reset);

        initView();

        llBeginTime.setOnClickListener(this);
        llEndTime.setOnClickListener(this);
        tvReSet.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

    }

    private void initView() {
        Calendar calendar = Calendar.getInstance();
        defaultDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        tvBeginTime.setText(defaultDate);
        tvEndTime.setText(defaultDate);
    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public String[] getTextInfo() {
        String[] repairInfo = new String[3];
        repairInfo[0] = mResultList.get(adapter.getIndex()).getPartID();
        repairInfo[1] = mTimeCount > 0 ? tvBeginTime.getText().toString().trim() : "";
        repairInfo[2] = mTimeCount > 0 ? tvEndTime.getText().toString().trim() : "";
        return repairInfo;
    }

    public void setSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onSearchClick(View view, int position);
    }

    private void dialog() {
        dialog = new Dialog(mContext, R.style.quick_option_dialog);
        dialog.setContentView(R.layout.dialog_date_picker);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setWindowAnimations((R.style.popWindow_anim_style));
        dialog.show();
        Button btnSelCancel = dialog.findViewById(R.id.repair_date_sel_cancel);
        Button btnSelOk = dialog.findViewById(R.id.repair_date_sel_ok);
        picker = dialog.findViewById(R.id.date_picker);
//        ((NumberPicker) ((ViewGroup) ((ViewGroup) picker.getChildAt(0)).getChildAt(0)).getChildAt(1)).setDisplayedValues(mDisplayMonths);
        //设置分割线颜色
        setDatePickerDividerColor();

        btnSelCancel.setOnClickListener(this);
        btnSelOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_master_spare_begin_time:
                dialog();
                break;
            case R.id.ll_master_spare_end_time:
                dialog();
                break;
            case R.id.tv_master_spare_confirm:
                this.dismiss();
                mOnSearchClickListener.onSearchClick(tvSearch, 0);
                break;
            case R.id.tv_master_spare_reset:
                tvBeginTime.setText(defaultDate);
                tvEndTime.setText(defaultDate);
                mTimeCount = 0;
                break;
            case R.id.repair_date_sel_cancel:
                dialog.dismiss();
                break;
            case R.id.repair_date_sel_ok:
                dialog.dismiss();
                String month = (picker.getMonth() + 1 < 10) ? "0" + (picker.getMonth() + 1) : "" + (picker.getMonth() + 1);
                String day = (picker.getDayOfMonth() < 10) ? "0" + picker.getDayOfMonth() : "" + picker.getDayOfMonth();
                String date = picker.getYear() + "-" + month + "-" + day;
                // 判断后面时间是否大于前面时间
                String before = tvBeginTime.getText().toString();
                if (!isThanBefore(before, date)) {
                    mTimeCount ++;
                    tvEndTime.setText(date);
                }

                break;
        }
    }

    /**
     * 设置时间选择器的分割线颜色
     */
    private void setDatePickerDividerColor() {
        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) picker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(Color.parseColor("#7cbcf2")));//设置分割线颜色
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private boolean isThanBefore(String before, String after) {
        String[] btime = before.split("-");
        String[] atime = after.split("-");
        if (Integer.parseInt(btime[0]) > Integer.parseInt(atime[0])) {
            ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
            return true;
        } else {
            if (Integer.parseInt(btime[1]) > Integer.parseInt(atime[1])) {
                ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
                return true;
            } else {
                if (Integer.parseInt(btime[2]) > Integer.parseInt(atime[2])) {
                    ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
                    return true;
                }
                return false;
            }
        }
    }
}
