package com.samluys.filtertab.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.samluys.filtertab.R;
import com.samluys.filtertab.Utils;
import com.samluys.filtertab.listener.OnFilterToViewListener;

import java.util.ArrayList;
import java.util.List;


public abstract class BasePopupWindow extends PopupWindow {

    private final static int ANIMATION_TIME = 350;
    /**
     * 筛选View
     */
    private View mRootView;
    /**
     * 透明背景
     */
    private FrameLayout mFrameLayout;
    /**
     * 上下文
     */
    public Context mContext;
    public Activity mActivity;
    /**
     * 筛选类型
     */
    private int filterType;
    /**
     * popupwindow位置信息
     */
    private int mPosition;
    /**
     * 数据集合
     */
    private List<BaseFilterBean> mData = new ArrayList<>();
    private float sNonCompatDensity;
    private float sNonCompatScaleDensity;

    private OnFilterToViewListener onFilterToViewListener;

    public BasePopupWindow(Context context) {
        super(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param context
     * @param data
     * @param filterType 筛选样式
     */
    public BasePopupWindow(Context context, List data, int filterType, int position, OnFilterToViewListener onFilterToViewListener) {
        this.mContext = context;
        this.filterType = filterType;
        this.mData = data;
        this.mPosition = position;
        this.onFilterToViewListener = onFilterToViewListener;
        mActivity = (Activity) context;
        mRootView = initView();
        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFrameLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_00000050));
        mFrameLayout.setFocusable(true);
        mFrameLayout.setFocusableInTouchMode(true);
//        setCustomDensity(mActivity,((Activity) context).getApplication());
        mFrameLayout.addView(mRootView);
        setContentView(mFrameLayout);
        initCommonContentView();
        initSelectData();
    }


    /**
     * 布局配置
     */
    protected void initCommonContentView() {

        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        this.setTouchable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable());
        this.setAnimationStyle(0);
    }

    /**
     * 布局绑定,id绑定
     * 需要子类实现
     */
    public abstract View initView();

    /**
     * 初始化SelectData
     * 需要子类实现
     */
    public abstract void initSelectData();


    /**
     * 如果有需要,子类会重写该方法,
     *
     * @param anchor
     */
    public void show(View anchor) {
        showAsDropDown(anchor);
    }


    /**
     * 适配Android7.0
     *
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {

        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        if (Build.VERSION.SDK_INT >= 25) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int mHeight = Utils.getScreenHeight(mContext) - rect.bottom;
            setHeight(mHeight);
            super.showAsDropDown(anchor);
        } else if (Build.VERSION.SDK_INT == 24) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            int heignt = anchor.getHeight();
            this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, a[1] + heignt);
            this.update();
        } else {
            super.showAsDropDown(anchor);
        }

        int height = 0;
        if (mRootView.getMeasuredHeight() == 0) {
            height = getHeight();
        } else {
            height = mRootView.getMeasuredHeight();
        }

        createInAnimation(mContext, -height);
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            BasePopupWindow.super.dismiss();
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public View getRootView() {
        return mRootView;
    }

    public void setRootView(View mRootView) {
        this.mRootView = mRootView;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public List<BaseFilterBean> getData() {
        return mData;
    }

    public void setData(List<BaseFilterBean> data) {
        this.mData = data;
    }

    /**
     * 进入动画
     *
     * @param context
     * @param fromYDelta
     * @return
     */
    private void createInAnimation(Context context, int fromYDelta) {
        try {
            AnimationSet set = new AnimationSet(context, null);
            set.setFillAfter(true);
            TranslateAnimation animation = new TranslateAnimation(0, 0, fromYDelta, 0);
            animation.setDuration(ANIMATION_TIME);
            set.addAnimation(animation);

            if (mFrameLayout != null) {
                mRootView.setAnimation(set);
                AnimationSet setAlpha = new AnimationSet(mContext, null);
                setAlpha.setFillAfter(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(ANIMATION_TIME);
                setAlpha.addAnimation(alphaAnimation);
                mFrameLayout.startAnimation(setAlpha);
            } else {
                mRootView.startAnimation(set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出动画
     *
     * @param context
     * @param toYDelta
     * @return
     */
    private void createOutAnimation(Context context, int toYDelta) {
        try {
            AnimationSet set = new AnimationSet(context, null);
            set.setFillAfter(true);
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, toYDelta);
            animation.setDuration(ANIMATION_TIME);
            set.addAnimation(animation);
            mRootView.setAnimation(set);
            if (mFrameLayout != null) {
                mRootView.setAnimation(set);
                AnimationSet alphaSet = new AnimationSet(mContext, null);
                alphaSet.setFillAfter(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(ANIMATION_TIME);
                alphaSet.addAnimation(alphaAnimation);
                mFrameLayout.startAnimation(alphaSet);
            } else {
                mRootView.startAnimation(set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OnFilterToViewListener getOnFilterToViewListener() {
        return onFilterToViewListener;
    }

    public void setOnFilterToViewListener(OnFilterToViewListener onFilterToViewListener) {
        this.onFilterToViewListener = onFilterToViewListener;
    }

    public int getPosition() {
        return mPosition;
    }
}
