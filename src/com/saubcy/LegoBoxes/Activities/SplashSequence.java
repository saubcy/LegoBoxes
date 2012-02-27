package com.saubcy.LegoBoxes.Activities;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.saubcy.LegoBoxes.Animation.AnimationFactory;
import com.saubcy.LegoBoxes.Interface.SplashOverListener;

public abstract class SplashSequence extends BaseActivity {
	
	private Animation fade_in = null;
	private Animation fade_out = null;
	private SplashOverListener listener = null;
	private List<Drawable> logoList = 
			new ArrayList<Drawable>();
	private int index = -1;
	
	public void setBackgroundColor(int color) {
		Root.setBackgroundColor(color);
	}
	
	public void setListener(SplashOverListener sl) {
		this.listener = sl;
	}
	
	public void addLogo(Drawable d) {
		logoList.add(d);
	}
	
	public void start() {
		changeLogo();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadViews();
	}
	
	private void changeLogo(){
		if ( logoList.size() == (index+1) ) {
			listener.notifyOver();
			return;
		}
		bg.setImageDrawable(logoList.get(++index));
//		bg.setBackgroundDrawable(logoList.get(++index));
		bg.startAnimation(fade_in);
	}
	
	private void loadViews() {
		fade_in = AnimationFactory.getFadeIn(this);
		fade_in.setDuration(2000);
		fade_out = AnimationFactory.getFadeOut(this);
		
		fade_in.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				bg.startAnimation(fade_out);
			}
		});
		
		fade_out.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				changeLogo();
			}
		});
		
	}
}
