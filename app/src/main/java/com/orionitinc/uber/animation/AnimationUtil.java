package com.orionitinc.uber.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;


public class AnimationUtil {
    public static  void animate( RecyclerView.ViewHolder holder , boolean goesDown){
        AnimatorSet animatorSet = new AnimatorSet ();

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(holder.itemView,"translationY",goesDown==true?200:-200,0);
        animatorX.setDuration(1000);

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(holder.itemView,"translationX",-50,50,-30,30,-20,20,-5,5,0);
        animatorY.setDuration(1000);

        animatorSet.playTogether(animatorX,animatorY);
        animatorSet.start();
    }
}
