package com.mdx.ryan.pagerecycleview.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdx.ryan.pagerecycleview.R;


/**
 * Created by ryan on 2016/8/12.
 */
public class SimpleLoadingFace implements SwipRefreshStateView.LoadingFace {
    @Override
    public int getViewRes(int state, int error, String msg) {

        if (state == 1) {
            return R.layout.sr_loading_state_a;
        } else if (state == 3) {
            return R.layout.sr_loading_state_b;
        }
        return 0;
    }

    @Override
    public void setValue(View view, int state, int error, String msg) {
        if (state == 1) {
        } else if (state == 3) {
            ImageView img = (ImageView) view.findViewById(R.id.image);
            TextView tv = (TextView) view.findViewById(R.id.text);
            if (error == 0) {
                img.setImageResource(R.mipmap.sr_ic_nodata_n);
                tv.setText(R.string.page_nodate);
            } else {
                img.setImageResource(R.mipmap.sr_ic_error_n);
                tv.setText(msg + "," + tv.getResources().getString(R.string.click_reload));
            }
        }
    }
}
