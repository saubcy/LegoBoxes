package com.saubcy.LegoBoxes.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.saubcy.LegoBoxes.Interface.SelectListener;
import com.saubcy.LegoBoxes.ViewGroup.SlidingLayoutWithAnim;

public abstract class StageSelectorGallery extends BaseActivity 
implements SelectListener {

	private TableLayout Container = null;
	private LinearLayout TipsContainer = null;
	private SlidingLayoutWithAnim StageGellery = null;
	private TextView StageText = null;
	private SelectListener listenner = null;
	
	private int StageGalleryHeight = 264;
	private int StageTextHeight = 20;
	
	private String[] Stages = null;

	public StageSelectorGallery(int gh, int th) {
		this.StageGalleryHeight = gh;
		this.StageTextHeight = th;
	}

	public void  setTips(View v) {
		LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		TipsContainer.addView(v, lp);
	}
	
	public void setStageText(String text) {
		StageText.setText(text);
	}
	
	public void setListener(SelectListener sl) {
		this.listenner = sl;
	}
	
	public void loadStages(String[] stages) {
		Stages = stages;
		
		StageGellery.MAX_CHILDREN = Stages.length;
		if (Stages.length % 2 == 1) {
			StageGellery.NUMBER_CHILDREN_UNDER = 
					Math.round(Stages.length / 2) - 1;
		} else {
			StageGellery.NUMBER_CHILDREN_UNDER = 
					Stages.length / 2;
		}
		
		ImageView map = null;
		StageInfo si = null;
		SlidingLayoutWithAnim.GalleryLayout linearLayout = null;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int i = 0;
		String[] satageNames = new String[Stages.length];
		for (String code : Stages) {
			linearLayout = 
					new SlidingLayoutWithAnim.GalleryLayout(code, getApplicationContext());
			linearLayout.setPadding(5, 5, 5, 5);
			linearLayout.setBackgroundColor(Color.WHITE);
			si = getStageImg(code);
			map = si.iv;

			linearLayout.addView(map);
			StageGellery.addView(linearLayout, layoutParams);
			satageNames[i++] = si.name;
		}
		StageGellery.IMAGE_NAMES = satageNames;
		StageGellery.IMAGE_NAME_TEXT_VIEW = StageText;
		StageGellery.IMAGE_NAME_TEXT_VIEW.setText(satageNames[0]);
	}
	
	public abstract StageInfo getStageImg(String code);
	
	@Override
	public void  notifySelect(String code) {
		if ( null != listenner ) {
			listenner.notifySelect(code);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadViews();
	}

	private void loadViews() {
		Container = new TableLayout(this.getBaseContext());
		Container.setOrientation(TableLayout.VERTICAL);
		TableLayout.LayoutParams tl =
				new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
		Container.setLayoutParams(tl);
		Container.setGravity(Gravity.CENTER_HORIZONTAL);

		TipsContainer = new LinearLayout(this.getBaseContext());
		TipsContainer.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		TipsContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		Container.addView(TipsContainer, lp);

		StageGellery = 
				new SlidingLayoutWithAnim(
						this.getBaseContext());
		tl = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				StageGalleryHeight);
		Container.addView(StageGellery, tl);
		StageGellery.setListener(this);
		
		StageText = new TextView(this.getBaseContext());
		tl = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				StageTextHeight);
		tl.gravity = Gravity.CENTER;
		StageText.setGravity(Gravity.CENTER);
		Container.addView(StageText, tl);
		
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		Root.addView(Container, rl);
	}
	
	public class StageInfo {
		public ImageView iv = null;
		public String name = null;
		
		public StageInfo(ImageView iv) {
			this.iv = iv;
		}
		
		public StageInfo(ImageView iv, String name) {
			this.iv = iv;
			this.name = name;
		}
	}
	
}
