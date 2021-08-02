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
//import com.tejpratapsingh.pdfcreator.utils.FileManager;

//import net.roganjosh.mailcraft.ComposeActivity;

import org.apache.commons.io.IOUtils;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class Abc extends BaseActivity implements CreateHtmlTask.OnTaskFinishedListener {

    FirebaseAnalytics firebaseAnalytics;
    private static final String TAG = "Abc";
    private ProgressDialog pd;

    TextView textView;
    ApiInterface apiInterface;

    private void sendMail(String appName, String playStoreLink) {
        String msg = "<HTML><BODY>Hello,<br>Recently,I downloaded <b><font color=\"red\">"+appName+"</font></b>"+
                " from Play Store.I found this very challenging and a great game."+
                "<br>I would like to suggest you this game.<br><br><a href="+playStoreLink+">Download</a><br><br>"+
                "<br>Thank You</BODY></HTML>";
        String sub = "Get it now. It is there in Play Store";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/html");
        email.putExtra(Intent.EXTRA_SUBJECT, sub);
        email.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(msg));
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

        textView = findViewById(R.id.textView3);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        Button button = (Button) findViewById(R.id.button2);
        firebaseAnalytics = FirebaseAnalytics.getInstance(Abc.this);

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
       // materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setTheme(R.style.AppTheme2);
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

//        materialDatePicker.

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        button.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dialog
                    }
                });

        //String value = "<html>Visit my blog <a href=\"http://www.maxartists.com\">mysite</a> View <a href=\"sherif-activity://myactivity?author=sherif&nick=king\">myactivity</a> callback</html>";
//        textView.setText("This is link https://www.google.co.in/");
        //textView.setText(Html.fromHtml(value));
        //textView.setMovementMethod(LinkMovementMethod.getInstance());

        String value = "<html>There is no upfront or monthly fee. There is a charge per transaction, which varies by country. To check the charges in your country, please visit <a href=\"https://www.stripe.com/us/pricing\">Stripe pricing</a> or  <a href=\"https://www.paypal.com/us/webapps/mpp/merchant-fees\">https://www.paypal.com/us/webapps/mpp/merchant-fees</a></html>";
//        textView.setText("<html>There is no upfront or monthly fee. There is a charge per transaction, which varies by country. To check the charges in your country, please visit <a href=\"http://www.maxartists.com\">mysite</a>Stripe pricing or https://www.paypal.com/us/webapps/mpp/merchant-fees</html>");
        textView.setText(Html.fromHtml(value));
        textView.setMovementMethod(LinkMovementMethod.getInstance());




        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                String html = "<!DOCTYPE html><html><body><a href=\"http://www.w3schools.com\" target=\"_blank\">Visit W3Schools.com!</a>" + "<p>If you set the target attribute to \"_blank\", the link will open in a new browser window/tab.</p></body></html>";


//                final Intent emailIntent2 = new Intent(android.content.Intent.ACTION_SEND);
//                emailIntent2.setType("text/html");
//                emailIntent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject");
//                emailIntent2.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(html));
//                startActivity(Intent.createChooser(emailIntent2, "Email:"));

                Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.setType("plain/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"abc@gmailcom"});
//                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(html));

                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<html><body><br/> <a href=\"http://www.facebook.com\"> Facebook</a><br/></body><html>", HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
                intent.putExtra(Intent.EXTRA_HTML_TEXT, "<html><body><br/> <a href=\"http://www.facebook.com\"> Facebook</a><br/></body><html>");
                intent.setType("text/html");

                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);


//                Intent intent = new Intent(Abc.this, ComposeActivity.class);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                intent.putExtra(Intent.EXTRA_TEXT, "Text content");
//                intent.putExtra(Intent.EXTRA_HTML_TEXT, "<h1>HTML content</h1>");
//                intent.putStringArrayListExtra(Intent.EXTRA_EMAIL, new ArrayList<>(Arrays.asList("dinesh.kumar@apptunix.com")));
//                startActivity(intent);


              //  String token = Constant.GetSharedPreferences(Abc.this, Constant.ACCESS_TOKEN);
