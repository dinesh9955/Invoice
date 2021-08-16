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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//
//import com.itextpdf.text.pdf.PdfWriter;

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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import io.github.lucasfsc.html2pdf.Html2Pdf;

//import com.lowagie.text.Font;
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

import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class Abc extends BaseActivity {

    private static final String TAG = "Abc";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

     //   textView = findViewById(R.id.textView3);

        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("content://com.sirapp/file:///android_asset/po.html");

                String string = "<table border='1' align='center'><tr style='color:blue'><th>Day</th><th>Date</th><th>Start Time</th><th>End Time</th><th>Total Time</th></tr><tr><td align='center'>Sunday</td><td align='center'>19/07/2011</td><td align='center'>13:00</td><td align='center'>19:00</td><td align='center'>06:00</td></tr></table>";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");

                intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(string));

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(intent);


//                Intent sendIntent = new Intent(Intent.ACTION_SEND);
//             //   sendIntent.setType("text/html");
//                sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml("<p><b>Some Content</b></p>" +
//                        "http://www.google.com" + "<small><p>More content</p></small>"));
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"test subject");
//                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///android_asset/po.html"));
//                startActivity(Intent.createChooser(sendIntent, "Send email..."));

//                onemonth, onemonth_add, oneyear, oneyear_add


            }
        });
    }
}