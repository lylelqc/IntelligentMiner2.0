package com.sly.app.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sly.app.R;
import com.sly.app.model.GlideRoundTransform;
import com.sly.app.view.CircleTransform;

/**
 * 构造一个通用的viewholder类
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;

    //layoutId即我们要引入的item布局文件的id
    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        this.mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);//引入我们的item布局文件
        mConvertView.setTag(this);
    }

    //ViewHolder并不是每次都需要实例化，当convertview不为空时我们就不需要再实例化ViewHolder，因此我们增加一个入口方法
    //来判断是否需要对ViewHolder实例化
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);//返回一个实例化对象
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过viewId获取控件,此方法返回的是View的一个子类
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            //如果mViews没有相应的控件，我们就从convertView中找到这个控件，并将此控件和其id存放在mViews中
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //mConvertView的get方法
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 找到我们定义的TextView控件，并给控件赋值
     */
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    /**
     * 找到我们定义的TextView控件，并给控件赋值
     * @param viewId
     * @param text
     * @param flag  Paint.STRIKE_THRU_TEXT_FLAG
     * @return
     */
    public ViewHolder setText(int viewId, String text,int flag) {
        TextView tv = getView(viewId);
        tv.getPaint().setFlags(flag);
        tv.setText(text);
        return this;
    }
    /**
     * 如果item中有ImageView控件的话，我们就可以用此方法给控件设置图片资源
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }
    /**
     * 如果item中有ImageView控件的话，我们就可以用此方法给控件设置图片资源
     */
    public ViewHolder setImageURL(int viewId, String ResUrl) {
        ImageView imageView = getView(viewId);
        Glide.with(mContext).load(ResUrl).error(R.drawable.common_details_carousel_placeholder).into(imageView);
        return this;
    }
    /**
     * 如果item中有ImageView控件的话，我们就可以用此方法给控件设置图片资源
     */
    public ViewHolder setImageURLHP(int viewId, String ResUrl) {
        ImageView imageView = getView(viewId);
        Glide.with(mContext).load(ResUrl).placeholder(R.drawable.icon_app).error(R.drawable.icon_app).transform(new CircleTransform(mContext)).into(imageView);
        return this;
    }


    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 如果item中有ImageView控件的话，我们就可以用此方法给控件设置图片资源
     *
     * @param viewId
     * @param resId
     * @param isFillet 是否圆角
     * @return
     */
    public ViewHolder setImageResourceC(int viewId, int resId, boolean isFillet,int tag) {
        ImageView imageView = getView(viewId);
        if (isFillet) {
            Glide.with(mContext).load(resId).placeholder(R.drawable.common_details_carousel_placeholder).transform(new GlideRoundTransform(mContext,tag)).into(imageView);
        } else {
            Glide.with(mContext).load(resId).placeholder(R.drawable.common_details_carousel_placeholder).into(imageView);
        }
        return this;
    }

    /**
     * 如果item中有ImageView控件的话，我们就可以用此方法给控件设置图片资源
     *
     * @param viewId
     * @param resId
     * @param isRound 是否圆角
     * @return
     */
    public ViewHolder setImageResourceG(int viewId, int resId, boolean isRound) {
        ImageView imageView = getView(viewId);
        if (isRound) {
            Glide.with(mContext).load(resId).placeholder(R.drawable.common_details_carousel_placeholder).transform(new CircleTransform(mContext)).into(imageView);
        } else {
            Glide.with(mContext).load(resId).placeholder(R.drawable.common_details_carousel_placeholder).into(imageView);
        }
        return this;
    }



    public ViewHolder setCheckBox(int viewId, boolean c, boolean isShow) {
        CheckBox mCheckBox = getView(viewId);
        if (isShow == true) {
            mCheckBox.setVisibility(View.VISIBLE);
        } else {
            mCheckBox.setVisibility(View.GONE);
        }
        mCheckBox.setChecked(c);
        return this;
    }
    //我们还可以添加其他的更多的方法。。。。。。。。。。。



}
