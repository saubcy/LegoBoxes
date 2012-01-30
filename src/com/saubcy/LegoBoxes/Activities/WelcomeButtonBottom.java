package com.saubcy.LegoBoxes.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.saubcy.LegoBoxes.Animation.AnimationFactory;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public abstract class WelcomeButtonBottom extends Activity {

	private List<View> Buttons = null;
	private RelativeLayout Root = null;
	private LinearLayout Container = null;
	private LinearLayout ButtonContainer = null;
	private ImageView header = null;

	private Animation FadeOutAnimation = null;

	private int ButtonSpan = 20;
	private long AnimationOffset = 200L;

	private SelectListener listener = null;

	public WelcomeButtonBottom() {
		Buttons = new ArrayList<View>();
	}

	public void setSpan(int span) {
		this.ButtonSpan = span;
	}
	
	public void addButton(View button) {
		if ( Buttons.contains(button) ) {
			return;
		}

		LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		ButtonContainer.addView(button, lp);
		LinearLayout span = new LinearLayout(this.getBaseContext());
		span.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				ButtonSpan);
		ButtonContainer.addView(span, lp);

		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				notifyInner();
				FadeOutAnimation.setAnimationListener(
						new StartActivityAfterAnimation(v));
				fadeoutAllButtons();
			}
		};
		button.setOnClickListener(listener);

		Buttons.add(button);
	}
	
	public void setHeader(Drawable d) {
		header.setBackgroundDrawable(d);
	}

	public void setListener(SelectListener sl) {
		this.listener = sl;
	}

	public void fadeoutAllButtons() {
		for ( int i=0; i<Buttons.size(); ++i ) {
			Buttons.get(i).startAnimation(FadeOutAnimation);
		}
	}

	public void setBackgroud(Drawable d) {
		Root.setBackgroundDrawable(d);
	}

	public void setBackgroud(int resid) {
		Root.setBackgroundResource(resid);
	}

	public void setButtonSpan(int span) {
		ButtonSpan = span;
	}

	public void setAnimationOffset(long offset) {
		AnimationOffset = offset;
	}

	public RelativeLayout getRoot() {
		return Root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		loadViews();
	}

	private void loadViews() {
		FadeOutAnimation = AnimationFactory.getFadeOut(this.getBaseContext());
		
		Container = new LinearLayout(this.getBaseContext());
		Container.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		Container.setLayoutParams(lp);
		Container.setGravity(Gravity.BOTTOM);

		ButtonContainer = new LinearLayout(this.getBaseContext());
		ButtonContainer.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		lp.gravity = Gravity.BOTTOM;
		ButtonContainer.setGravity(Gravity.BOTTOM);
		Container.addView(ButtonContainer, lp);

		Root = new RelativeLayout(this.getBaseContext());
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
		Root.addView(Container, rl);
		setContentView(Root);
		
		header = new ImageView(this);
		rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.ALIGN_TOP);
		rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
		Root.addView(header, rl);
	}

	@Override
	protected void onResume() {
		super.onResume();

		long offset = 0L;
		for ( int i=0; i<Buttons.size(); ++i ) {
			View button = Buttons.get(i);
			button.setVisibility(View.VISIBLE);
			button.clearAnimation();
			Animation anim = AnimationFactory.getButtonSlide();
			if ( offset > 0 ) {
				anim.setStartOffset(offset);
				offset += AnimationOffset;
			}
			button.startAnimation(anim);
		}
	}
	
	private void notifyInner() {
		if ( null != listener ) {
			listener.notifyQuick();
		}
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
		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {

		}
	}

}
