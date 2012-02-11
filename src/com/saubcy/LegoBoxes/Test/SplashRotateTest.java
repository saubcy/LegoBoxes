package com.saubcy.LegoBoxes.Test;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.SplashRotate;
import com.saubcy.LegoBoxes.Utils.CommonTools;

public class SplashRotateTest extends SplashRotate {

	private int addPercent = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadviews();
	}

	private void loadviews() {
		this.setBackground(R.drawable.background);
		this.setTitle(R.drawable.title);
		this.setIndicator(R.drawable.indicator);

		this.setProgressColor(Color.rgb(18, 134, 183));
		Typeface fontFace = Typeface.createFromAsset(getAssets(), 
				"fonts/wdhb.ttf");
		this.setProgressTypeface(fontFace);
		this.setPercent(0);
		this.startRotate();
		Thread thread =  new Thread(new Runnable() {					
			@Override
			public void run() {	
				try {
					while ( SplashRotateTest.this
							.getPercent() < 100 ) {
						showPercent(CommonTools.getRandom(5, 10));
						Thread.sleep(CommonTools.getRandom(100, 200));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start(); 


	}

	private void showPercent(int p) {
		addPercent = p;
		runOnUiThread(new Runnable() {					
			@Override
			public void run() {	
				SplashRotateTest.this.addPercent(addPercent);
			}
		});
	}

}
