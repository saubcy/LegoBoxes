package com.saubcy.LegoBoxes.Interface;

import android.view.View;

public interface SelectListener {
	
	public void notifyQuick();
	
	public void notifySelect(View v);
	
	public void notifySelect(String code);
	
	public void notifySelect(int code);
}
