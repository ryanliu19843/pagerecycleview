//
//  BaseItem
//
//  Created by ryan on 2017-05-04 12:57:55
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.mdx.ryan.pagetest.item;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.mdx.ryan.pagerecycleview.viewhold.MViewHold;
import com.mdx.ryan.pagerecycleview.viewhold.ViewHodeParam;

public class BaseItem extends MViewHold implements OnClickListener {
	protected Context context;
	protected View contentview;

    public BaseItem(View itemView){
        super(itemView);
    }

    public BaseItem(View itemView,ViewHodeParam viewHodeParm){
        super(itemView,viewHodeParm);
    }


	@Override
	public void onClick(View v) {

	}

    public View findViewById(int id) {
        return this.itemView.findViewById(id);
    }

}

