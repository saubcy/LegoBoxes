package com.saubcy.LegoBoxes.Object;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.saubcy.LegoBoxes.Object.DownloadObject.State;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DownloadObjectAdapter extends SortableObjectAdapter{
	
	private int imageID;
	private int nameID;
	private int stateID;
	private int infoID;
	private int infoStrID;
	private String StateText = "•";
	
	public void setImageID(int id) {
		this.imageID = id;
	}
	
	public void setNameID(int id) {
		this.nameID = id;
	}
	
	public void setStateID(int id) {
		this.stateID = id;
	}
	
	public void setInfoID(int id, int strId) {
		this.infoID = id;
		this.infoStrID = strId;
	}
	
	public void setStateText(String t) {
		this.StateText = t;
	}
	
	private HashMap<String, Boolean> categories = new HashMap<String, Boolean>();
	
	public DownloadObjectAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public DownloadObjectAdapter(Context context, 
			int textViewResourceId,	List<DownloadObject> objects) {
		super(context, textViewResourceId, objects);
		this.loadUniqueCategegories();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)super.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(super.layoutResourceId, parent, false);
        }
        DownloadObject p = 
        		super.filteredObjects.get(position);
        if (p != null) {          	
        	((ImageView)v.findViewById(imageID)).setImageBitmap(p.getImage());
        	((TextView)v.findViewById(nameID)).setText(p.getName());
        	((TextView)v.findViewById(stateID)).setText(StateText);
        	switch (p.getState()) {
        	case installed:
        		((TextView)v.findViewById(stateID)).setTextColor(Color.GREEN);
        		break;
        	case not_installed:
        		((TextView)v.findViewById(stateID)).setTextColor(Color.RED);
        		break;
        	case update:
        		((TextView)v.findViewById(stateID)).setTextColor(Color.BLUE);
        		break;
        	case local:
        		((TextView)v.findViewById(stateID)).setTextColor(Color.CYAN);
        		break;
        	}
        	
        	if( p.getState() == State.loading){
        		((TextView)v.findViewById(stateID)).setTextColor(Color.rgb(255, 126, 0));
        		((TextView)v.findViewById(infoID)).setText(
        				context.getString(infoStrID, 
        						p.getDoneFileCount() ,p.getTotalFileCount()));
        	}else{
        		((TextView)v.findViewById(infoID)).setText(
        				context.getString(infoStrID, p.getTotalFileCount(), p.getBytes()));
        	}      
        }
        return v;
	}
	
	public boolean hasUpdate(){
		for(int i = 0; i < getCount(); i++){
			if(getItem(i).getState() == State.update)
				return true;
		}
		return false;
	}
	
	public boolean hasNotInstalled(){
		for(int i = 0; i < getCount(); i++){
			if(getItem(i).getState() == State.not_installed)
				return true;
		}
		return false;
	}
	
	public void loadUniqueCategegories(){
		categories = new HashMap<String, Boolean>();
		for(DownloadObject p : super.allObjects){
			for(String s : p.getCategories()){
				s = s.trim().toLowerCase();
				if(categories.containsKey(s) == false){
					categories.put(s, true);
				}
			}
		}
	}
	
	public HashMap<String, Boolean> getCategories(){
		return categories;
	}
	
	public String[] getCategoryNames(){
		return (String[])this.categories.keySet().toArray(new String[this.categories.keySet().size()]);
	}
	
	public String[] getCategoryNamesWithCount(){
		String[] names = this.getCategoryNames();
		for(int i = 0; i < names.length; i++){
			names[i] = names[i] + " (" + this.countPoniesInCategory(names[i]) + ")";
		}
		return names;
	}
	
	public boolean[] getCategoryStates(){
		Boolean[] values = this.categories.values().toArray(new Boolean[this.categories.size()]);
		boolean[] r = new boolean[values.length];
		for(int i = 0; i < values.length; i++){
			r[i] = values[i].booleanValue();
		}
		return r;
	}
	
	public void setCategoryFilter(String category, boolean state){
		this.categories.put(category, state);
	}

	public int countPoniesInCategory(String category){
		int c = 0;
		for(DownloadObject p : this.allObjects){
			if(containsIgnoreCase(p.getCategories(), category)){
				c++;
			}
		}
		return c;
	}
	
	public void filterByCategory() {
		super.filteredObjects.clear();
		for(DownloadObject p : this.allObjects){
			for(String c : p.getCategories()){
				c = c.trim().toLowerCase();
				if(this.categories.get(c) == true){
					super.filteredObjects.add(p);
					break;
				}
			}
		}
	}
	
	private boolean containsIgnoreCase(List <String> l, String s){
		 Iterator <String> it = l.iterator();
		 while(it.hasNext()){
			 if(it.next().equalsIgnoreCase(s))
				 return true;
		 }
		 return false;
	}
}