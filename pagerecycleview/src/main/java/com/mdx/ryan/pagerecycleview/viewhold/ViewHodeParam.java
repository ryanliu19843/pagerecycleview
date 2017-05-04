package com.mdx.ryan.pagerecycleview.viewhold;

import android.graphics.Rect;

/**
 * Created by ryan on 2016/6/30.
 */
public class ViewHodeParam {

    public int frameType = 0;

    public int showType = 0;

    public Rect rect;

    public ViewHodeParam(int frameType, Rect rect) {
        this.frameType = frameType;
        this.rect = rect;
    }


    public ViewHodeParam() {
    }
}
