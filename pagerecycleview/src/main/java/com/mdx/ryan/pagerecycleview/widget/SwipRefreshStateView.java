package com.mdx.ryan.pagerecycleview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mdx.ryan.pagerecycleview.MRecyclerView;

import java.util.HashMap;


/**
 * Created by ryan on 2016/6/4.
 */
public class SwipRefreshStateView extends FrameLayout implements SwipRefreshState {
    public MRecyclerView mRecyclerView;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private View nowView;
    private LoadingFace loadingFace = new SimpleLoadingFace();
    private int state, error;
    private String msg;


    public SwipRefreshStateView(Context context) {
        super(context);
        init(context);

    }

    public SwipRefreshStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SwipRefreshStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipRefreshStateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void init(Context context) {
    }

    public View setRes(int res) {
        View view = null;
        if (viewMap.containsKey(res)) {
            view = viewMap.get(res);
        }
        if (nowView != null && nowView == view) {
            return view;
        }
        this.removeAllViewsInLayout();
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (view != null) {
            view = viewMap.get(res);
            this.addView(view, lp);
        } else {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(res, null);
            viewMap.put(res, view);
            this.addView(view, lp);
        }
        return view;
    }

    public void setState(int state, int error, String msg) {
        this.state = state;
        this.error = error;
        this.msg = msg;
        if (this.loadingFace != null) {
            int res = loadingFace.getViewRes(state, error, msg);
            if (res != 0) {
                View view = setRes(res);
                loadingFace.setValue(view, state, error, msg);
            }
        }
    }

    public void setLoadingFace(LoadingFace loadingFace) {
        this.loadingFace = loadingFace;
        this.viewMap.clear();
        this.removeAllViews();
        setState(this.state, this.error, this.msg);
    }


    public interface LoadingFace {
        int getViewRes(int state, int error, String msg);

        void setValue(View view, int state, int error, String msg);
    }
}