//                Call<InvoiceResponseDto> resposresult = apiInterface.getInvoiceDetail(token, "2393", "english");
//
//                resposresult.enqueue(new Callback<InvoiceResponseDto>() {
//                    @Override
//                    public void onResponse(Call<InvoiceResponseDto> call, Response<InvoiceResponseDto> response) {
//
//                        Gson gson = new Gson();
//                        String json = gson.toJson(response);
//
//                        Log.e(TAG, "onResponse:: "+json);
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<InvoiceResponseDto> call, Throwable t) {
//                        Log.e(TAG, "onFailure:: "+t.getMessage());
//                    }
//                });
//
//
//
//                Call<ResponseBody> call = apiInterface.getInvoiceDetail2(token, "2393", "english");
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                        String responseCode = "";
//                        try {
//                            if(response.body() != null) {
//                                responseCode = response.body().string();
//                                Log.e(TAG, "onResponse:: "+responseCode);
//
//                                Gson g = new Gson();
//                                Root p = g.fromJson(responseCode, Root.class);
//
//                                Log.e(TAG, "onResponse:: "+p.data.invoice.company_id);
//
//
//                            }else{
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "onFailure:: "+t.getMessage());
//                    }
//                });


//                Call<InvoiceResponseDto> call2 = RetrofitClient.getInstance().getMyApi().getInvoiceDetail(token, "2393", "english");
//
//                call2.enqueue(new Callback<InvoiceResponseDto>() {
//                    @Override
//                    public void onResponse(Call<InvoiceResponseDto> call, Response<InvoiceResponseDto> response) {
//
//                        //In this point we got our hero list
//                        //thats damn easy right ;)
//                     //   InvoiceResponseDto heroList = response.body();
//
//                        Log.e(TAG, "onResponse2:: "+response.body().toString());
//
//                        //now we can do whatever we want with this list
//                    }
//
//                    @Override
//                    public void onFailure(Call<InvoiceResponseDto> call, Throwable t) {
//                        //handle error or failure cases here
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });








                String name = "debit.html";
                String nameName = "file:///android_asset/debit.html";

                String contentAA = null;
                try {
                    contentAA = IOUtils.toString(Abc.this.getAssets().open(name));

                } catch (IOException e) {
                    e.printStackTrace();

                }


                //String name = params[0];
                StringBuilder builder = new StringBuilder();

                builder.append("<!DOCTYPE html>");
                builder.append("<html>");
                builder.append("<head>");
                builder.append("</head>");
                builder.append("<body>");
                builder.append("<p>");
                builder.append(contentAA);
                builder.append("<button type=\"button\">Click Me!</button>");
                builder.append("<img src=\"file://android_res/drawable/white_img.png\"/>");
                builder.append("</body>");
                builder.append("</html>");
                String content = builder.toString();


String dd = "<html>\n" +
        "    <p style=\"color: #5987c6\">My Shared Itinerary - John Smith.</p>\n" +
        "    <p>Hello.</p>\n" +
        "    <p>I want to share my Memmingen, DE trip itinerary with you.</p>\n" +
        "    <p>Shared using \n" +
        "        <span style=\"color: #5987c6\">Blah</span> by BlahBlah\n" +
        "    </p>\n" +
        "</html>";


//                String emailList[]= {"dinesh.kumar@apptunix.com"};
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("message/rfc822");
//                intent.putExtra(Intent.EXTRA_EMAIL,emailList);
//                intent.putExtra(Intent.EXTRA_SUBJECT,"Email Subject");
//                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
//                startActivity(Intent.createChooser(intent,"Choice email APP"));


                String body = "<!DOCTYPE html><html>I am <b>bold text</b> and I am <i>italic text</i> and I am normal text.</html>";

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/html; charset=utf-8");

                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject");
              //  emailIntent.putExtra(Intent.EXTRA_TEXT,  Html.fromHtml("<a href =www.example.com/"+"result"+ " + > click here! </a> " ,0));
                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
//                emailIntent.putExtra(
//                        Intent.EXTRA_TEXT,
//                        Html.fromHtml(new StringBuilder()
//                                .append("<html>")
//                                .append("<p><b>Some Content</b></p>")
//                                .append("<small><p>More content</p></small>")
//                                .append("<a href=\"https://www.w3docs.com/\" class=\"button\">Click Here</a>")
//                                .append("</html>")
//                                .toString())
//                );
//                emailIntent.addCategory()
//                emailIntent.setContent(str,"text/html; charset=utf-8");
              //  startActivity(Intent.createChooser(emailIntent, "Email:"));

             //   new CreateHtmlTask(getCacheDir(), Abc.this).execute("input");


             //   sendMail("appa ", "link");


