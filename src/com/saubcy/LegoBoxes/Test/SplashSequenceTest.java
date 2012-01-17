package com.saubcy.LegoBoxes.Test;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.SplashSequence;
import com.saubcy.LegoBoxes.Interface.SplashOverListener;

public class SplashSequenceTest extends SplashSequence
implements SplashOverListener {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		init();
		Log.d("trace", "splash start");
		this.start();
	}
	
	private void init() {
		this.setListener(this);
		this.setBackgroundColor(Color.WHITE);
		this.addLogo(getResources()
                .getDrawable(R.drawable.logo_1));
		this.addLogo(getResources()
                .getDrawable(R.drawable.logo_2));
		this.addLogo(getResources()
                .getDrawable(R.drawable.logo_3));
	}

	@Override
	public void notifyOver() {
		Log.d("trace", "splash end");
	}
}
