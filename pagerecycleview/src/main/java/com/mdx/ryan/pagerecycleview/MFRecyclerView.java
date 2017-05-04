package com.mdx.ryan.pagerecycleview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mdx.ryan.pagerecycleview.ada.Card;
import com.mdx.ryan.pagerecycleview.ada.CardAdapter;
import com.mdx.ryan.pagerecycleview.ada.MAdapter;
import com.mdx.ryan.pagerecycleview.viewhold.MViewHold;
import com.mdx.ryan.pagerecycleview.widget.OnSwipLoadListener;
import com.mdx.ryan.pagerecycleview.widget.SwipRefreshLoadingView;
import com.mdx.ryan.pagerecycleview.widget.SwipRefreshState;
import com.mdx.ryan.pagerecycleview.widget.SwipRefreshStateView;
import com.mdx.ryan.pagerecycleview.widget.SwipRefreshView;

import java.util.ArrayList;
import java.util.List;

import static com.mdx.ryan.pagerecycleview.animator.ViewAnimator.DEFAULT_ANIMATION_DURATION_MILLIS;


/**
 * Created by ryan on 2016/7/2.
 */
public class MFRecyclerView extends FrameLayout {
    private MRecyclerView mRecyclerView;
    private boolean mIsBeingDragged;
    private int mTouchSlop, mDis;
    private float mLastMotionY = 0;
    private List<Card> list = new ArrayList<>();
    private float offsetT = 0;
    private Card lcard = null;
    boolean isnedrset = false;
    private SwipRefreshStateView swipRefreshStateView;
    private int showtype = 0;
    private int state, error;
    private String msg;

