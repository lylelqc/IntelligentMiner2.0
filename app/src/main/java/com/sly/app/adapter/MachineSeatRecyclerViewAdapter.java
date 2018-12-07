package com.sly.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sly.app.R;
import com.sly.app.activity.machineSeat.MachineSeatDetailActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.http.NetWorkCons;
import com.sly.app.model.sly.MachineSeatDataBean;
import com.sly.app.utils.CommonUtil2;
import com.sly.app.utils.GlideImgManager;
import com.sly.app.utils.SharedPreferencesUtil;
import com.sly.app.utils.StringUtils;
import com.sly.app.utils.ToastUtils;

import java.util.List;

/**
 * 作者 by K
 * 时间：on 2017/9/4 10:00
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class MachineSeatRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private String storeType;
    private int[] mImgList;
    private String[] mTag;
    private Context context;
    private static final int EMPTY_VIEW = 5;
    private final LayoutInflater mLayoutInflater;
    private List<Integer> lists;
    private List<MachineSeatDataBean> beanList;
    private String TyepName;
    private boolean isAddFootView = true;

    public MachineSeatRecyclerViewAdapter(Context context, List<MachineSeatDataBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public MachineSeatRecyclerViewAdapter(Context context, List<MachineSeatDataBean> beanList, String TypeName) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.TyepName = TypeName;
    }

    public MachineSeatRecyclerViewAdapter(Context context, List<MachineSeatDataBean> beanList, boolean isAddFootView) {
        this.context = context;
        this.beanList = beanList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isAddFootView = isAddFootView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == EMPTY_VIEW) {
//            View view = LayoutInflater.from(context).inflate(R.layout.view_list_footer, parent, false);
//            if (isAddFootView == false) {
//                EmptyViewHolder holder = new EmptyViewHolder(view, false);
//                return holder;
//            } else {
//                EmptyViewHolder holder = new EmptyViewHolder(view, false);
//                return holder;
//            }
//        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_machine_seta, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof EmptyViewHolder) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//            p.setFullSpan(true);
//        } else {
        final ViewHolder eholder = (ViewHolder) holder;


           if (beanList.size()!=0){
               final MachineSeatDataBean bean = beanList.get(position);

               eholder.mTitle.setText(bean.getMine20_Name());
               String introduce = bean.getMine79_Intro().replace("u000a", "");
               eholder.mMsg.setText(introduce);
               eholder.mAddress.setText(bean.getProvince());
               eholder.mNumber.setText(bean.getMine79_Count()+"");
//               if (bean.getMine79_MinPrice()!=null && bean.getMine79_MaxPrice()!=null){
               eholder.mPrice.setText("服务费："+bean.getMine79_MinPrice()+"-"+bean.getMine79_MaxPrice());
//               }
               if(bean.getMine79_AuditStatusCode().equals("1")){
                   eholder.mAuthIcon.setImageResource(R.drawable.authentication_icon_1);
               }else{
                   eholder.mAuthIcon.setImageResource(R.drawable.authentication_icon_2);
               }
               GlideImgManager.glideLoader(context, NetWorkCons.IMG_URL+bean.getMine18_ImageURl(), R.drawable.seat_photo, R.drawable.seat_photo, eholder.mImg);
//               Glide.with(context)
//                       .load(NetWorkCons.IMG_URL+bean.getMine35_Pic())
//                       .error(R.drawable.seat_head_portrait)
//                       .into(eholder.mImg);
               eholder.mRlItem.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       String Token = SharedPreferencesUtil.getString(context, "Token");
                       String User = SharedPreferencesUtil.getString(context, "User");
                       if (StringUtils.isEmpty(Token) || StringUtils.isEmpty(User)) {
                           ToastUtils.showToast("请先登录");
                           CommonUtil2.goActivity(context, LoginActivity.class);
                      }else{
                           Intent intent = new Intent(context, MachineSeatDetailActivity.class);
                           intent.putExtra("MachineSeatDataBean",bean);
                           context.startActivity(intent);
                      }
                   }
               });

               if(bean.getIsDelete().equals("True")){
                   eholder.mDeleteSeat.setVisibility(View.VISIBLE);
               }else{
                   eholder.mDeleteSeat.setVisibility(View.GONE);
               }

               eholder.mDeleteSeat.setTag(position);
               eholder.mDeleteSeat.setOnClickListener(this);

               //将position保存在itemView的Tag中，以便点击时进行获取
               eholder.itemView.setTag(position);
//           }
        }
    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (beanList.size() == 0 || beanList.size() < position + 1) {
            return EMPTY_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        ImageView mAuthIcon;
        TextView mTitle;
        TextView mMsg;
        TextView mAddress;
        TextView mNumber;
        TextView mPrice;
        RelativeLayout mRlItem;
        RelativeLayout mDeleteSeat;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.id_head_machine);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mAuthIcon = (ImageView) view.findViewById(R.id.iv_rz_status);
            mMsg = (TextView) view.findViewById(R.id.tv_msg);
            mAddress = (TextView) view.findViewById(R.id.tv_address);
            mNumber = (TextView) view.findViewById(R.id.tv_number);
            mPrice = (TextView) view.findViewById(R.id.tv_price);
            mRlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
            mDeleteSeat = (RelativeLayout) view.findViewById(R.id.rl_delete_seat);
        }
    }
}
