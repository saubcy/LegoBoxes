package com.saubcy.LegoBoxes.Test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.saubcy.LegoBoxes.Activities.R;

public class Stub_01 extends Activity {
	
	private LinearLayout Container = null;
	private LinearLayout ButtonContainer = null;
	private int ButtonSpan = 20;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Container = new LinearLayout(this.getBaseContext());
		Container.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
		Container.setLayoutParams(lp);
		Container.setBackgroundResource(R.drawable.welcome_button_bottom_background);
		setContentView(Container);

		ButtonContainer = new LinearLayout(this.getBaseContext());
		ButtonContainer.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		ButtonContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		Container.addView(ButtonContainer, lp);

		ImageView options = new ImageView(this.getBaseContext());
		options.setImageDrawable(getResources()
				.getDrawable(R.drawable.welcome_button_bottom_options));
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		ButtonContainer.addView(options, lp);
		LinearLayout span = new LinearLayout(this.getBaseContext());
		span.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				ButtonSpan);
		ButtonContainer.addView(span, lp);
		
		ImageView newgame = new ImageView(this.getBaseContext());
		newgame.setImageDrawable(getResources()
				.getDrawable(R.drawable.welcome_button_bottom_newgame));
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		ButtonContainer.addView(newgame, lp);
		span = new LinearLayout(this.getBaseContext());
		span.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				ButtonSpan);
		ButtonContainer.addView(span, lp);
		
		Intent i = this.getIntent();
		Log.d("trace", "stage: "+i.getStringExtra("StageCode"));
	}
}
