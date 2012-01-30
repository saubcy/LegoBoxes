package com.saubcy.LegoBoxes.Object;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SortableObjectAdapter 
extends ArrayAdapter<DownloadObject> {

	protected Context context;
	protected int layoutResourceId;
	protected int textViewResourceId;
	protected List<DownloadObject> allObjects;
	protected List<DownloadObject> filteredObjects;	
	
	public SortableObjectAdapter(Context context, int resourceId) {
		super(context, resourceId);		
		this.context = context;
		this.layoutResourceId = resourceId;
		this.allObjects = new LinkedList<DownloadObject>();
		this.filteredObjects = new LinkedList<DownloadObject>();
	}

	public SortableObjectAdapter(Context context, int resourceId,	
			List<DownloadObject> objects) {
		super(context, resourceId);
		this.context = context;
		this.layoutResourceId = resourceId;
		this.allObjects = new LinkedList<DownloadObject>(objects);
		this.filteredObjects = new LinkedList<DownloadObject>();
		this.resetFilter();
		this.sort(true);
	}
	
	public SortableObjectAdapter(Context context, int resourceId, 
			int textViewResourceId) {
		super(context, resourceId, textViewResourceId);
		this.context = context;
		this.layoutResourceId = resourceId;
		this.textViewResourceId = textViewResourceId;
		this.allObjects = new LinkedList<DownloadObject>();
		this.filteredObjects = new LinkedList<DownloadObject>();
	}
	
	public void setLayoutID(int id) {
		this.layoutResourceId = id;
	}
	
	public void setTextViewID(int id) {
		this.textViewResourceId = id;
	}
	
	public void setObjects(List<DownloadObject> objects) {
		this.allObjects = new LinkedList<DownloadObject>(objects);
		this.resetFilter();
		this.sort(true);
	}
	
	@Override
	public void add(DownloadObject object) {
		this.allObjects.add(object);
	}
	
	@Override
	public void remove(DownloadObject object) {
		this.allObjects.remove(object);
		this.filteredObjects.remove(object);
	}
			
	public void resetFilter(){
		this.filteredObjects = new LinkedList<DownloadObject>(this.allObjects);
	}
	
	public void filterByName(String name){
		this.filteredObjects.clear();
		for(DownloadObject p : this.allObjects){
			if(p.getName().toLowerCase().contains(name.toLowerCase())){
				this.filteredObjects.add(p);
			}
		}
	}
	
	public void filterByState(DownloadObject.State state){
		this.filteredObjects.clear();
		for(DownloadObject p : this.allObjects){
			if( p.getState() == state ){
				this.filteredObjects.add(p);
			}
		}
	}
	
	@Override
	public int getCount() {
		return this.filteredObjects.size();
	}
	
	public void sort(boolean ASC){
		if(ASC){
			Collections.sort(this.filteredObjects, 
					new Comparator<DownloadObject>() {
				@Override
				public int compare(DownloadObject a, DownloadObject b) {
					return a.compareTo(b);
				}			
			});
		}else{
			Collections.sort(this.filteredObjects, new Comparator<DownloadObject>() {
				@Override
				public int compare(DownloadObject a, DownloadObject b) {
					return b.compareTo(a);
				}			
			});
		}
	}

	public DownloadObject getItem(int position) {
		if(position > this.filteredObjects.size()) {
			return null;
		}
		return this.filteredObjects.get(position);
	}

	public DownloadObject getItem(String ID) {
		for(DownloadObject p : this.allObjects){
			if(p.getFolder().equals(ID)){
				return p;
			}
		}
		return null;
	}
}