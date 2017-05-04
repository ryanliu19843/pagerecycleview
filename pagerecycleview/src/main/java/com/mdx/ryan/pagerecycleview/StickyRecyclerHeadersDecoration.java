package com.mdx.ryan.pagerecycleview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mdx.ryan.pagerecycleview.viewhold.MViewHold;
import com.mdx.ryan.pagerecycleview.viewhold.ViewHodeParam;


public class StickyRecyclerHeadersDecoration extends RecyclerView.ItemDecoration {
    private Rect rect = new Rect(0, 0, 0, 0);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        ViewHodeParam vhp = null;
        if (viewHolder instanceof MViewHold) {
            vhp = ((MViewHold) viewHolder).viewHodeParam;
        }
        if (vhp != null) {
            if (vhp.frameType == 2) {
                outRect.set(0, 0, 0, 0);
                return;
            } else {
                Rect rect = vhp.rect;
                if (rect != null) {
                    outRect.set(rect);
                    return;
                }
            }
        }
        outRect.set(rect);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        MFRecyclerView mfRecyclerView = null;
        if (parent.getParent() instanceof MFRecyclerView) {
            mfRecyclerView = (MFRecyclerView) parent.getParent();
        }
        if (mfRecyclerView != null) {
            mfRecyclerView.setDisTouch(parent, state);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }


    public void setDecoration(int left,int top,int right,int bottom){
        rect.set(left,top,right,bottom);
    }
}
