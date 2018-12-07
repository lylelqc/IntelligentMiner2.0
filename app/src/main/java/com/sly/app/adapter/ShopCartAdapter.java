package com.sly.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.model.GoodsInfo;
import com.sly.app.model.StoreInfo;
import com.sly.app.utils.GlideImgManager;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者 by K
 * 时间：on 2017/9/13 15:24
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class ShopCartAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<StoreInfo> groups;
    private Map<String, List<GoodsInfo>> children;

    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param children 子元素列表
     * @param context
     */
    public ShopCartAdapter(List<StoreInfo> groups, Map<String, List<GoodsInfo>> children, Context context) {
        this.groups = groups;
        this.children = children;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return groups.size();//店铺总数
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<GoodsInfo> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        final GroupViewHolder mGroupHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_scart_group_layout, null);
            mGroupHolder = new GroupViewHolder(view);
            view.setTag(mGroupHolder);
        } else {
            mGroupHolder = (GroupViewHolder) view.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        mGroupHolder.mTvSourceName.setText(group.getName());
        mGroupHolder.mDetermineChekbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
            }
        });
        mGroupHolder.mDetermineChekbox.setChecked(group.isChoosed());
        if (group.isEdtor()) {
            mGroupHolder.mTvStoreEdtor.setText("完成");
        } else {
            mGroupHolder.mTvStoreEdtor.setText("编辑");
        }
        mGroupHolder.mTvStoreEdtor.setOnClickListener(new GroupViewClick(groupPosition, mGroupHolder.mTvStoreEdtor, group));
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        final ChildViewHolder mChildHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_scart_child_layout, null);
            mChildHolder = new ChildViewHolder(view);
            view.setTag(mChildHolder);
        } else {
            mChildHolder = (ChildViewHolder) view.getTag();
        }
        if (groups.get(groupPosition).isEdtor()) {
            mChildHolder.mLlEditLayout.setVisibility(View.VISIBLE);
            mChildHolder.mRlNoEdtor.setVisibility(View.GONE);
        } else {
            mChildHolder.mLlEditLayout.setVisibility(View.GONE);
            mChildHolder.mRlNoEdtor.setVisibility(View.VISIBLE);
        }
        final GoodsInfo goodsInfo = (GoodsInfo) getChild(groupPosition, childPosition);
        if (goodsInfo != null) {
            mChildHolder.mTvGoodsName.setText(goodsInfo.getComTitle());
            mChildHolder.mTvGoodsNum.setText(goodsInfo.getCount() + "");
            mChildHolder.mTvGoodsType.setText(goodsInfo.getOptionIDCombin() + "");
            mChildHolder.mTvGoodsPrice.setText(goodsInfo.getPrice() + "");
            GlideImgManager.glideLoader(mContext, goodsInfo.getComPicUrl().replace("40-40","400-400"), R.drawable.common_details_carousel_placeholder, mChildHolder.mIvGoodsPic);
            mChildHolder.mCbSelect.setChecked(goodsInfo.isChoosed());
            mChildHolder.mCbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    mChildHolder.mCbSelect.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            mChildHolder.mTvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// 暴露增加接口
                    modifyCountInterface.doIncrease(groupPosition, childPosition, mChildHolder.mTvNum, mChildHolder.mTvNum.getText().toString(), mChildHolder.mCbSelect.isChecked());
                }
            });
            mChildHolder.mTvReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// 暴露删减接口
                    modifyCountInterface.doDecrease(groupPosition, childPosition, mChildHolder.mTvNum, mChildHolder.mTvNum.getText().toString(), mChildHolder.mCbSelect.isChecked());
                }
            });
            //预留左滑删除
            mChildHolder.mTvGoodsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  //删除 购物车
                    modifyCountInterface.childDelete(groupPosition, childPosition);
                }
            });
        }
        return view;
    }

    //子类点击事件 true/ false
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * Group 父类 view 店铺
     */
    static class GroupViewHolder {
        @BindView(R.id.determine_chekbox)
        CheckBox mDetermineChekbox;
        @BindView(R.id.tv_source_name)
        TextView mTvSourceName;
        @BindView(R.id.tv_store_edtor)
        Button mTvStoreEdtor;
        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Child 子类view 店铺
     */
    static class ChildViewHolder {
        @BindView(R.id.cb_select)
        CheckBox mCbSelect;
        @BindView(R.id.iv_goods_pic)
        ImageView mIvGoodsPic;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_goods_type)
        TextView mTvGoodsType;
        @BindView(R.id.textView)
        TextView mTextView;
        @BindView(R.id.tv_goods_price)
        TextView mTvGoodsPrice;
        @BindView(R.id.tv_goods_num)
        TextView mTvGoodsNum;
        @BindView(R.id.ll_textview)
        LinearLayout mLlTextview;
        @BindView(R.id.rl_no_edtor)
        LinearLayout mRlNoEdtor;
        @BindView(R.id.tv_reduce)
        TextView mTvReduce;
        @BindView(R.id.tv_num)
        EditText mTvNum;
        @BindView(R.id.tv_add)
        TextView mTvAdd;
        @BindView(R.id.ll_change_num)
        LinearLayout mLlChangeNum;
        @BindView(R.id.id_tv_price_now)
        TextView mIdTvPriceNow;
        @BindView(R.id.id_tv_des_now)
        TextView mIdTvDesNow;
        @BindView(R.id.ll_layout)
        LinearLayout mLlLayout;
        @BindView(R.id.tv_goods_delete)
        TextView mTvGoodsDelete;
        @BindView(R.id.ll_edit_layout)
        LinearLayout mLlEditLayout;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //供总编辑按钮调用
    public void setupEditingAll(boolean isEditingAll) {
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo storeBean = groups.get(i);
            storeBean.setIsEdtor(isEditingAll);
        }
        notifyDataSetChanged();
    }

    /**
     * 使某个组处于编辑状态
     * <p/>
     * groupPosition组的位置
     */
    class GroupViewClick implements View.OnClickListener {
        private int groupPosition;
        private Button edtor;
        private StoreInfo group;

        public GroupViewClick(int groupPosition, Button edtor, StoreInfo group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setIsEdtor(false);
                } else {
                    group.setIsEdtor(true);
                }
                notifyDataSetChanged();
            }
        }
    }

    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, String num, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, String num, boolean isChecked);

        /**
         * 删除子item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }


}