//                double quantity = 10.0;
//
//                double bb = 11.0;
//
//                if(quantity < bb){
//                    Log.e(TAG, "AAAAAAAAAA");
//                }else{
//                    Log.e(TAG, "BBBBBBBBBB");
//                }




//               WebView invoiceweb = findViewById(R.id.invoiceweb);
//                //create object of print manager in your device
//                PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);
//
//
//
//                //create object of print adapter
//                PrintDocumentAdapter printAdapter = invoiceweb.createPrintDocumentAdapter();
//
//                //provide name to your newly generated pdf file
//                String jobName = getString(R.string.app_name) + " Print Test";
//
//                PrintAttributes.Builder builder = new PrintAttributes.Builder();
//                builder.setMediaSize( PrintAttributes.MediaSize.ISO_A4);
//                printManager.print(jobName, printAdapter, builder.build());




                double vv = 987898.7272727273;

                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);
                String defaultPattern = nf.format(vv);


               // String patternA = Utility.getPatternFormat(""+0, vv)+"";

                Log.e(TAG, "taxAmount2 "+defaultPattern);

              //  materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

//                Map<String, Object> eventValue = new HashMap<String, Object>();
//                eventValue.put(AFInAppEventParameterName.PARAM_1, "App_Open");
//                AppsFlyerLib.getInstance().trackEvent(Abc.this, "app_open", eventValue);

//
//                Bundle parameters = new Bundle();
//                parameters.putString("level_name", "Caverns01");
//               // parameters.putInt("level_difficulty", 4);
//              //  firebaseAnalytics.setDefaultEventParameters(parameters);
//
////                Bundle bundle = new Bundle();
////                bundle.putString(EventTrackingKeys.EventTypes.CATEGORY, categoryName);
////                bundle.putString(EventTrackingKeys.EventTypes.ACTION, actionName);
////                bundle.putLong(EventTrackingKeys.EventTypes.VALUE, value);
//                firebaseAnalytics.setUserProperty("level_name", "Caverns01");
////                mFirebaseAnalytics.setUserProperty(EventTrackingKeys.EventTypes.ACTION, actionName);
////                mFirebaseAnalytics.setUserProperty(EventTrackingKeys.EventTypes.VALUE, value);
//                firebaseAnalytics.logEvent("feature_selected_event", parameters);

//                Bundle params = new Bundle();
//                params.putString("invalid_url", "urlPart");
//            //    firebaseAnalytics.logEvent("event_a", params);
//
////                Bundle bundle = new Bundle();
////                bundle.putString(FirebaseAnalytics.Param.CONTENT_ID, contentId);
////                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
//                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params);



              //  AppRater.app_launched(Abc.this);



//                Bundle params2 = new Bundle();
//                params2.putString("event_name", "App Open");
//                firebaseAnalytics.logEvent("app_open", params2);

                int a = 2;
                int b = 10;
                int x = 10;

                if(x >= a && b >= x){
                    Log.e(TAG , "yyyyyyyyyyy");
                }else{
                    Log.e(TAG , "nnnnnnnnnnn");
                }


                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);


                android.util.Pair<Boolean, SublimeOptions> optionsPair = getOptions();

//                if (!optionsPair.first) { // If options are not valid
//                    Toast.makeText(Abc.this, "No pickers activated",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            //    pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");


//
//                MaterialDatePicker.Builder startDateBuilder;
//                MaterialDatePicker startDatePicker;
//
//                startDateBuilder = MaterialDatePicker.Builder.datePicker();
//                startDateBuilder.setTitleText("Starting date");
//
//                long today = MaterialDatePicker.todayInUtcMilliseconds();
//
//
//                CalendarConstraints.Builder con = new CalendarConstraints.Builder();
//
//                CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();
//
//                con.setValidator(dateValidator); // Previous dates hide
//
//                con.setStart(today); // Calender start from set day of the month
//
//                startDateBuilder.setSelection(today);
//
//                startDateBuilder.setCalendarConstraints(con.build());
//                startDateBuilder.setTheme(R.style.AppTheme2); // Custom Theme
//
//                startDatePicker = startDateBuilder.build();
//
//                startDatePicker.show(getSupportFragmentManager(), startDatePicker.toString());


