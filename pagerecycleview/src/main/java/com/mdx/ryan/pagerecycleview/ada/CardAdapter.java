package com.mdx.ryan.pagerecycleview.ada;

import android.content.Context;

import java.util.List;

/**
 * Created by ryan on 2016/4/23.
 */
public class CardAdapter extends MAdapter {

    public CardAdapter(Context context) {
        super(context);
    }

    public CardAdapter(Context context, Card[] list) {
        super(context, list);
    }

    public CardAdapter(Context context, List<Card> list) {
        super(context, list);
    }
}
