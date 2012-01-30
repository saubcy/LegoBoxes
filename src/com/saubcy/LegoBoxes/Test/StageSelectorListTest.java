package com.saubcy.LegoBoxes.Test;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.StageSelectorList;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public class StageSelectorListTest extends StageSelectorList 
implements SelectListener {
	
	private ArrayList<StageSelectorList.LevelMetaData> mLevels = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setListener(this);
		
		mLevels = getLevelData();
		this.setRowLayoutId(R.layout.level_select_row);
		this.setTextId(R.id.level_name);
		this.setLevelData(mLevels);
	}
	
	protected ArrayList<StageSelectorList.LevelMetaData> getLevelData() {
		ArrayList<StageSelectorList.LevelMetaData> levels =
				new ArrayList<StageSelectorList.LevelMetaData>();
		
		StageSelectorList.LevelMetaData level =
				new StageSelectorList.LevelMetaData("level-1", 0);
		levels.add(level);
		
		level = new StageSelectorList.LevelMetaData("level-2", 1);
		levels.add(level);
		
		level = new StageSelectorList.LevelMetaData("level-3", 2);
		levels.add(level);
		
		return levels;
	}

	@Override
	public void notifySelect(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifySelect(String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifySelect(int code) {
		Log.d("trace", "notifySelect: "+code);
		StageSelectorList.LevelMetaData level =
				mLevels.get(code);
		if ( null != mLevels ) {
			Log.d("trace", "select level: " + level.name);
		}
	}

	@Override
	public void notifyQuick() {
		// TODO Auto-generated method stub
		
	}
}
