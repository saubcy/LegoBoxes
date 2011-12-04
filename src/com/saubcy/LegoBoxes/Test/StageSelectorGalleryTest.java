package com.saubcy.LegoBoxes.Test;

import java.io.IOException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.StageSelectorGallery;

public class StageSelectorGalleryTest extends StageSelectorGallery 
implements StageSelectorGallery.SelectListener {
	
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
		
		this.setBackgroud(R.drawable.stage_selector_gallery_background);
		
		ImageView iv = new ImageView(this.getBaseContext());
		iv.setImageResource(R.drawable.stage_selector_gallery_tips);
		this.setTips(iv);
		
		this.setStageText("sadfsadfsdfsdf");
		this.loadStages(stages);
		this.setListener(this);
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
		Intent i = new Intent(this.getBaseContext(), Stub_01.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("StageCode", code);
		this.startActivity(i);
	}
}