//                new SlyCalendarDialog()
//                        .setSingle(false)
//                        .setCallback(listener)
//                        .
////                        .setBackgroundColor(Color.parseColor("#ff0000"))
////                        .setSelectedTextColor(Color.parseColor("#ffff00"))
////                        .setSelectedColor(Color.parseColor("#0000ff"))
//                        .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");





//                double n = 12345678.00;
//
//                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//                nf.setMinimumFractionDigits(2);
//                String val1 = nf.format(n);
//
//                System.out.println(val1);
//                Log.e(TAG, "savedPDFFile "+val1);
//
//                NumberFormat nf2 = NumberFormat.getInstance(new Locale("sk", "SK"));
//                nf2.setMinimumFractionDigits(2);
//                String val2 = nf2.format(n);
//
//                System.out.println(val2);
//                Log.e(TAG, "savedPDFFile "+val2);
//
//                NumberFormat nf3 = NumberFormat.getInstance(new Locale("da", "DK"));
//                nf3.setMinimumFractionDigits(2);
//                String val3 = nf3.format(n);
//
//                System.out.println(val3);
//                Log.e(TAG, "savedPDFFile "+val3);
//
//
//                NumberFormat nf4 = NumberFormat.getInstance(new Locale("en", "IN"));
//                nf4.setMinimumFractionDigits(2);
//                String val4 = nf4.format(n);
//                Log.e(TAG, "savedPDFFile "+val4);


//                String dd = "wew009";
//
//                String sss = getRealValue2(dd);
//
//                Log.e(TAG, "savedPDFFile "+sss);



//                String pdf = "/sdcard/CreditNote.pdf";
//                String png = "/sdcard/logo_new.png";
//
//                File mFile1 = new File(pdf);
//                File mFile2 = new File(png);
//
//                Uri imageUri1 = FileProvider.getUriForFile(
//                        Abc.this,
//                        "com.sirapp.provider", //(use your app signature + ".provider" )
//                        mFile1);
//
//                Uri imageUri2 = FileProvider.getUriForFile(
//                        Abc.this,
//                        "com.sirapp.provider", //(use your app signature + ".provider" )
//                        mFile2);
//
//                ArrayList<Uri> uriArrayList = new ArrayList<>();
//                uriArrayList.add(imageUri1);
//                uriArrayList.add(imageUri2);
//
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                sharingIntent.setType("application/pdf/*|image/*");
//                sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
//              //  startActivity(Intent.createChooser(sharingIntent, "Share using"));
//
//                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
//                        "Thank you note");
//                sharingIntent.putExtra(Intent.EXTRA_TEXT,
//                        "We appreciate your Business. Kindly find below Thank you note.");
//
//                if (Utility.isAppAvailable(Abc.this, "com.google.android.gm")){
//                    sharingIntent.setPackage("com.google.android.gm");
//                }
//                startActivity(sharingIntent);

            }
        });


    }



    android.util.Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

//        if (cbDatePicker.isChecked()) {
            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
//        }
//
//        if (cbTimePicker.isChecked()) {
//            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
//        }
//
//        if (cbRecurrencePicker.isChecked()) {
//            displayOptions |= SublimeOptions.ACTIVATE_RECURRENCE_PICKER;
//        }

//        if (rbDatePicker.getVisibility() == View.VISIBLE && rbDatePicker.isChecked()) {
            options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
//        } else if (rbTimePicker.getVisibility() == View.VISIBLE && rbTimePicker.isChecked()) {
//            options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
//        } else if (rbRecurrencePicker.getVisibility() == View.VISIBLE && rbRecurrencePicker.isChecked()) {
//            options.setPickerToShow(SublimeOptions.Picker.REPEAT_OPTION_PICKER);
//        }

        options.setDisplayOptions(displayOptions);

        // Enable/disable the date range selection feature
        options.setCanPickDateRange(true);

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);

        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new android.util.Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }



    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
           // rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {




