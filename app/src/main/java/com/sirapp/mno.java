package com.sirapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.Gson;
import com.sirapp.API.AllSirApi;
import com.sirapp.Invoice.List_of_Invoices;
import com.sirapp.R;
import com.sirapp.Utils.Utility;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class mno extends AppCompatActivity {
    private static final String TAG = "mno";
//    private BillingProcessor bp;

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mno);

        Button button = (Button) findViewById(R.id.button3);


        if (shouldAskPermissions()) {
            askPermissions();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //deleteTempFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/AndroidDvlpr").toString()+"");

//                final String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/AndroidDvlpr").toString();
//                File fdelete = new File(destination, "Invoice.pdf");
//                Log.e(TAG, "deleteddeleted "+fdelete);
////
//                if (fdelete.exists()) {
//                    fdelete.delete();
//                }
//
//                File fdeleteAA = new File(destination);
//                fdeleteAA.delete();


               // File source_f = new File(sourcepath);

//                String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/AndroidDvlpr2").toString();
//                File destination22 = new File(destinationPath);
//                try
//                {
//                    FileUtils.copyFile(fdelete , destination22);
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }


//                File dir = getFilesDir();
//                File file = new File(dir, destination);
//                Log.e(TAG, "deleteddeleted "+file);
//
//                boolean deleted = file.delete();

//                String pathToExternalStorage = Environment.getExternalStorageDirectory().toString();
//                File appDirectory = new File(pathToExternalStorage + "/" + "secretVideos");



//                final String destination = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + "Invoice.pdf";
//                final Uri uri = Uri.parse("file://" + destination);
//                File fdelete = new File(uri.getPath());
//                //final File file = new File(destination);
//                if (fdelete.exists()) {
//                    fdelete.delete();
//                }

              //  File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/AndroidDvlpr"); //Creates app specific folder
//
//                File file = new File(path, "Invoice.pdf");
//
//             //   File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), MEDIA_NAME);
//                File from      = new File(path, "Invoice.pdf");
//                File to        = new File(path, "Invoice1.pdf");
//                from.renameTo(to);

//                Log.e(TAG, "filefile "+file.toString());
//
//                boolean deleted = file.delete();
//
//                Log.e(TAG, "deleteddeleted "+deleted);


//                File root1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                File gpxfile2 = new File(root1, "Invoice.pdf");
//                Uri imageUri2 = FileProvider.getUriForFile(mno.this, BuildConfig.APPLICATION_ID + ".provider", gpxfile2);
//
//                ContentResolver contentResolver = getContentResolver();
//                contentResolver.delete(imageUri2, null, null);
//
//                getContentResolver().delete(imageUri2, null, null);

//                getApplicationContext().deleteFile(""+gpxfile2);

//
                new DownloadFileAttach(mno.this).execute("http://sir-app.com/app/uploads/invoice/pdf/60f4752521966.pdf");
//
//                String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/";
//
////                File directory = new File(folder);
////                if (!directory.exists()) {
////                    directory.mkdirs();
////                }
//
////                File dir = new File(folder);
////                if (dir.isDirectory())
////                {
////                    String[] children = dir.list();
////                    for (int i = 0; i < children.length; i++)
////                    {
////                        new File(dir, children[i]).delete();
////                    }
////                }
//
////                Log.e(TAG, "folderfolderfolder "+folder);
////                //Create androiddeft folder if it does not exist
////                File directory2 = new File(folder+"Invoice.pdf");
////
////                if (directory2.exists()) {
////                    directory2.delete();
////                }
//
//                Log.e(TAG, "folderfolderfolder "+folder+"Invoice.pdf");
//                deleteFiles(folder+"Invoice.pdf");
//
//                File root1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                File gpxfile2 = new File(root1, "Invoice.pdf");
//                Uri imageUri2 = FileProvider.getUriForFile(mno.this, BuildConfig.APPLICATION_ID + ".provider", gpxfile2);
//
//
//                File fdelete = new File(imageUri2.getPath());
//                if (fdelete.exists()) {
//                    if (fdelete.delete()) {
//                        System.out.println("file Deleted :" + imageUri2.getPath());
//                    } else {
//                        System.out.println("file not Deleted :" + imageUri2.getPath());
//                    }
//                }
//
            }
        });




    }


    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }



    private static class DownloadFileAttach extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
