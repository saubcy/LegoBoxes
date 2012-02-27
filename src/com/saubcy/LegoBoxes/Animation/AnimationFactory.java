package com.saubcy.LegoBoxes.Animation;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationFactory {
	
	public static Animation getButtonSlide() {
		AnimationSet as = new AnimationSet(false);
		
		Animation anima_1 = new TranslateAnimation(0.0f, 0.0f, 200.0f, -10.0f);
		anima_1.setInterpolator(new AccelerateDecelerateInterpolator());   
		anima_1.setDuration(600);
		anima_1.setFillAfter(false);
		
		Animation anima_2 = new TranslateAnimation(0.0f, 0.0f, -10.0f, 10.0f);
		anima_2.setInterpolator(new AccelerateDecelerateInterpolator());   
		anima_2.setDuration(150);
		anima_2.setFillAfter(false);
		anima_2.setStartOffset(600);
		
		Animation anima_3 = new TranslateAnimation(0.0f, 0.0f, 10.0f, 0.0f);
		anima_2.setInterpolator(new AccelerateDecelerateInterpolator());   
		anima_2.setDuration(200);
		anima_2.setFillAfter(false);
		anima_2.setStartOffset(750);
		
		as.addAnimation(anima_1);
		as.addAnimation(anima_2);
		as.addAnimation(anima_3);
		
		return as;
	}
	
	public static Animation getFadeOut(Context context) {
		Animation anima = new AlphaAnimation(1.0f, 0.0f);
		
		anima.setInterpolator(AnimationUtils.loadInterpolator(context, 
				android.R.anim.accelerate_decelerate_interpolator));
		anima.setDuration(1000);
		anima.setFillAfter(true);
		
		return anima;
	}
	
	public static Animation getFadeIn(Context context) {
		Animation anima = new AlphaAnimation(0.0f, 1.0f);
		
		anima.setInterpolator(AnimationUtils.loadInterpolator(context, 
				android.R.anim.accelerate_decelerate_interpolator));
		anima.setDuration(1000);
		anima.setFillAfter(true);
		
		return anima;
	}
	
	public static Animation getButtonFlicker(Context context) {
		Animation anima = new AlphaAnimation(1.0f, 0.0f);
		
		anima.setInterpolator(AnimationUtils.loadInterpolator(context, 
				android.R.anim.accelerate_decelerate_interpolator));
		anima.setDuration(100);
		anima.setRepeatCount(7);
		anima.setRepeatMode(Animation.REVERSE);
		anima.setFillAfter(true);
		
		return anima;
	}
	
	public static Animation getCentreRotate(Context context) {
		RotateAnimation anima = new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		
		anima.setInterpolator(AnimationUtils.loadInterpolator(context, 
				android.R.anim.linear_interpolator));
		anima.setDuration(4000);
		anima.setRepeatCount(10000);
		anima.setRepeatMode(Animation.RESTART);
		
		return anima;
	}
}
