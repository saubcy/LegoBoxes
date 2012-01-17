package com.saubcy.LegoBoxes.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saubcy.LegoBoxes.Animation.AnimationFactory;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public class DifficultyList extends Activity {

	private List<View> Buttons = null;
	private List<View> Texts = null;
	private RelativeLayout Root = null;
	private FrameLayout Container = null;
	private LinearLayout ButtonContainer = null;
	private ImageView bg = null;
	private int textColor = Color.WHITE;
	private float textSize = 14.0f;

	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;

	private SelectListener listener = null;

	public DifficultyList() {
		Buttons = new ArrayList<View>();
		Texts = new ArrayList<View>();
	}

	public void setListener(SelectListener sl) {
		this.listener = sl;
	}

	public RelativeLayout getRoot() {
		return Root;
	}

	public void setTextColor(int color) {
		textColor = color;
	}

	public void setTextSize(float size) {
		textSize = size;
	}

	public void addButton(View button, String text) {
		if ( Buttons.contains(button) ) {
			return;
		}

		LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		if (Buttons.size()>0) {
			lp.setMargins(0, 15, 0, 0);
		}
		ButtonContainer.addView(button, lp);

		TextView tv = new TextView(this);
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		tv.setTextColor(textColor);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		tv.setFocusable(false);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		tv.setText(text);
		ButtonContainer.addView(tv, lp);

		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				v.startAnimation(mButtonFlickerAnimation);
	            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(v));
	            fadeoutExcpet(v);
	            fadeoutAllTexts();
			}
		};
		button.setOnClickListener(listener);

		Buttons.add(button);
		Texts.add(tv);
	}

	public void setBackgroud(Drawable d) {
		bg.setBackgroundDrawable(d);
	}
	
	public void setButtonBackground(Drawable d) {
		ButtonContainer.setBackgroundDrawable(d);
	}
	
	private void fadeoutAllTexts() {
		bg.startAnimation(mFadeOutAnimation);
		for ( int i=0; i<Texts.size(); ++i ) {
			Texts.get(i).startAnimation(mAlternateFadeOutAnimation);
		}
	}
	
	private void fadeoutExcpet(View v) {
		for ( int i=0; i<Buttons.size(); ++i ) {
			View b = Buttons.get(i);
			if ( b == v ) {
				continue;
			}
			b.startAnimation(mAlternateFadeOutAnimation);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		init();
		loadViews();
	}

	private void init() {
		mButtonFlickerAnimation = AnimationFactory.getButtonFlicker(this);
		mFadeOutAnimation = AnimationFactory.getFadeOut(this);
		mAlternateFadeOutAnimation = AnimationFactory.getFadeOut(this);
	}

	private void loadViews() {
		Container = new FrameLayout(this.getBaseContext());
		Root = new RelativeLayout(this.getBaseContext());
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		Root.addView(Container, rl);

		bg = new ImageView(this);
		bg.setAdjustViewBounds(true);
		bg.setScaleType(ScaleType.FIT_XY);
		FrameLayout.LayoutParams fp = 
				new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
		fp.gravity = Gravity.CENTER_VERTICAL;
		Container.addView(bg, fp);

		ButtonContainer = new LinearLayout(this);
		ButtonContainer.setOrientation(LinearLayout.VERTICAL);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT
				);
		ButtonContainer.setPadding(10, 10, 10, 10);
		lp.gravity = Gravity.CENTER;
		ButtonContainer.setGravity(Gravity.CENTER);
		Container.addView(ButtonContainer, lp);

		this.setContentView(Root);	

	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private View selectView;

		StartActivityAfterAnimation(View v) {
			this.selectView = v;
		}

		public void onAnimationEnd(Animation animation) {
			
			for ( int i=0; i<Buttons.size(); ++i ) { 
				View button = Buttons.get(i);
				button.setVisibility(View.INVISIBLE);
				button.clearAnimation();
			}

			if ( null != listener ) {
				listener.notifySelect(selectView);
			}
			  
			finish();	
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationStart(Animation animation) {
		}

	}
}
