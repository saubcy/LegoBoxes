package com.saubcy.LegoBoxes.Test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.WelcomeButtonBottom;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public class WelcomeButtonBottomTest extends WelcomeButtonBottom 
implements SelectListener {

	private ImageView newgame = null;
	private ImageView options = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// add background
		//		this.setBackgroud(R.drawable.welcome_button_bottom_background);
		this.setBackground(getResources().getDrawable(R.drawable
				.welcome_button_bottom_background));

		//		add button
		newgame = new ImageView(this.getBaseContext());
		newgame.setImageDrawable(getResources()
				.getDrawable(R.drawable.welcome_button_bottom_newgame));
		this.addButton(newgame);

		options = new ImageView(this.getBaseContext());
		options.setImageDrawable(getResources()
				.getDrawable(R.drawable.welcome_button_bottom_options));
		this.addButton(options);

		this.setListener(this);

		RelativeLayout rl = this.getRoot();
		TextView tv = new TextView(this.getBaseContext());
		tv.setText("hello WelcomeButtonBottomTest!!!!!!");
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_BOTTOM);
		rl.addView(tv, rlp);
	}

	@Override
	public void notifySelect(View v) {
		if ( v == newgame ) {
			Log.d("trace", "newgame");
			Intent i = new Intent(this.getBaseContext(), 
					//					StageSelectorGalleryTest.class);
					//					StageSelectorListTest.class);
					//					SplashSequenceTest.class);
					//					DifficultyListTest.class);
					//					StageSelectorGalleryTest.class);
					//					StageSelectorListTest.class);
					StageSelectorListTest.class);
			//					UpdateListTest.class);
			this.startActivity(i);
		} else if ( v == options ) {
			Log.d("trace", "options");
			Intent i = new Intent(this.getBaseContext(), 
					//					DockPanelTest.class);
					//					DifficultyListTest.class);
					UpdateListTest.class);
			this.startActivity(i);
		}
	}

	@Override
	public void notifySelect(String code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySelect(int code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyQuick() {
		// TODO Auto-generated method stub

	}

}
