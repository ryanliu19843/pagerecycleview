package com.mdx.ryan.pagerecycleview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mdx.ryan.pagerecycleview.MRecyclerView;
import com.mdx.ryan.pagerecycleview.R;
import com.mdx.ryan.pagerecycleview.easeinterpolator.Ease;
import com.mdx.ryan.pagerecycleview.easeinterpolator.EasingInterpolator;


/**
 * Created by ryan on 2016/4/27.
 */
public class SwipRefreshView extends RelativeLayout {
    public static final int SRV_STATE_PULL = 0;  //继续下拉
    public static final int SRV_STATE_RELR = 1;  //松开刷新
    public static final int SRV_STATE_REFI = 2;  //正在刷新
    public static final int SRV_STATE_REFE = 3;  //刷新完成

    private ObjectAnimator animator, animatorend, animatorpull;
    public MRecyclerView mRecyclerView;
    private float om = 0, smv = 0;
    private float minload = 300;
    private int state = SRV_STATE_PULL;
    public float overspeed = 0.5f, overslowS = 1f;
    private SwipRefresh swipRefresh;
    private int swipPadding = 0;

    public SwipRefreshView(Context context) {
        super(context);
        init(context);
    }

    public SwipRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(LayoutParams.MATCH_PARENT, 1 + swipPadding);
        lp.setFullSpan(true);
        this.setLayoutParams(lp);
        this.setPadding(0, swipPadding, 0, 0);
        minload = context.getResources().getDimension(R.dimen.sp_over_pull);
    }

    public void setSwipPadding(int padding) {
        this.swipPadding = padding;
        this.setPadding(0, padding, 0, 0);
        if (mRecyclerView.getOrientation() == LinearLayoutManager.VERTICAL) {
            setH(0, mRecyclerView.mt);
        } else {
            setW(0, mRecyclerView.mt);
        }
    }

    public void setSwipRefresh(SwipRefresh swipRefresh) {
        this.swipRefresh = swipRefresh;
        if (swipRefresh instanceof View) {
            this.swipRefresh.setOver(this.minload);
            this.addView((View) swipRefresh, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 2000));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipRefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(1, 1);
        lp.setFullSpan(true);
        this.setLayoutParams(lp);
    }

    public void setPull(float mv) {
        if (getMinload() * overslowS < mv) {
            mv = getMinload() * overslowS + (mv - getMinload() * overslowS) * overspeed;
        }
        if (mRecyclerView.getOrientation() == LinearLayoutManager.VERTICAL) {
            setH(mv, mRecyclerView.mt);
        } else {
            setW(mv, mRecyclerView.mt);
        }
        if (state == SRV_STATE_PULL || state == SRV_STATE_RELR) {
            if (getMinload() <= mv) {
                setState(SRV_STATE_RELR);
            } else {
                setState(SRV_STATE_PULL);
            }
        }
    }

    public void setW(float mv, float mt) {
        if(mRecyclerView.onPullListener!=null){
            mRecyclerView.onPullListener.onPullListener(mv,mt,LinearLayoutManager.HORIZONTAL,this.minload);
        }
        if (swipRefresh != null) {
            swipRefresh.setW(mv, mt);
        }
        if (this.getLayoutParams() != null) {
            this.getLayoutParams().width = (((int) mv) == 0 ? 1 : ((int) mv)) + swipPadding;
        }
    }

    public void setH(float mv, float mt) {
        if(mRecyclerView.onPullListener!=null){
            mRecyclerView.onPullListener.onPullListener(mv,mt,LinearLayoutManager.VERTICAL,this.minload);
        }
        if (swipRefresh != null) {
            swipRefresh.setH(mv, mt);
        }
        if (this.getLayoutParams() != null) {
            this.getLayoutParams().height = (((int) mv) == 0 ? 1 : ((int) mv)) + swipPadding;
        }
    }


    private void setState(int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (swipRefresh != null) {
            swipRefresh.setState(state);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void pullReload() {
        if (getState() != SRV_STATE_PULL) {
            return;
        }
        swipRefresh.setLinw(0);
        om = (getMinload() - mRecyclerView.mv) / 100;
        smv = mRecyclerView.mv;
        setState(SRV_STATE_REFI);
        if (animatorpull == null) {
            animatorpull = ObjectAnimator.ofInt(this, "ss", 0, 100);
            animatorpull.setDuration(300);
            animatorpull.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int x = (int) animation.getAnimatedValue();
                    mRecyclerView.mv = smv + om * x;
                    if (x == 100) {
                        mRecyclerView.mv = getMinload();
                        mRecyclerView.startLoad(1);
                    }
                    setPull(mRecyclerView.mv);
                    mRecyclerView.scrollToPosition(0);
                    requestLayout();
                }
            });
        }
        animatorpull.start();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setloadend(float mv) {
        om = mv / 100;
        smv = mv;
        setState(SRV_STATE_REFE);
        if (animatorend == null) {
            animatorend = ObjectAnimator.ofInt(this, "ss", 0, 100);
            animatorend.setDuration(300);
//            animatorend.setInterpolator(new EasingInterpolator(Ease.BACK_IN));
            animatorend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int x = (int) animation.getAnimatedValue();
                    mRecyclerView.mv = smv - om * x;
                    setPull(mRecyclerView.mv);
                    requestLayout();
                    if (x == 100) {
                        mRecyclerView.mv = 0;
                        setPull(mRecyclerView.mv);
                        requestLayout();
                        setState(SRV_STATE_PULL);
                    }
                }
            });
        }
        if (mv > 0) {
            animatorend.start();
        } else {
            setState(SRV_STATE_PULL);
        }
    }

    public float getMinload() {
        return minload;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setRelease(float mv) {
        if (getMinload() <= mv && this.state != SRV_STATE_REFI) {
            om = (mv - getMinload()) / 100;
            setState(SRV_STATE_REFI);
        } else if (this.state == SRV_STATE_REFI) {
            om = (mv - getMinload()) / 100;
        } else {
            om = mv / 100;
        }

        smv = mv;
        if (animator == null) {
            animator = ObjectAnimator.ofInt(this, "ss", 0, 100);
            animator.setDuration(450);
            animator.setInterpolator(new EasingInterpolator(Ease.BOUNCE_OUT));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int x = (int) animation.getAnimatedValue();
                    mRecyclerView.mv = smv - om * x;
                    setPull(mRecyclerView.mv);
                    requestLayout();
                    if (x == 100) {
                        if (state == SRV_STATE_REFI) {
                            mRecyclerView.startLoad(1);
                            mRecyclerView.mv = getMinload();
                        } else {
                            mRecyclerView.mv = 0;
                            setPull(mRecyclerView.mv);
                            requestLayout();
                        }
                    }
                }
            });
        }
        if (mv > 0) {
            animator.start();
        }
    }


    public void setTouch(float mv) {
        if (animator != null) {
            animator.cancel();
        }
    }

    public int getState() {
        return state;
    }
}
