package com.saubcy.LegoBoxes.Test;

import java.io.IOException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.StageSelectorGallery;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public class StageSelectorGalleryTest extends StageSelectorGallery 
implements SelectListener {
	
	public static int GalleryHeight = 264;
	public static int TextHeight = 20;
	
	private String[] stages = {
			"s01", "s02", "s03", "s04",
	};
	
	public StageSelectorGalleryTest() {
		super(GalleryHeight, TextHeight);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setBackground(R.drawable.stage_selector_gallery_background);
		
		ImageView iv = new ImageView(this.getBaseContext());
		iv.setImageResource(R.drawable.stage_selector_gallery_tips);
		this.setTips(iv);
		
		this.setStageText("sadfsadfsdfsdf");
		this.loadStages(stages);
		this.setListener(this);
		
		RelativeLayout rl = this.getRoot();
		TextView tv = new TextView(this.getBaseContext());
		tv.setText("hello StageSelectorGalleryTest!!!!!!");
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.WRAP_CONTENT,  
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_BOTTOM);
		rl.addView(tv, rlp);
	}

	@Override
	public StageInfo getStageImg(String code) {
		
		ImageView iv = new ImageView(getApplicationContext());
		try {
			iv.setBackgroundDrawable(Drawable.createFromStream(getAssets().open(
					"stages/" + code + ".png"), code));
		} catch (IOException e) {
			Log.d("trace", "load error!!");
			e.printStackTrace();
		}
		
		return new StageInfo(iv, code);
	}

	@Override
	public void notifySelect(String code) {
		Intent i = new Intent(this.getBaseContext(), SplashSequenceTest.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("StageCode", code);
		this.startActivity(i);
	}

	@Override
	public void notifySelect(View v) {
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
