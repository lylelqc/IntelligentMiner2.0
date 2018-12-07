package com.sly.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.sly.app.R;
import com.sly.app.adapter.base.CommonAdapter;
import com.sly.app.adapter.base.ViewHolder;
import com.sly.app.model.AddressBean;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/20 13:36
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class AddressAdapter extends CommonAdapter<AddressBean> {
    List<AddressBean> mDatas;
    Context mContext;

    public AddressAdapter(Context context, List<AddressBean> mDatas, int layoutId) {
        super(context, mDatas, layoutId);
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public void convert(ViewHolder holder, final AddressBean addressBean, final int i) {
        holder.setText(R.id.tv_name, addressBean.getM045_Consignee()).setText(R.id.tv_tell, addressBean.getM045_Tel()).setText(R.id.tv_address, addressBean.getM045_Address());
        View view = holder.getView(R.id.view_bg);
        if (i==0){
            view.setBackgroundResource(R.drawable.order_address_cor);
        }
        CheckBox ivDefault = holder.getView(R.id.iv_default);
        LinearLayout lLDelete = holder.getView(R.id.ll_delete);
        ivDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    proInterface.defaultAddress(i,addressBean.getM045_ID()+"",b);
                    notifyDataSetChanged();
            }
        });
        lLDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proInterface.delAddress(i,addressBean.getM045_ID()+"");
                notifyDataSetChanged();
            }
        });
    }
    private ProInterface proInterface;

    public void setProInterface(ProInterface proInterface) {
        this.proInterface = proInterface;
    }
    /**
     * 回调接口
     */
    public interface ProInterface {
        /**
         * 删除
         * @param position 元素位置
         */
        void delAddress(int position, String id);

        /**
         * 设置默认
         *
         * @param id    地址ID
         */
        void defaultAddress(int position, String id, boolean isDefault);
    }
}
