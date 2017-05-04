package com.mdx.ryan.pagerecycleview.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.ryan.pagerecycleview.MRecyclerView;
import com.mdx.ryan.pagerecycleview.R;


/**
 * Created by ryan on 2016/6/4.
 */
public class SwipRefreshLoadingView extends LinearLayout implements View.OnClickListener {
    public MRecyclerView mRecyclerView;
    private ObjectAnimator animatorend;
    private ProgressBar progressBar;
    private TextView tv_loading;

    public SwipRefreshLoadingView(Context context) {
        super(context);
        init(context);

    }

    public SwipRefreshLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SwipRefreshLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipRefreshLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void init(Context context) {
        this.setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.page_list_footloadingview, this);
        StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setFullSpan(true);
        this.setLayoutParams(lp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
    }


    public void setState(int type, String msg) {
        if (type == 0) {
            progressBar.setVisibility(View.VISIBLE);
            tv_loading.setText("加载中... ");
            this.setOnClickListener(null);
        } else if (type == 1) {
            progressBar.setVisibility(View.GONE);
            tv_loading.setText("没有更多信息");
            this.setOnClickListener(null);
        } else if (type == 2) {
            progressBar.setVisibility(View.GONE);
            tv_loading.setText("加载失败,点击重试");
            this.setOnClickListener(this);
        }
    }



    @Override
    public void onClick(View view) {
        if (mRecyclerView.onSwipLoadListener != null) {
            mRecyclerView.onSwipLoadListener.onPageLoad();
        }
    }

}
