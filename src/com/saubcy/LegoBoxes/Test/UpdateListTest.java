package com.saubcy.LegoBoxes.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.saubcy.LegoBoxes.Activities.R;
import com.saubcy.LegoBoxes.Activities.UpdateList;
import com.saubcy.LegoBoxes.Object.DownloadObject;
import com.saubcy.LegoBoxes.Utils.FileTools;

public class UpdateListTest extends UpdateList 
implements UpdateList.DownloadHandler {

	public static final String REMOTE_BASE_URL = 
			"http://mlp-livewallpaper.googlecode.com/svn/assets/";
	public static final String REMOTE_LIST_URL = 
			REMOTE_BASE_URL + "ponies2.lst";
	private File localFolder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		this.loadList();
	}

	private void init() {
		this.setRowLayoutID(R.layout.update_row);

		this.getDownloadAdapter().setLayoutID(R.layout.update_row);
		this.getDownloadAdapter().setImageID(R.id.row_preview);
		this.getDownloadAdapter().setInfoID(R.id.row_textInfo, 
				R.string.row_info);
		this.getDownloadAdapter().setNameID(R.id.row_title);
		this.getDownloadAdapter().setStateID(R.id.row_state);
		if(FileTools.isSDMounted()) {
			localFolder = new File(
					Environment.getExternalStorageDirectory(), 
					"UpdateListTest");

		} else {
			localFolder = new File(getFilesDir(), 
					"UpdateListTest");
		}
		this.setLocalFolder(localFolder);
		this.setBaseURL(REMOTE_BASE_URL);
		this.setRemoteURL(REMOTE_LIST_URL);
		this.setPreviewFileName("preview.gif");
		this.setLocalConfigName("list.ini");
		this.setHandler(this);
	}

	@Override
	public void notifyFailed(String msg) {
		Log.d("trace", "failed: "+msg);
	}

	@Override
	public DownloadObject parseNet(String line) {
		line = line.trim();
		if(line.startsWith("'")) {
			return null;
		}
		final String data[] = splitWithQualifiers(line, ",", "[", "]");
		if(data.length < 5) {
			return null;
		}
		File local = new File(localFolder, data[2]);
		DownloadObject.State state = 
				DownloadObject.State.not_installed;
		if(local.exists()) {
			state = DownloadObject.State.installed;
		}
		DownloadObject p = 
				new DownloadObject(data[0], data[2], 
						Integer.valueOf(data[3].trim()), 
						Long.valueOf(data[4].trim()), state);

		String[] categories;
		if(data[1].contains(","))
			categories = data[1].trim().split(",");
		else
			categories = new String[] {data[1].trim()};

		p.setCategories(Arrays.asList(categories));
		p.setImage(BitmapFactory.decodeResource(getResources(), 
				R.drawable.logo_2));
		p.setLastUpdate(Long.valueOf(data[4]));
		return p;
	}

	@Override
	public DownloadObject parseLocal(File folder) {
		String name = "";
		String[] categories = null;		
		try{
			String line = "";
			File iniFile = new File(folder, "pony.ini");
			if(iniFile.exists() == false)
				iniFile = new File(folder, "Pony.ini");
			BufferedReader br = null;
			InputStreamReader is = 
					new InputStreamReader(
							new FileInputStream(iniFile), "UTF-8");
			if(is.read() == 0x0fffd){		    	
				br = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(iniFile), "UTF-16"));
			} else {
				br = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(iniFile), "UTF-8"));
			}
			is.close();
			while ((line = br.readLine()) != null) {	
				if(line.startsWith("'")) continue; //skip comments
				if(line.toLowerCase().startsWith("name,")){ 
					name = line.substring("name,".length()); 
					continue;
				}
				if(line.toLowerCase().startsWith("categories,")){
					String category = line.substring("categories,".length());
					categories = category.replace("\"", "").split(",");
					continue;
				}
			}
			DownloadObject p = new DownloadObject(name, folder.getName(), 
					FileTools.getFolderItemCount(folder, fileOnlyFilter), 
					FileTools.getFolderSize(folder, fileOnlyFilter), 
					DownloadObject.State.local);
			p.setCategories(Arrays.asList(categories));
			p.setLastUpdate(0);
			return p;
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public SharedPreferences getSharedPreferences() {
		SharedPreferences sharedPreferences = 
				getSharedPreferences("UpdateListTest", MODE_PRIVATE);
		return sharedPreferences;
	}
	
	public static FileFilter fileOnlyFilter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			String filename = pathname.getName().toLowerCase();
			return pathname.isFile() && (filename.endsWith(".gif") || filename.endsWith(".png") || filename.endsWith(".ini"));
		}
	}; 

	public static String[] splitWithQualifiers(String SourceText, 
			String TextDelimiter, String TextQualifier, String ClosingTextQualifier) {
		String[] strTemp;
		String[] strRes; int I; int J; String A; String B; boolean blnStart = false;
		B = "";

		if (TextDelimiter != " ") SourceText = SourceText.trim();
		if (ClosingTextQualifier.length() > 0) SourceText = SourceText.replace(ClosingTextQualifier, TextQualifier);
		strTemp = SourceText.split(TextDelimiter);
		for (I = 0; I < strTemp.length; I++) {
			J = strTemp[I].indexOf(TextQualifier, 0);
			if (J > -1) {
				A = strTemp[I].replace(TextQualifier, "").trim();
				String C = strTemp[I].replace(TextQualifier, "");
				if (strTemp[I].trim().equals(TextQualifier + A + TextQualifier)) {
					B = B + A + " \n";
					blnStart = false;
				} else if (strTemp[I].trim().equals(TextQualifier + C + TextQualifier)) {
					B = B + C + " \n";
					blnStart = false;
				} else if (strTemp[I].trim().equals(TextQualifier + A)) {
					B = B + A + TextDelimiter;
					blnStart = true;
				} else if (strTemp[I].trim().equals(A)) {
					B = B + A + TextDelimiter;
					blnStart = false;
				} else if (strTemp[I].trim().equals(A + TextQualifier)) {
					B = B + A + "\n";
					blnStart = false;
				}
			} else {
				if (blnStart)
					B = B + strTemp[I] + TextDelimiter;
				else
					B = B + strTemp[I] + "\n";
			}
		}
		if (B.length() > 0) {
			B = B.substring(0, B.length());
			strRes = B.split("\n");
		} else {
			strRes = new String[1];
			strRes[0] = SourceText;
		}	
		return strRes;
	}
}
