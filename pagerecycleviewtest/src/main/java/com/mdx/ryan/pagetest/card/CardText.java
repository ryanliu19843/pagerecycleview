//
//  CardText
//
//  Created by ryan on 2017-05-04 12:57:55
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.mdx.ryan.pagetest.card;

import android.support.v7.widget.RecyclerView;

import com.mdx.ryan.pagerecycleview.ada.Card;
import com.mdx.ryan.pagetest.item.Text;

public class CardText extends Card<String> {
	
	public CardText(String item) {
	    this.setItem(item);
    	this.type = com.mdx.ryan.pagetest.R.string.id_text;
    }

     @Override
     public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Text item = (Text) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
     }
}