//
//            Log.e(TAG, "YEARSS "+String.valueOf(selectedDate.getStartDate().get(Calendar.YEAR)));
//            Log.e(TAG, "MONTHSS "+String.valueOf(selectedDate.getStartDate().get(Calendar.MONTH)));
//            Log.e(TAG, "DAYSS "+String.valueOf(selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH)));
//
//            String ddd = selectedDate.getStartDate().get(Calendar.YEAR) + "-" + (selectedDate.getStartDate().get(Calendar.MONTH)+1) + "-" + selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH);
//
//            try{
//                DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = simple.parse(ddd);
//                long datemillis = date.getTime();
//
//                Log.e(TAG, "datemillis "+datemillis);
//
//
//               // long miliSec = 3010;
//
//                // Creating date format
//               // DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
//
//                // Creating date from milliseconds
//                // using Date() constructor
//                Date result = new Date(datemillis);
//
//                // Formatting Date according to the
//                // given format
//
//                Log.e(TAG, "datemillis22 "+simple.format(result));
//
//
//                System.out.println(simple.format(result));
//
//
//            }catch (Exception e){
//
//            }

            Log.e(TAG, "");

            if (selectedDate != null) {
                if (selectedDate.getType() == SelectedDate.Type.SINGLE) {
//                tvYear.setText(applyBoldStyle("YEAR: ")
//                        .append(String.valueOf(mSelectedDate.getStartDate().get(Calendar.YEAR))));
//                tvMonth.setText(applyBoldStyle("MONTH: ")
//                        .append(String.valueOf(mSelectedDate.getStartDate().get(Calendar.MONTH))));
//                tvDay.setText(applyBoldStyle("DAY: ")
//                        .append(String.valueOf(mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH))));
                } else if (selectedDate.getType() == SelectedDate.Type.RANGE) {
//                llDateHolder.setVisibility(View.GONE);
//                llDateRangeHolder.setVisibility(View.VISIBLE);
//
//                tvStartDate.setText(applyBoldStyle("START: ")
//                        .append(DateFormat.getDateInstance().format(mSelectedDate.getStartDate().getTime())));
//                tvEndDate.setText(applyBoldStyle("END: ")
//                        .append(DateFormat.getDateInstance().format(mSelectedDate.getEndDate().getTime())));

                   // String dddS = selectedDate.getStartDate().get(Calendar.YEAR) + "-" + (selectedDate.getStartDate().get(Calendar.MONTH)+1) + "-" + selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH);

                    String fff = DateFormat.getDateInstance().format(selectedDate.getStartDate().getTime());
                    Log.e(TAG, "fff "+fff);
                    DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                    Date result = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
                    Log.e(TAG, "datemillis22 "+simple.format(result));
                }
            }
        }
    };


    SlyCalendarDialog.Callback listener = new SlyCalendarDialog.Callback() {
        @Override
        public void onCancelled() {

        }

        @Override
        public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {

        }
    };


    public void DownloadFile(String strUrl, String folderName, String fileName) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/"
                    + folderName);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            URL url = new URL(strUrl);
            File file = new File(dir, fileName);

            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void DownloadFromUrl(String DownloadUrl, String fileName) {

        try {
            File root = android.os.Environment.getExternalStorageDirectory();

            File dir = new File (root.getAbsolutePath() + "/DCIM");
            if(dir.exists()==false) {
                dir.mkdirs();
            }

            URL url = new URL(DownloadUrl); //you can write here any link
            File file = new File(dir, fileName);

            long startTime = System.currentTimeMillis();
            Log.d("DownloadManager", "download begining");
            Log.d("DownloadManager", "download url:" + url);
            Log.d("DownloadManager", "downloaded file name:" + fileName);

            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

            /*
             * Define InputStreams to read from the URLConnection.
             */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }


            /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            Log.e("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

        } catch (IOException e) {
            Log.e("DownloadManager", "Error: " + e);
        }

    }



