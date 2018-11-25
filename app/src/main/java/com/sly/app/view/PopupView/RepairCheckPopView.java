package com.sly.app.view.PopupView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.sly.app.utils.ToastUtils;

import java.lang.reflect.Field;
import java.util.Calendar;

public class RepairCheckPopView extends PopupWindow implements View.OnClickListener{

    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private EditText etBillID;
    private EditText etAreaIP;
    private TextView tvBeginpTime;
    private TextView tvEndpTime;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private RadioButton cbStatus1;
    private RadioButton cbStatus2;

    private LinearLayout llBeginpTime;
    private LinearLayout llEndpTime;
    private LinearLayout llBeginTime;
    private LinearLayout llEndTime;
    private LinearLayout llRepairTime;
    private LinearLayout llChooseStatus;

    private int mCount = 1;
    private int mptimeCount = 0;
    private int metimeCount = 0;

    private int setTimePosition;
    private Dialog dialog;
    private DatePicker picker;
    private String defaultDate;

    public RepairCheckPopView(final Activity context, int count) {
        this.mContext = context;
        this.mCount = count;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_repair_check, null);

        etBillID = contentView.findViewById(R.id.et_repair_bill_id);
        etAreaIP = contentView.findViewById(R.id.et_repair_ip);

        llBeginpTime = contentView.findViewById(R.id.ll_repair_begin_ptime);
        llEndpTime = contentView.findViewById(R.id.ll_repair_end_ptime);
        llBeginTime = contentView.findViewById(R.id.ll_repair_begin_time);
        llEndTime = contentView.findViewById(R.id.ll_repair_end_time);

        tvBeginpTime = contentView.findViewById(R.id.tv_repair_begin_ptime);
        tvEndpTime = contentView.findViewById(R.id.tv_repair_end_ptime);
        tvBeginTime = contentView.findViewById(R.id.tv_repair_begin_time);
        tvEndTime = contentView.findViewById(R.id.tv_repair_end_time);

        cbStatus1 = contentView.findViewById(R.id.rb_repair_check_status1);
        cbStatus2 = contentView.findViewById(R.id.rb_repair_check_status2);

        llRepairTime = contentView.findViewById(R.id.ll_repair_check_endtime);
        llChooseStatus = contentView.findViewById(R.id.ll_chooseStatus);
        tvSearch = contentView.findViewById(R.id.tv_repair_confirm);
        tvReSet = contentView.findViewById(R.id.tv_repair_reset);

        initView(count);

        llBeginpTime.setOnClickListener(this);
        llEndpTime.setOnClickListener(this);
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

    private void initView(int Count) {
        Calendar calendar = Calendar.getInstance();
        defaultDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        tvBeginpTime.setText(defaultDate);
        tvEndpTime.setText(defaultDate);
        if(Count == 1){
            llRepairTime.setVisibility(View.GONE);
            llChooseStatus.setVisibility(View.GONE);
        }else if(Count == 2){
            llRepairTime.setVisibility(View.GONE);
            llChooseStatus.setVisibility(View.VISIBLE);
            cbStatus1.setText("处理中\n等待付款");
            cbStatus2.setText("处理中\n已付款");
        }else if(Count == 3){
            tvBeginTime.setText(defaultDate);
            tvEndTime.setText(defaultDate);
            llRepairTime.setVisibility(View.VISIBLE);
            llChooseStatus.setVisibility(View.VISIBLE);
            cbStatus1.setText("已处理");
            cbStatus2.setText("取消维修");
        }

    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public String[] getTextInfo(){
        String[] repairInfo = new String[8];
        repairInfo[0] = etBillID.getText().toString().trim();
        repairInfo[1] = etAreaIP.getText().toString().trim();
//        repairInfo[2] = tvBeginpTime.getText().toString().trim();
//        repairInfo[3] = tvEndpTime.getText().toString().trim();
//        repairInfo[4] = tvBeginTime.getText().toString().trim();
//        repairInfo[5] = tvEndTime.getText().toString().trim();
        repairInfo[2] = mptimeCount > 0 ? tvBeginpTime.getText().toString().trim() : "";
        repairInfo[3] = mptimeCount > 0 ? tvEndpTime.getText().toString().trim() : "";
        repairInfo[4] = metimeCount > 0 ? tvBeginTime.getText().toString().trim() : "";
        repairInfo[5] = metimeCount > 0 ? tvEndTime.getText().toString().trim() : "";
        repairInfo[6] = cbStatus1.isChecked() ? "true" : "false";
        repairInfo[7] = cbStatus2.isChecked() ? "true" : "false";
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
        switch (view.getId()){
            case R.id.ll_repair_begin_ptime:
                setTimePosition = 1;
                dialog();
                break;
            case R.id.ll_repair_end_ptime:
                setTimePosition = 2;
                dialog();
                break;
            case R.id.ll_repair_begin_time:
                setTimePosition = 3;
                dialog();
                break;
            case R.id.ll_repair_end_time:
                setTimePosition = 4;
                dialog();
                break;
            case R.id.tv_repair_confirm:
                this.dismiss();
                mOnSearchClickListener.onSearchClick(tvSearch, 0);
                break;
            case R.id.tv_repair_reset:
                etBillID.setText("");
                etAreaIP.setText("");
                mptimeCount = 0;
                metimeCount = 0;
                tvBeginpTime.setText(defaultDate);
                tvEndpTime.setText(defaultDate);
                tvBeginTime.setText(defaultDate);
                tvEndTime.setText(defaultDate);
                cbStatus1.setChecked(true);
                cbStatus2.setChecked(false);
                break;
            case R.id.repair_date_sel_cancel:
                dialog.dismiss();
                break;
            case R.id.repair_date_sel_ok:
                dialog.dismiss();
                String month = (picker.getMonth()+1 < 10) ? "0" + (picker.getMonth()+1) : "" + (picker.getMonth()+1);
                String day = (picker.getDayOfMonth() < 10) ? "0" + picker.getDayOfMonth() : "" + picker.getDayOfMonth();

                String date = picker.getYear() + "-" + month + "-" + day;
                if(setTimePosition == 1){
                    mptimeCount ++;
                    tvBeginpTime.setText(date);
                }else if(setTimePosition == 2){
                    // 判断后面时间是否大于前面时间
                    String before = tvBeginpTime.getText().toString();
                    if(!isThanBefore(before, date)){
                        mptimeCount ++;
                        tvEndpTime.setText(date);
                    }
                }else if(setTimePosition == 3){
                    metimeCount ++;
                    tvBeginTime.setText(date);
                }else if(setTimePosition == 4){
                    String before = tvBeginTime.getText().toString();
                    if(!isThanBefore(before, date)){
                        metimeCount ++;
                        tvEndTime.setText(date);
                    }
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

    private boolean isThanBefore(String before, String after){
        String[] btime = before.split("-");
        String[] atime = after.split("-");
        if(Integer.parseInt(btime[0]) > Integer.parseInt(atime[0])){
            ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
            return true;
        }else {
            if(Integer.parseInt(btime[1]) > Integer.parseInt(atime[1])){
                ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
                return true;
            }else{
                if(Integer.parseInt(btime[2]) > Integer.parseInt(atime[2])){
                    ToastUtils.showToast(mContext.getString(R.string.repair_than_time));
                    return true;
                }
                return false;
            }
        }
    }
}
