//
//  
//
//  Created by ryan on 2016-04-23 16:25:05
//  Copyright (c) ryan All rights reserved.


/**

 */

package com.mdx.ryan.pagerecycleview.ada;


import android.content.Context;
import android.view.ViewGroup;

import com.mdx.ryan.pagerecycleview.commons.MLog;
import com.mdx.ryan.pagerecycleview.viewhold.MViewHold;

import java.lang.reflect.Method;


public class CardIDS {
    public static MViewHold CreateViewHolde(int cardType, Context context, ViewGroup parent) {
        String card = context.getString(cardType);
        try {
            Class<?> classType = Class.forName(card);
            Method method = classType.getMethod("getView", Context.class, ViewGroup.class);
            return (MViewHold) method.invoke(null, context, parent);
        } catch (Exception e) {
            MLog.D(e);
        }
        return null;
    }
}