//    public void createandDisplayPdf(String text) {
//
//        Document doc = new Document();
//
//        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
//
//            File dir = new File(path);
//            if(!dir.exists())
//                dir.mkdirs();
//
//            File file = new File(dir, "newFile.pdf");
//            FileOutputStream fOut = new FileOutputStream(file);
//
//            PdfWriter.getInstance(doc, fOut);
//
//            //open the document
//            doc.open();
//
//            Paragraph p1 = new Paragraph(text);
//            Font paraFont= new Font(Font.COURIER);
//            p1.setAlignment(Paragraph.ALIGN_CENTER);
////            p1.setFont(paraFont);
//
//            //add paragraph to document
//            doc.add(p1);
//
//        } catch (DocumentException de) {
//            Log.e("PDFCreator", "DocumentException:" + de);
//        } catch (IOException e) {
//            Log.e("PDFCreator", "ioException:" + e);
//        }
//        finally {
//            doc.close();
//        }
//
//        //viewPdf("newFile.pdf", "Dir");
//    }


    public void stringtopdf(String data) {
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        File fol = new File(extstoragedir, "pdf");
        File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdir();
        }
        try {
//            final File file = new File(folder, "sample.pdf");
//            file.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText(data, 10, 10, paint);


            document.finishPage(page);
            document.writeTo(new FileOutputStream(getOutputFile()));
            document.close();

        } catch (IOException e) {
            Log.i("error", e.getLocalizedMessage());
        }

    }

    private File getOutputFile(){
        File root = new File(this.getExternalFilesDir(null),"My");

        boolean isFolderCreated = true;

        if (!root.exists()){
            isFolderCreated = root.mkdir();
        }

        if (isFolderCreated) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "PDF_" + timeStamp;

            return new File(root, imageFileName + ".pdf");
        }
        else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void createPDFWithMultipleImage(){
        File file = getOutputFile();
        if (file != null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = new PdfDocument();

               // for (int i = 0; i < images.size(); i++){
                    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/aa.png");
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), (1)).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    canvas.drawPaint(paint);
                    canvas.drawBitmap(bitmap, 0f, 0f, null);
                    pdfDocument.finishPage(page);
                    bitmap.recycle();
            //    }
                pdfDocument.writeTo(fileOutputStream);
                pdfDocument.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG , "CCCC "+e.getMessage());
            }
        }
    }


    private String getRealValue(String sss) {
        String valueIs = "";
        if(sss.toString().length() > 0){

            // char cc = invoicenum.getText().toString().charAt(invoicenum.getText().toString().length() - 1);

            boolean gg = isNumeric(sss);
            Log.e(TAG, "gggggg "+gg);

            boolean dd = isChar(sss);
            Log.e(TAG, "dddddd "+dd);

            if(gg == false && dd == false){
                Log.e(TAG, "truee ");
                Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
                Log.e(TAG, "cccccc "+flag);
                if(flag == true){

                    String str = sss;


                    StringBuilder sb = new StringBuilder();
                    for (int i = str.length() - 1; i >= 0; i --) {
                        char c = str.charAt(i);
                        if (Character.isDigit(c)) {
                            sb.insert(0, c);
                        } else {
                            break;
                        }
                    }
                    String result = sb.toString();

                    Log.e(TAG, "ccccccresult "+result);

                    String vvvvv = sss.substring(0, sss.length() - result.length());
                    Log.e(TAG, "ccccccvvvvv "+vvvvv);

                    int secondLength = result.length();
                    Log.e(TAG, "secondLength "+secondLength);

                    int wrappedValue = Integer.parseInt(result);
                    Log.e(TAG, "wrappedValue "+wrappedValue);

                    int XXXXX = String.valueOf(wrappedValue).length();
                    int XXXXXPlus = wrappedValue+1;
//                   int first =  String.valueOf(secondLength).length();
//                    int second = String.valueOf(wrappedValue).length();
//                    Log.e(TAG, "first "+first);
                    Log.e(TAG, "XXXXX "+XXXXX);

                    String vvvvvSS = sss.substring(0, sss.length() - XXXXX);
                    Log.e(TAG, "vvvvvSS "+vvvvvSS+XXXXXPlus);

//                    if(secondLength == XXXXX){
//                        int wrappedValuePlus1 = Integer.parseInt(result) + 1;
//                        Log.e(TAG, "wrappedValuePlus1 "+wrappedValuePlus1);
//                    }else{
//                        int wrappedValuePlus2 = Integer.parseInt(result) + 1;
//                        Log.e(TAG, "wrappedValuePlus2 "+wrappedValuePlus2);
//                    }




//                    String str = sss;
//                    String cc = extractInt(str);
//                    if(cc.contains(" ")){
//                        String vv[] = cc.split(" ");
//                        String ii =  vv[vv.length - 1];
//                        Log.e(TAG , "extractInt "+ii);
//                        String vvvvv = sss.substring(0, sss.length() - ii.length());
//
//                        Log.e(TAG , "vvvvv "+vvvvv);
//
//                        int myValue = Integer.parseInt(ii)+1;
//                        valueIs = vvvvv+myValue;
//                    }
//                    if(!cc.contains(" ")){
//                        Log.e(TAG , "extractInt2 "+cc);
//
//                        int myValue = Integer.parseInt(cc);
//                        Log.e(TAG , "aaaaaa "+myValue);
//
//                        int length = String.valueOf(myValue).length();
//
//                        String vvvvvSS = sss.substring(0, sss.length() - length);
//
//                        int myValuePlus = myValue+1;
//                        Log.e(TAG , "myValuePlus "+myValuePlus);
//
//                        Log.e(TAG , "myValuePlusvvv "+vvvvvSS);
//
//
//                        valueIs = vvvvvSS+myValuePlus;
//
//                        Log.e(TAG , "bbbbbb "+valueIs);
//
//                    }
                }
            }else{
                boolean ddd = isChar(sss);
                if(ddd == false){
                    int myValue = Integer.parseInt(sss)+1;
                    valueIs = "Inv # "+myValue;
                }
            }
        }
        return valueIs;
    }


    private String getRealValue2(String sss) {
        String valueIs = "";
        if(sss.toString().length() > 0){
                Log.e(TAG, "truee ");
                Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
                Log.e(TAG, "cccccc "+flag);
                if(flag == true){
                    String str = sss;
                    StringBuilder sb = new StringBuilder();
                    for (int i = str.length() - 1; i >= 0; i --) {
                        char c = str.charAt(i);
                        if (Character.isDigit(c)) {
                            sb.insert(0, c);
                        } else {
                            break;
                        }
                    }
                    String result = sb.toString();

                    Log.e(TAG, "ccccccresult "+result);
                    valueIs = "Inv # "+result;
                }
        }
        return valueIs;
    }




    static String extractInt(String str)
    {
        // Replacing every non-digit number
        // with a space(" ")
        str = str.replaceAll("[^\\d]", " ");

        // Remove extra spaces from the beginning
        // and the ending of the string
        str = str.trim();

        // Replace all the consecutive white
        // spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.equals(""))
            return "-1";

        return str;
    }


    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }


    public static boolean isChar(String str)
    {
        for (char c : str.toCharArray())
        {
            if (Character.isDigit(c)) return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG , "onActivityResult "+requestCode);

    }

    @Override
    public void onHtmlCreated(HtmlFile html) {
        startSendEmailIntent(html.getFilePath());
    }


    private void startSendEmailIntent(Uri attachmentUri) {
        File mFile2 = new File("/sdcard/share.jpg");
        Uri imageUri2 = FileProvider.getUriForFile(
                Abc.this,
                BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
                mFile2);

        ArrayList<Uri> uriArrayList = new ArrayList<>();
        uriArrayList.add(attachmentUri);
       // uriArrayList.add(imageUri2);


        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //intent.setType("text/html");
        intent.setType("application/pdf/*|image/*|text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//        intent.putExtra(Intent.EXTRA_STREAM, attachmentUri);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Message body. <b>Funky!</b> <i>not</i>"));
        Intent chooser = Intent.createChooser(intent, "Send Email");
        startActivity(chooser);
    }



    class MyAsyncTasks extends AsyncTask<String, String, String> {

        File sdCardRoot;

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();


                sdCardRoot = new File(Environment.getExternalStorageDirectory(), "DCIM");

                if (!sdCardRoot.exists()) {
                    sdCardRoot.mkdirs();
                }

                Log.e("check_path", "" + sdCardRoot.getAbsolutePath());

                String fileName =
                        strings[0].substring(strings[0].lastIndexOf('/') + 1, strings[0].length());
                Log.e("dfsdsjhgdjh", "" + fileName);
                File imgFile =
                        new File(sdCardRoot, fileName);
                if (!sdCardRoot.exists()) {
                    imgFile.createNewFile();
                }
                InputStream inputStream = urlConnection.getInputStream();
                int totalSize = urlConnection.getContentLength();
                FileOutputStream outPut = new FileOutputStream(imgFile);
                int downloadedSize = 0;
                byte[] buffer = new byte[2024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    outPut.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.e("Progress:", "downloadedSize:" + Math.abs(downloadedSize * 100 / totalSize));
                }
                Log.e("Progress:", "imgFile.getAbsolutePath():" + imgFile.getAbsolutePath());

                Log.e(TAG, "check image path 2" + imgFile.getAbsolutePath());

               // mImageArray.add(imgFile.getAbsolutePath());
                outPut.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("checkException:-", "" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            imagecount++;
//            Log.e("check_count", "" + totalimagecount + "==" + imagecount);
//            if (totalimagecount == imagecount) {
//                pDialog.dismiss();
//                imagecount = 0;
//            }
//            Log.e("ffgnjkhjdh", "checkvalue checkvalue" + checkvalue);


        }


    }




    /*--Download Image in Storage--*/
    public void downloadImage(String URL) {
        final Long reference;
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("AppName");
        request.setDestinationInExternalPublicDir(String.format("%s/%s", Environment.getExternalStorageDirectory(),
                getString(R.string.app_name)), "FileName.pdf");
        Log.e("myi", "downloadImage: " + request.setDestinationInExternalPublicDir(String.format("%s/%s", Environment.getExternalStorageDirectory(),
                getString(R.string.app_name)), "FileName.pdf"));

        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        reference = downloadManager.enqueue(request);

        Log.e("download", "Image Download : " + reference);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Toast.makeText(Abc.this, "Image Downloaded Successfully ", Toast.LENGTH_LONG);
                } catch (Exception e) {
                }
            }
        };
        getApplicationContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }





    private class DownloadImage extends AsyncTask<String, Integer, String> {

        private Context c;

        private DownloadImage(Context c) {
            this.c = c;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream is = null;
            OutputStream os = null;
            HttpURLConnection con = null;
            int length;
            try {
                URL url = new URL(sUrl[0]);
                con = (HttpURLConnection) url.openConnection();
                con.connect();

                if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "HTTP CODE: " + con.getResponseCode() + " " + con.getResponseMessage();
                }

                length = con.getContentLength();

                pd.setMax(length / (1000));

                is = con.getInputStream();
                os = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "a-computer-engineer.pdf");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = is.read(data)) != -1) {
                    if (isCancelled()) {
                        is.close();
                        return null;
                    }
                    total += count;
                    if (length > 0) {
                        publishProgress((int) total);
                    }
                    os.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (os != null)
                        os.close();
                    if (is != null)
                        is.close();
                } catch (IOException ioe) {
                }

                if (con != null)
                    con.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pd.setIndeterminate(false);
            pd.setProgress(progress[0] / 1000);
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            if (result != null) {
                Toast.makeText(c, "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(c, "Image downloaded successfully!", Toast.LENGTH_SHORT).show();
               // Bitmap b = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "a-computer-engineer.pdf");
                //iv.setImageBitmap(b);
            }
        }
    }



    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://13.126.22.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf");
                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                //and connect!
                urlConnection.connect();
                //set the path where we want to save the file in this case, going to save it on the root directory of the sd card.
                File SDCardRoot = Environment.getExternalStorageDirectory();
                //create a new file, specifying the path, and the filename which we want to save the file as.
                File file = new File(SDCardRoot,"image.pdf");
                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);
                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();
                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer
                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    //updateProgress(downloadedSize, totalSize);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Image Downloaded to sd card",Toast.LENGTH_SHORT).show();
        }
    }



//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        primaryBaseActivity=newBase;//SAVE ORIGINAL INSTANCE
//
//        /*Some locale handling stuff right here*/
//        /*LocaleHelper's onAttach is returning a *new* context in Android N which will void PrintManager's context*/
//        super.attachBaseContext(LocaleHelper.onAttach(primaryBaseActivity));
//
//    }

}
