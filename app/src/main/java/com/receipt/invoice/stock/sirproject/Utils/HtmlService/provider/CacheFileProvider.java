package com.receipt.invoice.stock.sirproject.Utils.HtmlService.provider;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

/**
 * Thanks to Stephen Nicholas for the information to allow Gmail to view internal app data
 * http://stephendnicholas.com/archives/974
 *
 * @author paul.blundell
 */
public class CacheFileProvider extends ContentProvider {

	private static final int A_MATCH = 1;

	public static final String AUTHORITY = "com.receipt.invoice.stock.sirproject.cacheFileProvider";

	private UriMatcher uriMatcher;

	@Override
	public boolean onCreate() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(AUTHORITY, "*", A_MATCH);

		return true;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		if(uriMatcher.match(uri) == A_MATCH){
			String fileLocation = getContext().getCacheDir() + File.separator + uri.getLastPathSegment();
			File externallyVisibleFile = new File(fileLocation);
			ParcelFileDescriptor pfd = ParcelFileDescriptor.open(externallyVisibleFile, ParcelFileDescriptor.MODE_READ_ONLY);

			return pfd;
		}
		return super.openFile(uri, mode);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
}