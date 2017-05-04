package com.mdx.ryan.pagerecycleview.widget;

import android.content.Context;

import com.mdx.ryan.pagerecycleview.MRecyclerView;


/**
 * Created by ryan on 2016/6/21.
 */
public abstract class OnSwipLoadListener {
    public MRecyclerView recyclerView;   //recyclerview
    public MRecyclerView.OnDataLoaded onDataLoaded;  //接口数据加载成功
    public Context context;   //上下文
    protected boolean havepage = false;   //是否有分页

    public abstract void onSwipLoad(int type);


    public abstract void onUpdateLoad(int type);



    public abstract void onPageLoad();


    public void onDataState(int state,int error, String msg) {

    }

    public void onSwipStateChange(int state, float mv, float mt) {

    }


    public void setRecyclerView(MRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    public void setOnDataLoaded(MRecyclerView.OnDataLoaded onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    public abstract void clear();

    public void setHavepage(boolean havepage) {
        this.havepage = havepage;
    }
}
