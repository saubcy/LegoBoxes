package com.saubcy.LegoBoxes.Activities;

import android.app.ListActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public abstract class BaseListActivity extends ListActivity {

	protected RelativeLayout Root = null;
	protected ImageView bg = null;
	protected ListView list = null;
	
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
	
	protected abstract void initBefore();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initBefore();
		loadViews();
	}
	
	private void loadViews() {
		Root = new RelativeLayout(this.getBaseContext());
		
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		bg = new ImageView(this);
		Root.addView(bg, rl);
		
		list = new ListView(this);
		list.setId(android.R.id.list);
		rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		Root.addView(list, rl);
		
		this.setContentView(Root);
	}
}
