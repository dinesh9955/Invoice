package com.sirapp;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
//import android.print.PdfConverter;
//import android.print.PdfConverter;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//
//import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import io.github.lucasfsc.html2pdf.Html2Pdf;

//import com.lowagie.text.Font;
import com.google.gson.GsonBuilder;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.POJO.Invoice.Root;
import com.sirapp.RetrofitApi.APIClient;
import com.sirapp.RetrofitApi.ApiInterface;
import com.sirapp.Utils.HtmlService.domain.HtmlFile;
import com.sirapp.Utils.HtmlService.task.CreateHtmlTask;
import com.sirapp.Utils.SublimePickerFragment;
import com.sirapp.R;
import com.sirapp.Utils.Utility;
//import com.tejpratapsingh.pdfcreator.utils.FileManager;

//import net.roganjosh.mailcraft.ComposeActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class Abc extends BaseActivity {

    private static final String TAG = "Abc";

    public GoogleSignInClient mGoogleSignInClient = null;
    public GoogleSignInOptions gso;

    public int RC_SIGN_IN = 120;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);


        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();


                mGoogleSignInClient = GoogleSignIn.getClient(Abc.this, gso);

                signOut();


                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

//                Uri uri = Uri.parse("content://com.sirapp/file:///android_asset/po.html");
//
//                String string = "<table border='1' align='center'><tr style='color:blue'><th>Day</th><th>Date</th><th>Start Time</th><th>End Time</th><th>Total Time</th></tr><tr><td align='center'>Sunday</td><td align='center'>19/07/2011</td><td align='center'>13:00</td><td align='center'>19:00</td><td align='center'>06:00</td></tr></table>";
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("*/*");
//
//                intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
//                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(string));
//
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//
//                startActivity(intent);


//                Intent sendIntent = new Intent(Intent.ACTION_SEND);
//             //   sendIntent.setType("text/html");
//                sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml("<p><b>Some Content</b></p>" +
//                        "http://www.google.com" + "<small><p>More content</p></small>"));
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"test subject");
//                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///android_asset/po.html"));
//                startActivity(Intent.createChooser(sendIntent, "Send email..."));

//                onemonth, onemonth_add, oneyear, oneyear_add


//                String html = "<html><body><b>bold</b><u>underline</u></body></html>";
//                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
//                intent.setType("text/html");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                intent.putExtra(Intent.EXTRA_TEXT,
//                        Html.fromHtml(html));
//                startActivity(Intent.createChooser(intent, "Send Email"));



//                String fileName = "image1.jpg";//Name of an image
//                String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
//                String myDir = externalStorageDirectory + "/"; // the file will be in saved_images
//                Uri uri = Uri.parse("file:///" + myDir + fileName);
//                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                shareIntent.setType("text/html");
//                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Launcher");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                startActivity(Intent.createChooser(shareIntent, "Share Deal"));

//                ShareCompat.IntentBuilder.from(Abc.this)
//                        .setType("text/html")
//                        .setChooserTitle("Share URL")
//                        .setText(Html.fromHtml(html))
//                        .startChooser();


//                String body = "<!DOCTYPE html><html><body><a href=\"http://www.w3schools.com\" target=\"_blank\">Visit W3Schools.com!</a></body></html>";
//
//                ShareCompat.IntentBuilder.from(Abc.this)
//                        //.setType("message/rfc822")
//                        .setType("text/html")
//                        .setSubject("test")
//                        .setHtmlText(body)
//                        .setChooserTitle("Send Email")
//                        .startChooser();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", "3");
                    obj.put("name", "NAME OF STUDENT");
                    obj.put("year", "3rd");
                    obj.put("curriculum", "Arts");
                    obj.put("birthday", "5/5/1993");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



               // generateNoteOnSD(Abc.this , "info.txt", ""+obj.toString());

            }
        });





    }



//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void signOut() {
        if(mGoogleSignInClient != null){
            mGoogleSignInClient.signOut();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.e(TAG, "firebaseAuthWithGoogle:" + task.isSuccessful());

            handleGoogleSignin(task);

        }
    }

    private void handleGoogleSignin(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.e(TAG, "firebaseAuthWithGoogle:" + account.getId());

//            idOb.set(account.id)
//            firstnameOb.set(account.givenName)
//            lastNameOb.set(account.familyName)
//            emailOb.set(account.email)
//
//            Log.e(TAG, "gt" + idOb.get().toString())
//            Log.e(TAG, "gt" + firstnameOb.get().toString())
//            Log.e(TAG, "gt" + lastNameOb.get().toString())
//            Log.e(TAG, "gt" + emailOb.get().toString())
//
            callSocialLoginApi(account, "google");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callSocialLoginApi(GoogleSignInAccount account, String google) {
    }


}