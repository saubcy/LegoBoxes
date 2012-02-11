package com.saubcy.LegoBoxes.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saubcy.LegoBoxes.Animation.AnimationFactory;

public abstract class SplashRotate extends BaseActivity {
	
	private static final float BASE_WIDTH = 480.0f;
	private static final float BASE_TEXT_SIZE = 50.0f;
	private float realWidth = 100.0f;

	private ImageView indicator = null;
	private ImageView title = null;
	private Matrix matrix = null;
	private TextView progress = null;
	private int percent = 0;
	
	public void setTitle(int resid) {
		Bitmap bmp = 
				BitmapFactory.decodeResource(getResources(), resid);
		int bmpwidth = bmp.getWidth();
		int bmpheight = bmp.getHeight();
		Bitmap bm = 
				Bitmap.createBitmap(bmp, 0, 0, bmpwidth,
						bmpheight, matrix, true);
		title.setImageBitmap(bm);
	}
	
	public void setTitle(Bitmap bmp) {
		title.setImageBitmap(bmp);
	}
	
	public void setIndicator(int resid) {
		Bitmap bmp = 
				BitmapFactory.decodeResource(getResources(), resid);
		int bmpwidth = bmp.getWidth();
		int bmpheight = bmp.getHeight();
		Bitmap bm = 
				Bitmap.createBitmap(bmp, 0, 0, bmpwidth,
						bmpheight, matrix, true);
		indicator.setImageBitmap(bm);
	}
	
	public void setIndicator(Bitmap bmp) {
		indicator.setImageBitmap(bmp);
	}

	public void setProgressColor(int color) {
		progress.setTextColor(color);
	}
	
	public void setProgressSize(float size) {
		progress.setTextSize(size);
	}
	
	public void setProgressTypeface(Typeface fontFace) {
		progress.setTypeface(fontFace); 
	}
	
	public void startRotate() {
		indicator.startAnimation(AnimationFactory.getCentreRotate(this));
	}
	
	public int setPercent(int p) {
		percent = p;
		showPercent();
		return percent;
	}
	
	public int getPercent() {
		return percent;
	}
	
	public int addPercent(int p) {
		percent += p;
		if ( percent > 100 ) {
			percent = 100;
		}
		showPercent();
		return percent;
	}
	
	private void showPercent() {
		progress.setText(percent+"%");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadViews();
	}

	private void loadViews() {
		realWidth = 
				getWindowManager().getDefaultDisplay()
				.getWidth();
		matrix = new Matrix();
		matrix.postScale(realWidth/BASE_WIDTH, 
				realWidth/BASE_WIDTH);
		
		title = new ImageView(this);
		title.setPadding(20, 20, 20, 20);
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.ALIGN_TOP);
		Root.addView(title, rl);
		
		indicator = new ImageView(this);
		rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.CENTER_IN_PARENT);
		Root.addView(indicator, rl);
		
		progress = new TextView(this);
		this.setProgressSize(BASE_TEXT_SIZE*(realWidth/BASE_WIDTH));
		rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.CENTER_IN_PARENT);
		Root.addView(progress, rl);
	}
}
