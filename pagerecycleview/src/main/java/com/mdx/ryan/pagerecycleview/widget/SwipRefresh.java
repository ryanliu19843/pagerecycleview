package com.mdx.ryan.pagerecycleview.widget;

import android.animation.ObjectAnimator;

/**
 * Created by ryan on 2016/5/25.
 */
public interface SwipRefresh {
    /**
     * 设置当前高度
     * @param mv  当前高度
     * @param mt
     */
    void setH(float mv, float mt);

    /**
     * 设置当前宽度
     * @param mv  位移宽度
     * @param mt  手指位置
     */
    void setW(float mv, float mt);

    /**
     * 设置当前状态
     * @param state
     */
    void setState(int state);

    /**
     * 设置溢出高度
     * @param over
     */
    void setOver(float over);

    /**
     *
     */
    void startPullload();


    /**
     * 设置下拉动画，当自动下拉刷新是播放
     * @return
     */
    ObjectAnimator getPullAnimator();

    /**
     * 设置加载结束动画，当加载结束是播放的动画
     * @return
     */
    ObjectAnimator getEndAnimator();


    /**
     * 设置释放动画 当下拉释放后执行的动画
     * @return
     */
    ObjectAnimator getAnimator();
}
