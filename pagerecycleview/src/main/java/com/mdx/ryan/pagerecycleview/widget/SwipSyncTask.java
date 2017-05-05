package com.mdx.ryan.pagerecycleview.widget;

import com.mdx.ryan.pagerecycleview.ada.CardAdapter;

/**
 * Created by ryan on 2017/5/4.
 */

public abstract class SwipSyncTask {

    public OnSyncPageSwipListener onPageSwipListener;
    public boolean haspage;
    public String error;

    public void setOnPageSwipListener(OnSyncPageSwipListener onPageSwipListener) {
        this.onPageSwipListener = onPageSwipListener;
    }


    protected abstract CardAdapter doInBackground(Object... params);

    protected void onPostExecute(CardAdapter result) {
        if (onPageSwipListener != null) {
            this.onPageSwipListener.setAdapter(result, haspage, error);
        }
    }


    protected void onPreExecute() {
        error = null;
        haspage = false;
    }


    public abstract void intermit();
}
