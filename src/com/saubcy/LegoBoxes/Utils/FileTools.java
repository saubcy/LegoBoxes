package com.saubcy.LegoBoxes.Utils;

import java.io.File;
import java.io.FileFilter;

import android.os.Environment;

public class FileTools {
	
	public static boolean isSDMounted(){
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	public static long getFolderSize(File folder, 
			FileFilter fileOnlyFilter){
		File[] files = folder.listFiles(fileOnlyFilter);
		int size = 0;
		for(File f : files){
			size += f.length();
		}
		return size;
	}
	
	public static int getFolderItemCount(File folder, 
			FileFilter fileOnlyFilter) {
		File[] files = folder.listFiles(fileOnlyFilter);
		return files.length;
	}
	
	public static String formatBytes(float bytes) {
	    String units[] = {"B", "KB", "MB", "GB", "TB"};
	  
	    bytes = Math.max(bytes, 0);
	    int pow = (int) Math.floor(((bytes != 0) ? Math.log(bytes) : 0) / Math.log(1024));
	    pow = Math.min(pow, units.length - 1);
	  
	    bytes /= Math.pow(1024, pow);
	    
	    return  String.format("%.2f", bytes) + ' ' + units[pow];
	}
}
