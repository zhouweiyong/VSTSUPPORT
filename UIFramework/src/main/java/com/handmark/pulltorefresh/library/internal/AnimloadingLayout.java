package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.vstecs.android.uiframework.R;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/5/19
 * class description:请输入类描述
 */
public class AnimloadingLayout extends LoadingLayout {
    private AnimationDrawable animationDrawable;

    public AnimloadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.pull_refresh_anim;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {
        animationDrawable.stop();
        mHeaderImage.clearAnimation();
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {

    }
}
