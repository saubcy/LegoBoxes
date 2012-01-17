package com.saubcy.LegoBoxes.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.saubcy.LegoBoxes.Animation.AnimationFactory;
import com.saubcy.LegoBoxes.Interface.SplashOverListener;

public class SplashSequence extends Activity {
	
	private RelativeLayout Root = null;
	private Animation fade_in = null;
	private Animation fade_out = null;
	private ImageView bg = null;
	private SplashOverListener listener = null;
	private List<Drawable> logoList = 
			new ArrayList<Drawable>();
	private int index = -1;
	
	public RelativeLayout getRoot() {
		return Root;
	}
	
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		loadViews();
	}
	
	private void changeLogo(){
		if ( logoList.size() == (index+1) ) {
			listener.notifyOver();
			return;
		}
		bg.setBackgroundDrawable(logoList.get(++index));
		bg.startAnimation(fade_in);
	}
	
	private void loadViews() {
		Root = new RelativeLayout(this.getBaseContext());
		
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		bg = new ImageView(this);
		bg.setBackgroundResource(R.drawable.logo_3);
		Root.addView(bg, rl);
		
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
		
		this.setContentView(Root);
	}
}