    public MFRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public MFRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MFRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mRecyclerView = new MRecyclerView(context);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mRecyclerView, lp);

        this.swipRefreshStateView = new SwipRefreshStateView(context);
        this.addView(swipRefreshStateView, lp);
        swipRefreshStateView.setVisibility(View.INVISIBLE);
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
        if (showtype == 1 || showtype == 2) {
            if (state == 3) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
            }
        } else {
            setLoadingState(state, error, msg);
        }
    }

    public void setLoadingFace(SwipRefreshStateView.LoadingFace loadingFace) {
        swipRefreshStateView.setLoadingFace(loadingFace);
    }

    public void setLoadingState(int state, int error, String msg) {
        this.state = state;
        this.error = error;
        this.msg = msg;
        if (showtype == 0) {
            if (state == 0) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
            } else if (state == 1) {
                mRecyclerView.setCanPull(false);
                swipRefreshStateView.setOnClickListener(null);
                swipRefreshStateView.setVisibility(View.VISIBLE);
            } else if (state == 3) {
                mRecyclerView.setCanPull(false);
                swipRefreshStateView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRecyclerView.reload();
                    }
                });
                mRecyclerView.hidePage();
                swipRefreshStateView.setVisibility(View.VISIBLE);
            }
            this.swipRefreshStateView.setState(state, error, msg);
        } else if (showtype == 1) {
            if (state == 0) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
            } else if (state == 1) {
                mRecyclerView.setCanPull(false);
                swipRefreshStateView.setOnClickListener(null);
                swipRefreshStateView.setVisibility(View.VISIBLE);
            } else if (state == 3) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
                mRecyclerView.hidePage();
            }
            this.swipRefreshStateView.setState(state, error, msg);
        } else if (showtype == 2) {
            mRecyclerView.setCanPull(true);
            swipRefreshStateView.setVisibility(View.INVISIBLE);
            if (state == 3) {
                mRecyclerView.setCanPull(true);
                mRecyclerView.hidePage();
            }
        }
        if (error != 0) {
            mRecyclerView.swipRefreshLoadingView.setState(2, msg);
        }
    }

    public void setTouch(RecyclerView recyclerView, MViewHold mh, float l, float t, int w, int h, int posion) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (mh.itemView.getParent() == null) {
            this.addViewInLayout(mh.itemView, -1, new LayoutParams(w, h));
        }
        mh.itemView.setVisibility(View.VISIBLE);
        mh.itemView.setLayoutParams(new LayoutParams(w, h));
        mh.itemView.setY(t);
        mh.itemView.setX(l);
        mh.setXY(l, t);
        mh.y = t;
        mh.x = l;
        if (!mh.isAnima) {
            itemAnimshow(mh.getOverAnimators(0, 0, 0), mh.itemView);
            mh.isAnima = true;
        }
        if (lcard != null) {
            if (lcard != null && lcard.overViewHold != null && mh != lcard.overViewHold) {
                View v = lcard.overViewHold.itemView;
                if (v.getParent() == null) {
                    this.addViewInLayout(v, -1, new LayoutParams(v.getWidth(), v.getHeight()));
                }
                int mb = lcard.getViewHodeParam() != null ? (lcard.getViewHodeParam().rect != null ? lcard.getViewHodeParam().rect.bottom : 0) : 0;
                float b = v.getHeight() + (mb < 0 ? 0 : mb) + offsetT;
                float y = offsetT;
                if (b > t) {
                    float top = y - (b - t) - 1;
                    v.setY(top);
                    lcard.overViewHold.setXY(lcard.overViewHold.itemView.getX(), top);
                    isnedrset = false;
                    v.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void itemAnimshow(final Animator[] animators, final View view) {
        Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setDuration(DEFAULT_ANIMATION_DURATION_MILLIS);
        set.start();
    }

    public void setDisTouch(RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            if (!(v instanceof MRecyclerView) && !(v instanceof SwipRefreshState)) {
                v.setVisibility(View.GONE);
            }
        }
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int ind = parent.getChildAdapterPosition(child);
            if (ind >= 0) {
                RecyclerView.Adapter ada = parent.getAdapter();
                if (ada instanceof MAdapter) {
                    Object obj = ((MAdapter) ada).get(ind);
                    if (obj instanceof Card) {
                        Card c = (Card) obj;
                        if (c.getViewHodeParam() == null || c.getViewHodeParam().showType == 0) {
                            if (c.viewHold instanceof MViewHold) {
                                ((MViewHold) (c.viewHold)).setXY(c.viewHold.itemView.getLeft(), c.viewHold.itemView.getTop());
                            }
                        }
                    }
                }
            }
        }

        int lastshow = Integer.MAX_VALUE;
        int poind = 0;
        lcard = null;
        isnedrset = true;
        boolean isshowhead = false;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            int first = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            int last = ((LinearLayoutManager) parent.getLayoutManager()).findLastVisibleItemPosition();
            if (parent.getAdapter() instanceof MAdapter) {
                for (Card card : ((MAdapter) parent.getAdapter()).overCard) {
                    int po = ((MAdapter) parent.getAdapter()).getPosion(card);
                    if (po < first && lastshow > first - po) {
                        lastshow = first - po;
                        lcard = card;
                        poind = lastshow;
                    } else {
                        break;
                    }
                }
            }
        }
        boolean isneedskip = true;
        if (offsetT != 0) {
            float lastheight = 0;
            if (lcard != null) {
                lastheight = 0;
            }
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                int ind = parent.getChildAdapterPosition(child);
                if (ind >= 0) {
                    RecyclerView.Adapter ada = parent.getAdapter();
                    if (ada instanceof MAdapter) {
                        Object obj = ((MAdapter) ada).get(ind);
                        if (obj instanceof Card) {
                            Card c = (Card) obj;
                            if (c.getViewHodeParam() != null && c.getViewHodeParam().showType == 1) {
                                if (lcard == null || lcard.overViewHold.itemView != child) {
                                    if (c.overViewHold.itemView.getY() < lastheight) {
                                        isneedskip = false;
                                        break;
                                    }
                                    lastheight = c.overViewHold.itemView.getY() + c.overViewHold.itemView.getHeight();
                                }
                            }
                        }
                    }
                }
            }
        }


        int ox = 0, oy = 0;

        if (mRecyclerView.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) mRecyclerView.getLayoutParams();
            ox = layoutParams.leftMargin;
            oy = layoutParams.topMargin;
        }

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int ind = parent.getChildAdapterPosition(child);
            if (ind >= 0) {
                RecyclerView.Adapter ada = parent.getAdapter();
                if (ada instanceof MAdapter) {
                    Object obj = ((MAdapter) ada).get(ind);
                    if (obj instanceof Card) {
                        Card c = (Card) obj;
                        if (c.getViewHodeParam() != null && c.getViewHodeParam().showType == 1) {
                            MViewHold mh = c.overViewHold;
                            float y = child.getY() + oy;
                            if (child.getY() + oy < offsetT) {
                                y = offsetT;
                                isshowhead = true;
                                lcard = c;
                            }
                            int cardidn = parent.getChildAdapterPosition(child);
                            Object fc = ((MAdapter) parent.getAdapter()).get(cardidn);
                            if ((child.getY() + oy + child.getHeight()) < offsetT && !isneedskip) {
                                y = child.getY() + oy;
                            }
                            setTouch(parent, mh, child.getX() + ox, y, child.getWidth(), child.getHeight(), ind);
                        }
                    }
                }
            }
        }
        if (lastshow != Integer.MAX_VALUE && !isshowhead && isnedrset) {
            if (lcard != null && lcard.overViewHold != null) {
                View v = lcard.overViewHold.itemView;
                setTouch(this.getRecyclerView(), lcard.overViewHold, v.getX(), offsetT, v.getWidth(), v.getHeight(), poind);
            }
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            if (!(v instanceof MRecyclerView) && !(v instanceof SwipRefreshState)) {
                if (v.getVisibility() == View.GONE) {
                    v.clearAnimation();
                    this.removeViewInLayout(v);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsBeingDragged) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mIsBeingDragged = false;
            }
            return mRecyclerView.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        if (!mIsBeingDragged) {
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE: {

                    final float y = ev.getY(0);
                    final int xDiff = (int) Math.abs(y - mLastMotionY);

                    if (xDiff > mTouchSlop) {
                        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_CANCEL, ev.getX(), ev.getY(), ev.getMetaState()));
                        mIsBeingDragged = true;
                        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, ev.getX(), ev.getY(), ev.getMetaState()));
                    }
                    break;
                }

                case MotionEvent.ACTION_DOWN: {
                    final float y = ev.getY();
                    mLastMotionY = y;
                    break;
                }

                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
            }
        }
        if (mIsBeingDragged) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void reload() {
        mRecyclerView.reload();
    }

    public void pullLoad() {
        setLoadingState(0, 0, "");
        mRecyclerView.pullLoad();
    }

    public void update() {
        mRecyclerView.update();
    }

    public void setOnSwipLoadListener(OnSwipLoadListener onSwipLoadListener) {
        mRecyclerView.setOnSwipLoadListener(onSwipLoadListener);
    }

    public void setOnDataLoaded(MRecyclerView.OnDataLoaded onDataLoaded) {
        mRecyclerView.setOnDataLoaded(onDataLoaded);
    }

    public void setSwipRefreshView(SwipRefreshView swipRefreshView, int type) {
        mRecyclerView.setSwipRefreshView(swipRefreshView, type);
    }

    public void setSwipRefreshLoadingView(SwipRefreshLoadingView swipRefreshLoadingView, int type) {
        mRecyclerView.setSwipRefreshLoadingView(swipRefreshLoadingView, type);
    }

    public void hideFoot() {
        mRecyclerView.endPage();
    }

    public void showFoot() {
        mRecyclerView.showPage();
    }

    public RecyclerView.Adapter getMAdapter() {
        return mRecyclerView.getAdapter();
    }

    public View getHeadView(int ind) {
        return mRecyclerView.getHeadView(ind);
    }

    public View getFootView(int ind) {
        return mRecyclerView.getFootView(ind);
    }

    public void addHeadView(MViewHold viewHold, int ind) {
        mRecyclerView.addHeadView(viewHold, ind);
    }

    public void addFootView(MViewHold viewHold, int ind) {
        mRecyclerView.addFootView(viewHold, ind);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void addAdapter(CardAdapter cardAdapter) {
        mRecyclerView.addAdapter(cardAdapter);
    }

    public void clearAdapter() {
        mRecyclerView.clearAdapter();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }


    public void setOnPullListener(MRecyclerView.OnPullListener onPullListener) {
        mRecyclerView.setOnPullListener(onPullListener);
    }

    public MRecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    public void setSwipPadding(int swipPadding) {
        mRecyclerView.setSwipPadding(swipPadding);
        this.offsetT = swipPadding;
    }

    public void setLoadingPadding(int loadingPadding) {
        mRecyclerView.setLoadingPadding(loadingPadding);
    }

    public void setLoadingType(int type) {
        mRecyclerView.setLoadingType(type);
    }

    public void setDecoration(int left, int top, int right, int bottom) {
        mRecyclerView.setDecoration(left, top, right, bottom);
    }
}