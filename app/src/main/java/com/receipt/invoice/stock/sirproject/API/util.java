package com.receipt.invoice.stock.sirproject.API;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.receipt.invoice.stock.sirproject.R;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class util

 {

     public static String BaseImageURL = "http://3.216.74.11/Tranzsafe/public";
    public static String API_KEY =
            "AIzaSyAMwlXkuKua0YDoenEK7-PUraTLlQnh6jE";
    public static String Internet_Check = "Please Check Your Internet Check Connection";
    public static String PAD_PAYMENT = "Please add payment";
    public static File myDir_images_sent = new File(Environment.getExternalStoragePublicDirectory("IDeliverIt"), "Images/Sent");


    private util() {

    }

    public static String decode_string_base64(String string) {
        String decoded_string = "";
        byte[] data = Base64.decode(string, Base64.DEFAULT);
        try {
            decoded_string = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decoded_string;
    }

    public static void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    public static void showSnackBar(View mParentview, String message, Context context) {

        Snackbar snackbar = Snackbar.make(mParentview, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();

    }

    public static String internet_Connection_Error = "Please Check Your Internet Connection";
    public static String message = "";

    public static void hideKeyboard(Activity context)

    {
        View view = context.getCurrentFocus();

        if (view != null) {

            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static File getOutputMediaFile(int type, String file_name) {

        File mediaFile;
        mediaFile = new File(myDir_images_sent, file_name + ".jpg");

        return mediaFile;

    }

    public static Uri getOutputMediaFileUri(File file) {

        return Uri.fromFile(file);

    }

    public static String getAbsolutePath(Context activity, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {

                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getPath(final Context context, final Uri uri)

    {

        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video1".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;

        } else {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static void Message(final Activity activity, String message, final int type){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);

        } else {

            builder = new AlertDialog.Builder(activity);
        }

        builder.setTitle(activity.getString(R.string.connection_not_found))
                .setMessage(message)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//                    }
//                })
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // do nothing
                    }
                })

                .show();

//        AlertDialog alert = builder.create();
//        alert.show();
    }
}
