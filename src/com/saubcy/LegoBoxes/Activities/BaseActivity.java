package com.saubcy.LegoBoxes.Activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public abstract class BaseActivity extends Activity {

	protected RelativeLayout Root = null;
	protected ImageView bg = null;
	
	public RelativeLayout getRoot() {
		return Root;
	}
	
	public void setBackgroundColor(int color) {
		Root.setBackgroundColor(color);
	}
	
	public void setBackground(Drawable d) {
		Root.setBackgroundDrawable(d);
	}
	
	public void setBackground(int resid) {
		Root.setBackgroundResource(resid);
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
		Root = new RelativeLayout(this.getBaseContext());
		
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		bg = new ImageView(this);
		bg.setScaleType(ScaleType.FIT_XY);
		Root.addView(bg, rl);
		
		this.setContentView(Root);
	}
}
