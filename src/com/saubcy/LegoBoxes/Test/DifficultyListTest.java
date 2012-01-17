package com.saubcy.LegoBoxes.Test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.saubcy.LegoBoxes.Activities.DifficultyList;
import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public class DifficultyListTest extends DifficultyList 
implements SelectListener {
	
	private ImageView newgame = null;
	private ImageView options = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setBackgroud(getResources().getDrawable(R.drawable
				.welcome_button_bottom_background));
		this.setButtonBackground(getResources().getDrawable(R.drawable
				.custom_toast_border));

		newgame = new ImageView(this.getBaseContext());
		newgame.setImageDrawable(getResources()
				.getDrawable(R.drawable.welcome_button_bottom_newgame));
		this.addButton(newgame, "I'm new game");
		
		options = new ImageView(this.getBaseContext());
		options.setImageDrawable(getResources()
				.getDrawable(R.drawable.welcome_button_bottom_options));
		this.addButton(options, "I'm new game");
		
		this.setListener(this);
	}

	@Override
	public void notifySelect(View v) {
		if ( v == newgame ) {
			Log.d("trace", "newgame click");
		} else if ( v == options ) {
			Log.d("trace", "options click");
		}
	}

	@Override
	public void notifySelect(String code) {
		
	}

	@Override
	public void notifySelect(int code) {
		// TODO Auto-generated method stub
		
	}
	
}
