package com.sly.app.view.PopupView.Yunw;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.adapter.yunw.goline.PlanAreaRecyclerViewAdapter;
import com.sly.app.model.yunw.goline.PlanAreaBean;
import com.sly.app.view.MyGridItemDecoration;
import com.sly.app.view.MyStaggeredGridLayoutManager;

import java.util.List;

public class PlanAreaCheckPopView extends PopupWindow implements View.OnClickListener {

    private PlanAreaRecyclerViewAdapter adapter;
    private View contentView;
    private Context mContext;

    private TextView tvSearch;
    private TextView tvReSet;

    private RecyclerView recyclerView;

    //    private Integer index = -1;
    private List<PlanAreaBean> areaBeanList;

    public PlanAreaCheckPopView(final Activity context, List<PlanAreaBean> areaList){
        this.mContext = context;
        this.areaBeanList = areaList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_manage_area, null);

        recyclerView = contentView.findViewById(R.id.recycler_view);
        MyStaggeredGridLayoutManager mLayoutManager = new MyStaggeredGridLayoutManager(2, MyStaggeredGridLayoutManager.VERTICAL);
        MyGridItemDecoration lineVertical = new MyGridItemDecoration(MyGridItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(lineVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PlanAreaRecyclerViewAdapter(mContext, areaList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tvReSet = contentView.findViewById(R.id.tv_manage_area_reset);
        tvSearch = contentView.findViewById(R.id.tv_manage_area_confirm);
        tvReSet.setText(mContext.getString(R.string.dialog_cancel));

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

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
//            this.showAtLocation(parent, Gravity.RIGHT,0,0);
        } else {
            this.dismiss();
        }
    }

    public String getPlanAreaCode(){
        if(!adapter.getAreaSet().isEmpty()){
            String areaCode = areaBeanList.get(adapter.getAreaSet().iterator().next()).getAreaCode();
            return areaCode;
        }
        return "";
    }

    public void setPlanAreaSearchClickListener(OnSearchClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    private OnSearchClickListener mOnSearchClickListener = null;

    //define interface
    public interface OnSearchClickListener {
        void onPlanAreaSearchClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manage_area_reset:
                clearAll();
                this.dismiss();
                break;
            case R.id.tv_manage_area_confirm:
                this.dismiss();
                mOnSearchClickListener.onPlanAreaSearchClick(tvSearch, 0);
                break;
        }
    }

    public void clearAll(){
        adapter.setAreaSetNull();
        adapter.notifyDataSetChanged();
    }
}
