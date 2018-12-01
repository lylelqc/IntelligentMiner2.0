/**
 * ****************************** Copyright (c)*********************************\
 * *
 * *                 (c) Copyright 2017, DevKit.vip, china, qd. sd
 * *                          All Rights Reserved
 * *
 * *                           By(K)
 * *
 * *-----------------------------------版本信息------------------------------------
 * * 版    本: V0.1
 * *
 * *------------------------------------------------------------------------------
 * *******************************End of Head************************************\
 */
package com.sly.app.activity.loading;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sly.app.R;
import com.sly.app.activity.MainActivity;
import com.sly.app.activity.mine.LoginActivity;
import com.sly.app.adapter.GuidePageAdapter;
import com.sly.app.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文 件 名: LoadingActivity
 * 创 建 人: By k
 * 创建日期: 2017/11/14 14:16
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注：
 */
public class LoadingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    //本地图片数据（资源文件）
    List<Integer> list = new ArrayList<>();
    @BindView(R.id.guide_vp)
    ViewPager guideVp;
    @BindView(R.id.guide_iv)
    ImageView guideIv;
    private List<View> viewList;//图片资源的集合
    private int FLAG = 0;
    @Override
    protected void initView() {
        setBannerView(list);
    }

    @Override
    protected void initData() {
        list.add(R.drawable.img_guide_1);
        list.add(R.drawable.img_guide_2);
//        list.add(R.drawable.img_guide_3);
//        list.add(R.drawable.img_guide_4);
        initViewPager();
    }
    @Override
    public boolean isCheckNetState() {
        return  false;
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    protected void setListener() {
//        mBannerView.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                if (position == list.size() - 1) {
//                    startActivityWithoutExtras(MainActivity.class);
//                }
//            }
//        });
//
//        mBannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Logcat.i("===================================" + position);
//                if (position == list.size() - 1) {
//                    mBannerView.stopAutoPlay();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }

    private void setBannerView(List<Integer> images) {
//        mBannerView.setBannerStyle(BannerConfig.NOT_INDICATOR);
//        mBannerView.setImageLoader(new BannerGlideImageLoader());
//        mBannerView.setImages(images);
//        mBannerView.isAutoPlay(false);
//        mBannerView.start();
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        guideIv.setVisibility(View.GONE);
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //循环创建View并加入到集合中
        int len = list.size();
        for (int i = 0; i < len; i++) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(list.get(i));
            //将ImageView加入到集合中
            viewList.add(imageView);
        }

        //View集合初始化好后，设置Adapter
        guideVp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        guideVp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //判断是否是最后一页
        if (position == list.size() - 1) {
            guideIv.setVisibility(View.VISIBLE);
        }else{
            guideIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.guide_iv)
    public void onViewClicked() {
        startActivityWithoutExtras(LoginActivity.class);
        finish();
    }
}