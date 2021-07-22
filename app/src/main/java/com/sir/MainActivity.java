package com.sir;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.karan.churi.PermissionManager.PermissionManager;
import com.sir.Invoice.CheckForSDCard;
import com.sir.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    Button example2;

    PermissionManager permissionManager;
    DownloadManager downloadManager;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String url = "http://kskglobalsourcing.com/boost-crm/storage/files/shares/example.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        example2 = findViewById(R.id.example_2);
        example2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckForSDCard.isSDCardPresent()) {

                    //check if app has permission to write to the external storage.
                    if (checkPermission()) {
                        //Get the URL entered
                        new DownloadFile(MainActivity.this).execute(url);
                    } else {

                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "SD Card not found", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        Context context;

        DownloadFile(Context c) {
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
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "abir_download/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

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
                return "" + folder + fileName;

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

            // Display File path after downloading
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();




            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(message);
            Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                    "com.sir.provider",
                    fileWithinMyDir);

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File...");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }
        }
    }



}
