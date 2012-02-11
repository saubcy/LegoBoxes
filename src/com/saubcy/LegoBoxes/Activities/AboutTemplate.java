package com.saubcy.LegoBoxes.Activities;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class AboutTemplate extends BaseActivity {

	private TextView content = null;
	
	private String vesrionInfo = "";
	private String copyrightInfo = "";
	private String contentInfo = "";
	private String contactInfo = "";
	
	public void setContentColor(int color) {
		content.setTextColor(color);
	}
	
	public void setLinkColor(int color) {
		content.setLinkTextColor(color);
	}
	
	public void setVersion(String info) {
		this.vesrionInfo = info;
	}
	
	public void setCopyright(String info) {
		this.copyrightInfo = info;
	}
	
	public void setContent(String info) {
		this.contentInfo = info;
	}
	
	public void setContact(String info) {
		this.contactInfo = info;
	}
	
	public void genAbout() {
		String about =
				vesrionInfo
				+  "\n" + copyrightInfo 
				+ "\n\n" + contentInfo
				+ "\n\n" + contactInfo;
		content.setText(about);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadViews();
	}
	
	private void loadViews() {
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		content = new TextView(this);
		content.setAutoLinkMask(Linkify.EMAIL_ADDRESSES|Linkify.WEB_URLS);
		content.setIncludeFontPadding(true);
		Root.addView(content, rl);
	}
	
}
