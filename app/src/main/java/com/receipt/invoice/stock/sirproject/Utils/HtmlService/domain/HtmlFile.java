package com.receipt.invoice.stock.sirproject.Utils.HtmlService.domain;

import java.io.File;

import com.receipt.invoice.stock.sirproject.Utils.HtmlService.provider.CacheFileProvider;

import android.net.Uri;

/**
 * This is a wrapper class for our 'File'
 * It lets us add methods and test the file without having to be locked to using java.io.File
 * @author paul.blundell
 */
public class HtmlFile {

	private final File file;

	public HtmlFile(File file) {
		this.file = file;
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
		return Uri.parse("content://"+ CacheFileProvider.AUTHORITY +"/"+ getFileName());
	}
}