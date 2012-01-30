package com.saubcy.LegoBoxes.Object;

import java.io.File;
import java.text.Collator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.saubcy.LegoBoxes.Utils.FileTools;

public class DownloadObject implements Comparable<DownloadObject> {	
	
	public enum State {
		installed, not_installed, update, local, loading,
	}
	
	private String name;
	private List<String> categories = 
			new LinkedList<String>();
	private String folder;
	private State state;
	private Bitmap image;
	private long size;
	private int totalFileCount;
	private int doneFileCount;
	private long lastUpdate = 0;
		
	public DownloadObject(String name, String folder, 
			int totalFileCount, long size, State state) {
		this.name = name;
		this.folder = folder;
		this.size = size;
		this.totalFileCount = totalFileCount;
		this.doneFileCount = 0;
		this.state = state;
	}
	
	public DownloadObject(String name, String folder, State state) {
		this.name = name;
		this.folder = folder;
		this.doneFileCount = 0;
		this.state = state;
		this.image = BitmapFactory.decodeFile(new File(this.folder, "preview.gif").getPath());
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public void setTotalFileCount(int totalFileCount) {
		this.totalFileCount = totalFileCount;
	}

	public int getDoneFileCount() {
		return doneFileCount;
	}

	public void setDoneFileCount(int doneFileCount) {
		this.doneFileCount = doneFileCount;
	}

	public String getBytes(){
		return FileTools.formatBytes(size);
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}	
		
	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public void addCategory(String category) {
		this.categories.add(category);
	}

	@Override
	public boolean equals(Object o) {
		return this.getFolder().equals(((DownloadObject) o).getFolder());
	}

	@Override
	public int compareTo(DownloadObject another) {
		Collator collator = Collator.getInstance(Locale.ENGLISH);
		collator.setStrength(Collator.SECONDARY);
		return collator.compare(getName(), another.getName());
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
