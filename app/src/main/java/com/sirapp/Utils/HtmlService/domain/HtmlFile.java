package com.sirapp.Utils.HtmlService.domain;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * This is a wrapper class for our 'File'
 * It lets us add methods and test the file without having to be locked to using java.io.File
 * @author paul.blundell
 */
public class HtmlFile {
	public static final String AUTHORITY = "com.sirapp.cacheFileProvider";

	String TAG = "HtmlFile";
	private final File file;

	private final Context context;

	public HtmlFile(File file, Context context) {
		this.file = file;
		this.context = context;
	}

	/**
	 * A convenience method to check that we are in a happy state
	 * @return
	 */
	public boolean isValid(){
		return this.file != null;
	}

	private String getFileName(){
		return file.getName();
	}

	/**
	 * @return a uri that is a pointer to the html file we have created - this can be used by content providers
	 */
	public Uri getFilePath(){
//		Log.e(TAG, "SSSSSS "+ "content://"+ CacheFileProvider.AUTHORITY +"/"+ getFileName());
//		return Uri.parse("content://"+ CacheFileProvider.AUTHORITY +"/"+ getFileName());




//		Uri photoURI = null;
//
//		File photoFile = null;
//		try {
//			photoFile = createImageFile();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//			// Error occurred while creating the File
//			Log.e(TAG, "photoFileErr "+ex.getMessage());
//		}
//		if (photoFile != null) {
//			photoURI = FileProvider.getUriForFile(context,
//					BuildConfig.APPLICATION_ID + ".provider",
//					photoFile);
//			Log.e(TAG, "SSSSSSphotoFile "+ photoURI);
//
//		}

		//return Uri.parse("content://"+BuildConfig.APPLICATION_ID + ".provider/"+ getFileName());


//		Uri photoURI = FileProvider.getUriForFile(getActivity(),
//				BuildConfig.APPLICATION_ID + ".provider",
//				getFileName());
//
		return Uri.parse("content://"+ AUTHORITY +"/"+ getFileName());

	}


	private File createImageFile() throws IOException {
		// Create an image file name
		//String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		//String mFileName = "JPEG_" + timeStamp + "_";
//		String fileLocation = context.getCacheDir() + ".dd.html";
//		File externallyVisibleFile = new File(fileLocation);
//
//
////
////		Log.e(TAG, "externallyVisibleFile "+externallyVisibleFile);
////		File downloadFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
////
////		downloadFilePath.mkdirs();
//
//		//File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//		File mFile = File.createTempFile("getFileName", ".html", externallyVisibleFile);
////		Log.e(TAG, "mFileAA "+mFile);


		File outputDir = context.getCacheDir(); // context being the Activity pointer
		File outputFile = File.createTempFile("prefix", null, outputDir);

		Log.e(TAG, "mFileAA "+outputFile);
		return outputFile;
	}
}