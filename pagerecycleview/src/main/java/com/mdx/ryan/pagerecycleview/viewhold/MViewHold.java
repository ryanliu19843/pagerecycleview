package com.mdx.ryan.pagerecycleview.viewhold;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mdx.ryan.pagerecycleview.ada.Card;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_Y;


/**
 * Created by ryan on 2016/4/23.
 */
public class MViewHold extends RecyclerView.ViewHolder implements HasVHparam {

    public ViewHodeParam viewHodeParam;
    public Card card;
    public int id;
    public int posion;
    public boolean isAnima=false,alwaysshowanima=false;
    public float x = 0, y = 0, t = 0, l = 0;

    public MViewHold(View itemView) {
        super(itemView);
    }

    public MViewHold(View itemView, ViewHodeParam viewHodeParam) {
        super(itemView);
        this.viewHodeParam = viewHodeParam;
    }


    public void setXY(float l, float t) {
        this.t = t;
        this.l = l;
    }

    public void preAnimateAddImpl() {
    }




    public Animator[] getOverAnimators(int first,int last,int posion) {
        View view=this.itemView;
        if(first>posion){
            return new Animator[]{
//                    ObjectAnimator.ofFloat(view, TRANSLATION_X, 0 - 500, 0),
                    ObjectAnimator.ofFloat(view, SCALE_Y, 0.88f, 1f),
                    ObjectAnimator.ofFloat(view, SCALE_X, 0.88f, 1f)
            };
        }
        return new Animator[]{
//                ObjectAnimator.ofFloat(view, TRANSLATION_X, 0 - 500, 0),
                ObjectAnimator.ofFloat(view, SCALE_Y, 0.88f, 1f),
                ObjectAnimator.ofFloat(view, SCALE_X, 0.88f, 1f)
        };
    }

    public Animator[] getAnimators(int first,int last,int posion) {
        View view=this.itemView;
        if(first>posion){
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, TRANSLATION_Y, -200, 0),
                    ObjectAnimator.ofFloat(view, SCALE_Y, 0.88f, 1f),
                    ObjectAnimator.ofFloat(view, SCALE_X, 0.88f, 1f)
            };
        }
        return new Animator[]{
                ObjectAnimator.ofFloat(view, TRANSLATION_Y, 200, 0),
                ObjectAnimator.ofFloat(view, SCALE_Y, 0.88f, 1f),
                ObjectAnimator.ofFloat(view, SCALE_X, 0.88f, 1f)
        };
    }

    public void animateShow() {
        if(isAnima ){
            return;
        }
        if (card!=null && !card.reanimation) {
            isAnima = true;
        }
    }

    public void preAnimateRemoveImpl() {
    }

    public void animateAddImpl(ViewPropertyAnimatorListener listener) {

    }

    public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {

    }

    @Override
    public ViewHodeParam getViewHodeParam() {
        return viewHodeParam;
    }
}
