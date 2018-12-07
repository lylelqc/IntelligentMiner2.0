package com.sly.app.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.CropOptions;
import com.sly.app.R;
import com.sly.app.utils.PhotoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/1.
 */

public class PhotoDialog extends Dialog {

    Context mContext;
    ViewHolder viewHolder;
    TakePhoto mTakePhoto;
    boolean isCrop;
    int limit = 1;
    int cropX = 3;
    int cropY = 2;

    public PhotoDialog(Context context) {
        this(context,null);
    }

    public PhotoDialog(Context context, TakePhoto takePhoto) {
        this(context,takePhoto,false);
    }

    public PhotoDialog(Context context, TakePhoto takePhoto, boolean isCrop) {
        super(context, R.style.MaterialDialogSheet);
        mContext = context;
        mTakePhoto = takePhoto;
        this.isCrop = isCrop;
        init();
    }

    public PhotoDialog(Context context, TakePhoto takePhoto, boolean isCrop, int cropX, int cropY) {
        super(context, R.style.MaterialDialogSheet);
        mContext = context;
        mTakePhoto = takePhoto;
        this.isCrop = isCrop;
        this.cropX = cropY;
        this.cropY = cropY;
        init();
    }



    public void init() {
        viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_photo, null));
        setContentView(viewHolder.mLayout);
        setCancelable(false);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    public void setChooselimit(int limit){
        this.limit = limit;
    }

    class ViewHolder {

        View mLayout;
        @BindView(R.id.canera_btn)
        View caneraBtn;
        @BindView(R.id.photo_btn)
        View photoBtn;
        @BindView(R.id.cancel_btn)
        View cancelBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mLayout = view;
        }

        @OnClick({R.id.cancel_btn, R.id.canera_btn, R.id.photo_btn})
        public void clcik(View v){
            switch (v.getId()){
                case R.id.photo_btn:
                    if(mTakePhoto != null){
                        if(isCrop){
                            CropOptions cropOptions = new CropOptions.Builder().setAspectX(cropX).setAspectY(cropY).setWithOwnCrop(true).create();
                            mTakePhoto.onPickMultipleWithCrop(limit,cropOptions);
                        }else{
                            mTakePhoto.onPickMultiple(limit);
                        }
                    }
                    PhotoDialog.this.dismiss();
                    break;
                case R.id.canera_btn:
                    if(mTakePhoto != null){
                        if(isCrop){
                            CropOptions cropOptions = new CropOptions.Builder().setAspectX(cropX).setAspectY(cropY).setWithOwnCrop(true).create();
                            mTakePhoto.onPickFromCaptureWithCrop(PhotoUtils.getPhotoFilePath(mContext),cropOptions);
                        }else{
                            mTakePhoto.onPickFromCapture(PhotoUtils.getPhotoFilePath(mContext));
                        }
                    }
                    PhotoDialog.this.dismiss();
                    break;
                case R.id.cancel_btn:
                    PhotoDialog.this.dismiss();
                    break;
            }
        }

    }


}
