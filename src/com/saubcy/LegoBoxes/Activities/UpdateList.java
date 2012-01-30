package com.saubcy.LegoBoxes.Activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.saubcy.LegoBoxes.Object.DownloadObject;
import com.saubcy.LegoBoxes.Object.DownloadObject.State;
import com.saubcy.LegoBoxes.Object.DownloadObjectAdapter;
import com.saubcy.LegoBoxes.Utils.AsynFolderDownloader.onDownloadListener;
import com.saubcy.LegoBoxes.Utils.AsynImageLoader;
import com.saubcy.LegoBoxes.Utils.AsynImageLoader.onImageListener;

public class UpdateList extends ListActivity 
implements onDownloadListener, onImageListener{

	public interface DownloadHandler{
		public void notifyFailed(String msg);
		public SharedPreferences getSharedPreferences();
		public DownloadObject parseNet(String line);
		public DownloadObject parseLocal(File folder);
	}

	private String REMOTE_LIST_URL = "";
	private String REMOTE_BASE_URL = "";
	private String localConfigFile = "list.ini";
	private String previewFileName = "preview.gif";
	private int rowLayoutID;

	private DownloadHandler handler = null;

	private RelativeLayout Root = null;

	private boolean isDownload = false;
	private boolean isUpdate = false;

	private File localFolder = null;
	private AsynImageLoader asynImageLoader = null;
	protected DownloadObjectAdapter adapter = null;

	public RelativeLayout getRoot() {
		return Root;
	}

	public void setRowLayoutID (int id) {
		this.rowLayoutID = id;
	}

	public void setHandler(DownloadHandler dh) {
		this.handler = dh;
	}

	public void setLocalConfigName(String name) {
		this.localConfigFile = name;
	}

	public void setPreviewFileName(String name) {
		this.previewFileName = name;
	}

	public void setRemoteURL(String url) {
		this.REMOTE_LIST_URL = url;
	}

	public void setBaseURL(String url) {
		this.REMOTE_BASE_URL = url;
	}

	public boolean getDownloadStatus() {
		return isDownload;
	}

	public boolean getUpdateStatus() {
		return isUpdate;
	}

	public void setLocalFolder(File f) {
		this.localFolder = f;
	}

	public UpdateList() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		init();
		loadViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void loadViews() {
		Root = new RelativeLayout(this.getBaseContext());
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(  
				RelativeLayout.LayoutParams.FILL_PARENT,  
				RelativeLayout.LayoutParams.FILL_PARENT);
		ListView lv = new ListView(this);
		lv.setId(android.R.id.list);
		Root.addView(lv, rl);
		setContentView(Root);
	}

	private void init() {
		asynImageLoader = new AsynImageLoader(this, this);
		adapter = new DownloadObjectAdapter(UpdateList.this, 
				rowLayoutID);
	}

	public DownloadObjectAdapter getDownloadAdapter() {
		return adapter;
	}

	public void loadList() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				final ArrayList<DownloadObject> objects = loadObjects();
				adapter.setObjects(objects);
				SharedPreferences preferences = 
						handler.getSharedPreferences();
				for(int i=0; i<adapter.getCount(); ++i){
					DownloadObject p = adapter.getItem(i);
					if(p.getState() == State.not_installed){
						asynImageLoader.push(p.getFolder(), 
								REMOTE_BASE_URL 
								+ p.getFolder() 
								+ "/" + previewFileName);
						Log.d("trace", "deal: "+p.getFolder());
					} else {
						adapter.getItem(i).setImage(
								BitmapFactory.decodeFile(
										new File(localFolder, p.getFolder() 
												+ "/" + previewFileName).getPath()));
					}
					long lastUpdateLocal = preferences.getLong(
							"lastupdate_" + p.getFolder(), 0);
					long lastUpdateRemote = p.getLastUpdate();
					if((lastUpdateRemote > lastUpdateLocal) 
							&& p.getState() != State.not_installed) {
						adapter.getItem(i).setState(State.update);
					}
				}
				asynImageLoader.start();
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {	
						Log.d("trace", "show list");
						setListAdapter(adapter);	
						setProgressbarVisibility(false);	
					}
				});
			}
		}).start();
		setProgressbarVisibility(true);
	}

	private void setProgressbarVisibility(boolean visible){
		//		int v = (visible) ? View.VISIBLE : View.GONE;
		//		((ProgressBar)findViewById(R.id.progress_circular)).setVisibility(v);
	}

	private ArrayList<DownloadObject> loadObjects(){
		ArrayList<DownloadObject> objectList = 
				new ArrayList<DownloadObject>();
		try {
			//	load net files
			URL listFile = new URL(REMOTE_LIST_URL);
			URLConnection urlCon = listFile.openConnection();
			urlCon.setConnectTimeout(10000);
			urlCon.setReadTimeout(10000);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(urlCon.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {		 
				DownloadObject p = handler.parseNet(line);
				Log.d("trace", "add: "+p.getName());
				objectList.add(p);
			}

			// read local files
			File[] subFolders = 
					this.localFolder.listFiles(new FileFilter() {				
						@Override
						public boolean accept(File pathname) {
							if(pathname.isDirectory() == false) {
								return false;
							}
							File[] files = pathname.listFiles(new FileFilter() {						
								@Override
								public boolean accept(File pathname) {
									return pathname.getName().equalsIgnoreCase(localConfigFile);
								}
							});
							return files.length > 0;
						}
					});

			for(File folder : subFolders){
				DownloadObject p = handler.parseLocal(folder);
				if(objectList.contains(p) == false){
					objectList.add(p);
				}
			}

		} catch (Exception e) {
			handler.notifyFailed(e.getMessage());
			e.printStackTrace();
		}	
		return objectList;
	}

	@Override
	public void imageComplete(String ID, Bitmap image) {
		final DownloadObject p = adapter.getItem(ID);
		if(p == null) {
			return;
		}
		p.setImage(image);
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {	
				adapter.notifyDataSetChanged();	
			}
		});
	}

	@Override
	public void imageError(String ID, String error) {
		final String msg = error;
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {	
				handler.notifyFailed(msg);
			}
		});	
	}

	@Override
	public void onDownloadStart(String ID) {
		DownloadObject p = adapter.getItem(ID);
		if(p == null) {
			return;
		}
		p.setState(State.loading);
		p.setDoneFileCount(0);
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {	
				adapter.notifyDataSetChanged();	
			}
		});
	}

	@Override
	public void onDownloadChanged(String ID, int filesDone) {
		DownloadObject p = adapter.getItem(ID);
		if(p == null) return;
		p.setDoneFileCount(filesDone);
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {	
				adapter.notifyDataSetChanged();	
			}
		});		
	}

	@Override
	public void onDownloadDone(String ID) {
		DownloadObject p = adapter.getItem(ID);
		if(p == null) return;
		p.setState(State.installed);
		SharedPreferences preferences = 
				handler.getSharedPreferences();
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong("lastupdate_" + p.getFolder(), p.getLastUpdate());
		editor.commit();
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {	
				adapter.notifyDataSetChanged();	
			}
		});			
	}

	@Override
	public void onDownloadError(String ID, String error) {
		final DownloadObject p = adapter.getItem(ID);
		if(p == null) return;
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				p.setState(State.not_installed);				
				handler.notifyFailed("Error while downloading " 
						+ p.getName());
				adapter.notifyDataSetChanged();

				// clean up already finished files
				File folder = new File(localFolder, p.getFolder());
				if(folder.isDirectory() == false) return;
				File[] files = folder.listFiles();
				if(files == null) return;
				for(File f : files){
					f.delete();
				}
				folder.delete();
			}
		});
	}
}
