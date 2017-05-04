//
//  CardGroup
//
//  Created by ryan on 2017-05-04 13:56:11
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.mdx.ryan.pagetest.card;

import android.support.v7.widget.RecyclerView;

import com.mdx.ryan.pagerecycleview.ada.Card;
import com.mdx.ryan.pagetest.item.Group;

public class CardGroup extends Card<String> {
	
	public CardGroup(String item) {
	    this.setItem(item);
    	this.type = com.mdx.ryan.pagetest.R.string.id_group;
    }

     @Override
     public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Group item = (Group) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
     }
    
    

}


