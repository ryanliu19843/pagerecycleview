/*
 * 文件名: Card.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014年6月27日
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.ryan.pagerecycleview.ada;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.mdx.ryan.pagerecycleview.viewhold.HasVHparam;
import com.mdx.ryan.pagerecycleview.viewhold.MViewHold;
import com.mdx.ryan.pagerecycleview.viewhold.ViewHodeParam;


/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014年6月27日 下午2:00:07]
 */
public abstract class Card<T> implements HasVHparam {
    protected int type = 0, span = 1;
    protected MAdapter mAdapter;
    public ViewHodeParam viewHodeParam;
    public BaseAdapter oAdapter;
    public boolean isanimation = false;
    public boolean useanimation = true;
    public boolean reanimation = false;
    public MViewHold overViewHold;
    public RecyclerView.ViewHolder viewHold;
    public int visibility = View.VISIBLE;
    public int state = 0;
    public T item, lastitem;

    public void setItem(T item) {
        this.lastitem = this.item;
        this.item = item;
    }

    public MAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(MAdapter adapter) {
        this.mAdapter = adapter;
    }

    public int getSpan() {
        return span <= 1 ? 1 : span;
    }


    public int getCardType() {
//        if (viewHodeParam != null && viewHodeParam.showType == 1) {
//            return -88;
//        }
        return type;
    }

    public void dispbind(RecyclerView.ViewHolder viewHolder, int posion) {
        if (viewHolder instanceof MViewHold) {
            ((MViewHold) viewHolder).viewHodeParam = viewHodeParam;
            ((MViewHold) viewHolder).card = this;
            ((MViewHold) viewHolder).posion = posion;
            ((MViewHold) viewHolder).card.isanimation = isanimation;
            if (viewHodeParam != null && viewHodeParam.showType == 1) {
                bindOver(posion);
            }
        }
        if (viewHodeParam != null && viewHodeParam.showType == 1) {
            viewHolder.itemView.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.itemView.setVisibility(this.visibility);
        }
        bind(viewHolder, posion);
    }

    public int getShowType() {
        if (viewHodeParam != null) {
            return viewHodeParam.showType;
        }
        return 0;
    }

    public void bindOver(int posion) {
        if (overViewHold == null) {
            overViewHold = CardIDS.CreateViewHolde(type, mAdapter.getContext(), mAdapter.recyclerView);
            overViewHold.viewHodeParam = viewHodeParam;
            overViewHold.card = this;
            overViewHold.posion = posion;
            bind(overViewHold, posion);
            if (overViewHold.itemView.getLayoutParams() == null) {
                overViewHold.itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(overViewHold.itemView.getLayoutParams().width, overViewHold.itemView.getLayoutParams().height);
                overViewHold.itemView.setLayoutParams(layoutParams);
            }
        }
    }

    public abstract void bind(RecyclerView.ViewHolder viewHolder, int posion);

    public Card setShowType(int type) {
        if (viewHodeParam == null) {
            viewHodeParam = new ViewHodeParam();
        }
        this.useanimation=false;
        viewHodeParam.showType = type;
        return this;
    }


    @Override
    public ViewHodeParam getViewHodeParam() {
        return viewHodeParam;
    }
}