//        private String fileName;
        private String folder;
        private boolean isDownloaded;
        Activity context;


        DownloadFileAttach(Activity c) {
            context = c;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {



//            try
//            {
//                URL url = new URL(f_url[0]);
//                HttpURLConnection c = (HttpURLConnection) url.openConnection();
//                c.setRequestMethod("GET");
//                c.setDoOutput(true);
//                c.connect();
//
//                String PATH = Environment.getExternalStorageDirectory().toString()
//                        + "/load";
//                Log.e("LOG_TAG", "PATH: " + PATH);
//
//                File file = new File(PATH);
//                file.mkdirs();
//                File outputFile = new File(file, "Invoice.pdf");
//                FileOutputStream fos = new FileOutputStream(outputFile);
//                InputStream is = c.getInputStream();
//
//                byte[] buffer = new byte[4096];
//                int len1 = 0;
//
//                while ((len1 = is.read(buffer)) != -1)
//                {
//                    fos.write(buffer, 0, len1);
//                }
//
//                fos.close();
//                is.close();
//
//                Toast.makeText(context, "A new file is downloaded successfull", Toast.LENGTH_SHORT).show();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
            
            
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

//                print_https_cert(connection);
//
//                //dump all the content
//                print_content(connection);

                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
//                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);

                //Append timestamp to file name
          //      fileName = timestamp + "_" + fileName;

                //External directory path to save file
                 // folder = Environment.getExternalStorageDirectory() + File.separator + "Download/";
            //    folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/";
                //folder = Environment.getDownloadCacheDirectory().toString()+"/";



                folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/AndroidDvlpr/"+timestamp).toString()+"/";
             //   folder = "/sdcard/ssss/";
                File directory = new File(folder);
                if (!directory.exists()) {
                    directory.mkdirs();
                }



                Log.e(TAG, "folderfolderfolder "+folder);
                //Create androiddeft folder if it does not exist
                 File directory2 = new File(folder+"Invoice.pdf");

                if (directory2.exists()) {
                    directory2.delete();
                }




//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//              //  myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                File wallpaperDirectory =new  File(Environment.getDownloadCacheDirectory().toString() + "/saad");
//                if (!wallpaperDirectory.exists()) {
//                    wallpaperDirectory.mkdirs();
//                }
//                try {
//                    File f = new File(wallpaperDirectory, ((Calendar.getInstance(Locale.ENGLISH).getTimeInMillis()) + ".pdf"));
//                    f.createNewFile();
//                    FileOutputStream fo =new  FileOutputStream(f);
//                    fo.write(bytes.toByteArray());
//                    //MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null);
//                    fo.close();
//                    return f.getAbsolutePath();
//
//                } catch ( IOException  io) {
//                    io.printStackTrace();
//                }


                String newFileName = "Invoice.pdf";
                // Output stream to write file

                OutputStream output = new FileOutputStream(folder + newFileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "" + folder + newFileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
        }
//
//            String urlPaypalName = "";
//            String urlStripeName = "";
//
//            // String filterPage = "";
//
//            if (paypal.equalsIgnoreCase("1")) {
//                urlPaypalName = "paypal";
//            }
//
//            if (stripe.equalsIgnoreCase("1")) {
//                urlStripeName = "stripe";
//            }
//
//            Log.e(TAG, "textXX " + text);
//            Log.e(TAG, "paypalXX " + urlPaypalName);
//            Log.e(TAG, "stripeXX " + urlStripeName);
//
//            String urlPaypal = AllSirApi.BASE + "view/" + urlPaypalName + "/" + link;
//            String urlStripe = AllSirApi.BASE + "view/" + urlStripeName + "/" + link;
//
//
//            // String urlStripeNameFilter = urlStripeName;
//
//
////            File mFile2 = new File("/sdcard/share.jpg");
////            Uri imageUri2 = FileProvider.getUriForFile(
////                    context,
////                    BuildConfig.APPLICATION_ID + ".provider",
////                    mFile2);
//
//            // File root1 = new File(Environment.getExternalStorageDirectory(), "Download/");
//            File root1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//            File gpxfile2 = new File(root1, "share.jpg");
//            Uri imageUri2 = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", gpxfile2);
//
//            File fileWithinMyDir = new File(message);
//            Uri imageUri1 = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
//
//            Log.e(TAG, "imageUri2 "+imageUri2);
//            Log.e(TAG, "imageUri1 "+imageUri1);
//
//            String finalUrlPaypalName = urlPaypalName;
//            String finalUrlStripeName = urlStripeName;
//
//
//            Log.e(TAG, "paypalXX8 ");
//
//
//
//
//
//            String content1 = "";
//            String content11 = "";
//
//            if (finalUrlPaypalName.equalsIgnoreCase("Paypal")) {
//                String linkWitch = "PayPal";
//                content1 = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "  <head>\n" +
//                        "    <title>Title of the document</title>\n" +
//                        "    <style>\n" +
//                        "      .button {\n" +
//                        "        background-color: #1c87c9;\n" +
//                        "        border: none;\n" +
//                        "        color: white;\n" +
//                        "        padding: 20px 34px;\n" +
//                        "        text-align: center;\n" +
//                        "        text-decoration: none;\n" +
//                        "        display: inline-block;\n" +
//                        "        font-size: 20px;\n" +
//                        "        margin: 4px 2px;\n" +
//                        "        cursor: pointer;\n" +
//                        "      }\n" +
//                        "    </style>\n" +
//                        "  </head>\n" +
//                        "  <body>\n" +
//                        "    <a href=\"" + urlPaypal + "\" class=\"button\">" + linkWitch + "</a>\n" +
//                        "  </body>\n" +
//                        "</html>";
//
//
//                content11 = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "  <head>\n" +
//                        "    <title>Title of the document</title>\n" +
//                        "    <style>\n" +
//                        "      .button {\n" +
//                        "        background-color: #1c87c9;\n" +
//                        "        border: none;\n" +
//                        "        color: white;\n" +
//                        "        padding: 60px 90px;\n" +
//                        "        text-align: center;\n" +
//                        "        text-decoration: none;\n" +
//                        "        display: inline-block;\n" +
//                        "        font-size: 45px;\n" +
//                        "        margin: 4px 2px;\n" +
//                        "        cursor: pointer;\n" +
//                        "        border-radius: 10px;\n" +
//                        "      }\n" +
//
//
//                        "     html, body {\n" +
//                        "      height: 100%;\n" +
//                        "     }\n" +
//
//
//                        "      .parent {\n" +
//                        "        width: 100%;\n" +
//                        "        height: 100%;\n" +
//                        "        display: table;\n" +
//                        "        text-align: center;\n" +
//                        "     }\n" +
//
//
//                        " 		.parent > .child {\n" +
//                        "		display: table-cell;\n" +
//                        "		vertical-align: middle;\n" +
//                        "     }\n" +
//
//                        "    </style>\n" +
//                        "  </head>\n" +
//                        "  <body>\n" +
//                        "  <section class=\"parent\">\n" +
//                        "	 <div class=\"child\">\n" +
//                        "      <a href=\""+urlPaypal+"\" class=\"button\">"+linkWitch+"</a>\n" +
//                        "	 </div>\n" +
//                        "	</section>\n" +
//                        "  </body>\n" +
//                        "</html>";
//
//                generateNoteOnSDPayPal(context , linkWitch+".html" , ""+content11.toString());
//
//                Log.e(TAG, "paypalXX1 " + urlPaypalName);
//
//            }
//
//            String content2 = "";
//            String content22 = "";
//
//            if (finalUrlStripeName.equalsIgnoreCase("Stripe")) {
//                String linkWitch = "Cards";
//                content2 = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "  <head>\n" +
//                        "    <title>Title of the document</title>\n" +
//                        "    <style>\n" +
//                        "      .button {\n" +
//                        "        background-color: #1c87c9;\n" +
//                        "        border: none;\n" +
//                        "        color: white;\n" +
//                        "        padding: 20px 34px;\n" +
//                        "        text-align: center;\n" +
//                        "        text-decoration: none;\n" +
//                        "        display: inline-block;\n" +
//                        "        font-size: 20px;\n" +
//                        "        margin: 4px 2px;\n" +
//                        "        cursor: pointer;\n" +
//                        "      }\n" +
//                        "    </style>\n" +
//                        "  </head>\n" +
//                        "  <body>\n" +
//                        "    <a href=\"" + urlStripe + "\" class=\"button\">" + linkWitch + "</a>\n" +
//                        "  </body>\n" +
//                        "</html>";
//
//                content22 = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "  <head>\n" +
//                        "    <title>Title of the document</title>\n" +
//                        "    <style>\n" +
//                        "      .button {\n" +
//                        "        background-color: #1c87c9;\n" +
//                        "        border: none;\n" +
//                        "        color: white;\n" +
//                        "        padding: 60px 90px;\n" +
//                        "        text-align: center;\n" +
//                        "        text-decoration: none;\n" +
//                        "        display: inline-block;\n" +
//                        "        font-size: 45px;\n" +
//                        "        margin: 4px 2px;\n" +
//                        "        cursor: pointer;\n" +
//                        "        border-radius: 10px;\n" +
//                        "      }\n" +
//
//
//                        "     html, body {\n" +
//                        "      height: 100%;\n" +
//                        "     }\n" +
//
//
//                        "      .parent {\n" +
//                        "        width: 100%;\n" +
//                        "        height: 100%;\n" +
//                        "        display: table;\n" +
//                        "        text-align: center;\n" +
//                        "     }\n" +
//
//
//                        " 		.parent > .child {\n" +
//                        "		display: table-cell;\n" +
//                        "		vertical-align: middle;\n" +
//                        "     }\n" +
//
//                        "    </style>\n" +
//                        "  </head>\n" +
//                        "  <body>\n" +
//                        "  <section class=\"parent\">\n" +
//                        "	 <div class=\"child\">\n" +
//                        "      <a href=\""+urlStripe+"\" class=\"button\">"+linkWitch+"</a>\n" +
//                        "	 </div>\n" +
//                        "	</section>\n" +
//                        "  </body>\n" +
//                        "</html>";
//                generateNoteOnSDStripe(context , linkWitch+".html" , ""+content22.toString());
//                Log.e(TAG, "paypalXX2 " + urlPaypalName);
//            }
//
//
//            Log.e(TAG, "paypalXX5 ");
//
//            ArrayList<Uri> uriArrayList = new ArrayList<>();
//
//
//            if (Utility.isAppAvailable(context, "com.samsung.android.email.provider")) {
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                uriArrayList.add(imageUri1);
//                uriArrayList.add(imageUri2);
//                Collections.reverse(uriArrayList);
//
//                intentShareFile.setType("application/pdf/*|image/*");
//                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
//                Log.e(TAG, "allData "+text);
//                String allData = text+content1+content2;
//                intentShareFile.putExtra(Intent.EXTRA_HTML_TEXT, allData);
//                intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
//                intentShareFile.setPackage("com.samsung.android.email.provider");
//                Log.e(TAG, "paypalXX9 ");
//                try{
//                    context.startActivity(intentShareFile);
//                    Log.e(TAG, "paypalXX71 ");
//                }catch (Exception e){
//                    Log.e(TAG, "paypalXX31 " + e.getMessage());
//                }
//            }else if (Utility.isAppAvailable(context, "com.google.android.gms")) {
//                Log.e(TAG, "paypalXX101 ");
//                if (paypal.equalsIgnoreCase("1")) {
//                    // File root = new File(Environment.getExternalStorageDirectory(), "Download/");
//                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File gpxfile = new File(root, "PayPal.html");
//
//                    // File mFile22 = new File("/sdcard/SIR/PayPal.html");
//                    Uri imageUri22 = FileProvider.getUriForFile(
//                            context,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            gpxfile);
//                    uriArrayList.add(imageUri22);
////                    intentShareFile.putExtra(Intent.EXTRA_STREAM,
////                            Uri.parse("file:///sdcard/Notes/PayPal.html"));
//                }
//                if (stripe.equalsIgnoreCase("1")) {
////                    File mFile22 = new File("/sdcard/SIR/Cards.html");
//                    //  File root = new File(Environment.getExternalStorageDirectory(), "Download/");
//                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File gpxfile = new File(root, "Cards.html");
//                    Uri imageUri22 = FileProvider.getUriForFile(
//                            context,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            gpxfile);
//                    uriArrayList.add(imageUri22);
////                    intentShareFile.putExtra(Intent.EXTRA_STREAM,
////                            Uri.parse("file:///sdcard/Notes/Cards.html"));
//                }
//
//                uriArrayList.add(imageUri1);
//                uriArrayList.add(imageUri2);
//                Collections.reverse(uriArrayList);
//
//
//                //intentShareFile.setType("*application/octet-stream*");
//                // intentShareFile.setType("application/pdf/*|image/*|text/html");
////                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                intentShareFile.setType("*/*");
//                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intentShareFile.putExtra(Intent.EXTRA_TEXT, text2);
//                intentShareFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
//
//
//                intentShareFile.setPackage("com.google.android.gm");
//
//                Log.e(TAG, "paypalXX10 ");
//                try {
//                    context.startActivity(intentShareFile);
//                    Log.e(TAG, "paypalXX72 ");
//                } catch (Exception e) {
//                    Log.e(TAG, "paypalXX32 " + e.getMessage());
//                }
//
//            }
//            else if (Utility.isAppAvailable(context, "com.android.email")) {
//                if (paypal.equalsIgnoreCase("1")) {
//                    //File root = new File(Environment.getExternalStorageDirectory(), "Download/");
//                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File gpxfile = new File(root, "PayPal.html");
//
//                    // File mFile22 = new File("/sdcard/SIR/PayPal.html");
//                    Uri imageUri22 = FileProvider.getUriForFile(
//                            context,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            gpxfile);
//                    uriArrayList.add(imageUri22);
////                    intentShareFile.putExtra(Intent.EXTRA_STREAM,
////                            Uri.parse("file:///sdcard/Notes/PayPal.html"));
//                }
//                if (stripe.equalsIgnoreCase("1")) {
////                    File mFile22 = new File("/sdcard/SIR/Cards.html");
//                    //File root = new File(Environment.getExternalStorageDirectory(), "Download/");
//                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File gpxfile = new File(root, "Cards.html");
//                    Uri imageUri22 = FileProvider.getUriForFile(
//                            context,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            gpxfile);
//                    uriArrayList.add(imageUri22);
////                    intentShareFile.putExtra(Intent.EXTRA_STREAM,
////                            Uri.parse("file:///sdcard/Notes/Cards.html"));
//                }
//
//                uriArrayList.add(imageUri1);
//                uriArrayList.add(imageUri2);
//                Collections.reverse(uriArrayList);
//
//
//                //intentShareFile.setType("*application/octet-stream*");
//                // intentShareFile.setType("application/pdf/*|image/*|text/html");
////                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                intentShareFile.setType("*/*");
//                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intentShareFile.putExtra(Intent.EXTRA_TEXT, text2);
//                //    intentShareFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
//
//
//                intentShareFile.setPackage("com.android.email");
//                Log.e(TAG, "paypalXX10 ");
//                try {
//                    context.startActivity(intentShareFile);
//                    Log.e(TAG, "paypalXX72 ");
//                } catch (Exception e) {
//                    Log.e(TAG, "paypalXX32 " + e.getMessage());
//                }
//
//            }else{
//                if (paypal.equalsIgnoreCase("1")) {
//                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File gpxfile = new File(root, "PayPal.html");
//
//                    // File mFile22 = new File("/sdcard/SIR/PayPal.html");
//                    Uri imageUri22 = FileProvider.getUriForFile(
//                            context,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            gpxfile);
//                    uriArrayList.add(imageUri22);
////                    intentShareFile.putExtra(Intent.EXTRA_STREAM,
////                            Uri.parse("file:///sdcard/Notes/PayPal.html"));
//                }
//                if (stripe.equalsIgnoreCase("1")) {
////                    File mFile22 = new File("/sdcard/SIR/Cards.html");
//                    //File root = new File(Environment.getExternalStorageDirectory(), "Download/");
//                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File gpxfile = new File(root, "Cards.html");
//                    Uri imageUri22 = FileProvider.getUriForFile(
//                            context,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            gpxfile);
//                    uriArrayList.add(imageUri22);
////                    intentShareFile.putExtra(Intent.EXTRA_STREAM,
////                            Uri.parse("file:///sdcard/Notes/Cards.html"));
//                }
//
//                uriArrayList.add(imageUri1);
//                uriArrayList.add(imageUri2);
//                Collections.reverse(uriArrayList);
//
//
//                //intentShareFile.setType("*application/octet-stream*");
//                // intentShareFile.setType("application/pdf/*|image/*|text/html");
////                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                intentShareFile.setType("*/*");
//                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intentShareFile.putExtra(Intent.EXTRA_TEXT, text2);
//                //   intentShareFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
//
//
//                final PackageManager pm = context.getPackageManager();
//                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//                for (ApplicationInfo packageInfo : packages) {
//                    String ccc = new Gson().toJson(packageInfo);
//                    // Log.e(TAG, "Source_dir : " + ccc);
//                    if(ccc.toLowerCase().contains("mail")){
////                        Log.e(TAG, "LaunchActivity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
////                        Log.e(TAG, "Installedpackage :" + packageInfo.packageName);
////                        Log.e(TAG, "paypalXX10 ");
//                        try {
//                            intentShareFile.setPackage(""+packageInfo.packageName);
//                            context.startActivity(intentShareFile);
//                            Log.e(TAG, "paypalXX72 ");
//                        } catch (Exception e) {
//                            Log.e(TAG, "paypalXX32 " + e.getMessage());
//                        }
//                        return;
//                    }
//                }
//
//
//            }
//
//            Log.e(TAG, "paypalXX6 ");
//
//        }


    }




}
