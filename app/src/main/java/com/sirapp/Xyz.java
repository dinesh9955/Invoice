package com.sirapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import project.aamir.sheikh.circletextview.CircleTextView;
import org.apache.commons.lang3.text.WordUtils;
public class Xyz extends AppCompatActivity {


    private static final String TAG = "Xyz";

    public void createShortDialog() {
		// TODO Auto-generated method stub

//				final Builder dialog = new Builder(splash, AlertDialog.THEME_HOLO_LIGHT);
				final Dialog dialog = new Dialog(Xyz.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.dialog_choose_color);

                RecyclerView mRecyclerView = (RecyclerView) dialog.findViewById(R.id.color_picker);
        //                mRecyclerView.setHasFixedSize(true);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");
                arrayList.add("#fff000");

                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));

               // mRecyclerView.setLayoutManager(new LinearLayoutManager(Xyz.this, LinearLayoutManager.VERTICAL, false));
                ColorAdapter mAdapter = new ColorAdapter(arrayList);
                mRecyclerView.setAdapter(mAdapter);

				dialog.show();




	}






    public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

        private static final String TAG = "ColorAdapter";

        ArrayList<String> cnames = new ArrayList<>();

        Dialog mybuilder;

        public ColorAdapter(ArrayList<String> cnames) {
            super();
            this.cnames = cnames;
            this.mybuilder = mybuilder;
        }




        @Override
        public ColorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.color_item, viewGroup, false);
            return new ColorAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ColorAdapter.ViewHolder viewHolder, final int i) {
//            mCircleTextView.setCustomText(mArrayList.get(position)); //Supply your whole text here it will automatically generate the initial
//            viewHolder.textViewName.setSolidColor(position); //pass position if used inside RecyclerView otherwise you can keep blank this is used to save background color state
            //mCircleTextView.setTextColor(Color.WHITE);
        //    mCircleTextView.setCustomTextSize(18);
            viewHolder.textViewName.setSolidColor(Color.BLUE);
//            viewHolder.textViewName.setText(""+cnames.get(i));
//            viewHolder.realtive1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mybuilder.dismiss();
//                    // description.setText(""+alName.get(i).getCompany_name());
//                    // HomeApi(alName.get(i).company_id);
//
//                    selectedCompanyId = cids.get(i);
//
//                    selectcompany.setText(""+cnames.get(i));
//
//                    warehouse_list(selectedCompanyId);
//                    serviceget(selectedCompanyId);
//                    customer_list(selectedCompanyId);
//                    CompanyInformation(selectedCompanyId);
//                }
//            });

        }


        @Override
        public int getItemCount() {
            return cnames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            CircleTextView textViewName;
            RelativeLayout realtive1;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
              //  realtive1 = (RelativeLayout) itemView.findViewById(R.id.realtive1);
                textViewName = (CircleTextView) itemView.findViewById(R.id.textView);

              //  CircleTextView  mCircleTextView = (CircleTextView) findViewById(R.id.textView); //change with your id

            }

        }



        public void updateData(ArrayList<String> cnames) {
            // TODO Auto-generated method stub
            this.cnames = cnames;
            notifyDataSetChanged();
        }


    }


    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }



    private void shareImage(){



        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.accessories);


        File f =  new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");
        Intent shareIntent;


        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

            outputStream.flush();
            outputStream.close();
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +getPackageName());
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        }catch (Exception e){
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(shareIntent,"Share Picture"));
    }


    void share() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.accessories);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_TEXT, "hello #test");

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private Double toMilliSeconds(Double days) {
        return days * 24 * 60 * 60 * 1000;
    }



    private String getRealValueWithoutplus(String sss) {
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
                    String cc = extractInt(str);
                    if(cc.contains(" ")){
                        String vv[] = cc.split(" ");
                        String ii =  vv[vv.length - 1];
                        Log.e(TAG , "extractInt "+ii);
                        String vvvvv = sss.substring(0, sss.length() - ii.length());

                        Log.e(TAG , "vvvvv "+vvvvv);

                        int myValue = Integer.parseInt(ii)+1;
                        valueIs = vvvvv+myValue;
                    }
                    if(!cc.contains(" ")){
                        Log.e(TAG , "extractInt2 "+cc);
                        int myValue = Integer.parseInt(cc);
                       // String vvvvv = sss.substring(0, sss.length() - cc.length());

                        Log.e(TAG , "bbbbbb "+myValue);
                        valueIs = "Invoice # "+myValue;
                    }
                }
            }else{
                boolean ddd = isChar(sss);
                if(ddd == false){
                    int myValue = Integer.parseInt(sss);
                    valueIs = "Invoice # "+myValue;
                }
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


    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }




    private static class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        Context context;
        String subject;

        DownloadFile(Context c, String sub) {
            context = c;
            subject = sub;
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
                folder = Environment.getExternalStorageDirectory() + File.separator + "SIR/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String newFileName = "Receipt.pdf";
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

            // Display File path after downloading
            //  Toast.makeText(context, message, Toast.LENGTH_LONG).show();


//                                    Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
//                                    shareIntent.setType("text/plain");
//                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
//                                    //String app_url = "file:///home/apptunix/Desktop/invoice.html";
//                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
//                                    context.startActivity(Intent.createChooser(shareIntent, "Share via"));


            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(message);
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.sirapp.provider",
                    fileWithinMyDir);

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        subject);

                context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }

        }
    }






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

        Button button = (Button) findViewById(R.id.button2);

        TextView textView3 = (TextView) findViewById(R.id.textView3);



        final String str2 = "hello wO";

        //String output = str2.substring(0, 1).toUpperCase() + str2.substring(1);

       // String output = capitalizeFirstLetter(str2);
       // System.out.println(capitalizeFirstLetter(str2));

        String ggg = WordUtils.capitalize(str2);

        Log.e(TAG , "gggGG "+ggg);

      //  textView3.setText(""+WordUtils.capitalizeFirstLetter(str2));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.thanksimg);
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "share.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                share.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                share.putExtra(Intent.EXTRA_TEXT, "txt");
//
//                share.putExtra(Intent.EXTRA_STREAM,
//                        Uri.parse("file:///sdcard/share.jpg"));
//                startActivity(Intent.createChooser(share, "Share..."));



//                Intent email = new Intent(Intent.ACTION_SEND);
////                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "dinu@gmail.com"});
//                email.setData(Uri.parse("mailto:" + "recipient@example.com"));
//                email.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                email.putExtra(Intent.EXTRA_TEXT, "message");
//
////need this to prompts email client only
//                email.setType("message/rfc822");
//
//                startActivity(Intent.createChooser(email, "Choose an Email client :"));



//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse("mailto:" + "recipient@example.com"));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");
//
//                try {
//                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(Xyz.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
//                }
//                share.putExtra(Intent.EXTRA_STREAM,
//                        Uri.parse("file:///sdcard/share.jpg"));

//                Intent intent = new Intent(Intent.ACTION_SENDTO)
//                        .setData(new Uri.Builder().scheme("mailto").build())
//                        .putExtra(Intent.EXTRA_EMAIL, new String[]{ "John Smith <johnsmith@yourdomain.com>" })
//                        .putExtra(Intent.EXTRA_SUBJECT, "Email subject")
//                        .putExtra(Intent.EXTRA_TEXT, "Email body")
//                        .putExtra(Intent.EXTRA_STREAM,
//                                Uri.parse("file:///sdcard/share.jpg"))
//                        ;
//                //intent.setType("image/png");
//                intent.setType("message/rfc822");
//
//                ComponentName emailApp = intent.resolveActivity(getPackageManager());
//                ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
//                if (emailApp != null && !emailApp.equals(unsupportedAction))
//                    try {
//                        // Needed to customise the chooser dialog title since it might default to "Share with"
//                        // Note that the chooser will still be skipped if only one app is matched
//                        Intent chooser = Intent.createChooser(intent, "Send email with");
//                        startActivity(chooser);
//                        return;
//                    }
//                    catch (ActivityNotFoundException ignored) {
//                    }


//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"skapis1@outlook.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
//               // i.setType("image/jpeg");
//                i.putExtra(Intent.EXTRA_STREAM,
//                                Uri.parse("file:///sdcard/share.jpg"));
//                try {
//                    startActivity(Intent.createChooser(i, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(Xyz.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }

//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                        "mailto","gg@gmail.com", null));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report message");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "edt_msg.getText().toString()");
//                emailIntent.putExtra(Intent.EXTRA_STREAM,
//                                Uri.parse("file:///sdcard/share.jpg"));
//                emailIntent.setType("message/rfc822");
//                startActivity(Intent.createChooser(emailIntent, "Send email..."));



//                String subject = "Lap times";
//                ArrayList<Uri> attachments = new ArrayList<Uri>();
//                attachments.add(Uri.parse("file:///sdcard/share.jpg"));
//                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
//                intent.setClassName("com.android.email", "com.android.mail.compose.ComposeActivity");
//
//                try {
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }



//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.setType("text/plain");
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
//                File root = Environment.getExternalStorageDirectory();
////                String pathToMyAttachedFile = "temp/attachement.xml";
////                File file = new File(root, pathToMyAttachedFile);
////                if (!file.exists() || !file.canRead()) {
////                    return;
////                }
//                Uri uri = Uri.fromFile(f);
//                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                emailIntent.setClassName("com.android.email", "com.android.mail.compose.ComposeActivity");
//                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

//                Intent email = new Intent(android.content.Intent.ACTION_SEND);
//                email.setType("application/octet-stream");


//                Uri path = Uri.fromFile(f);
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//// set the type to 'email'
//                emailIntent .setType("vnd.android.cursor.dir/email");
//                String to[] = {"asd@gmail.com"};
//                emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
//// the attachment
//                emailIntent .putExtra(Intent.EXTRA_STREAM, path);
//// the mail subject
//                emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                startActivity(Intent.createChooser(emailIntent , "Send email..."));



                try {
                    String to = "";
                    String subject= "Hi I am subject";
                    String body="Hi I am test body";
                    String mailTo = "mailto:" + to +
                            "?&subject=" + Uri.encode(subject) +
                            "&body=" + Uri.encode(body);

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    //intent.setType("*/*");
//                    intent.setType("application/pdf");
                   // intent.setType("text/plain");
                    String message="File to be shared is " + "file_name" + ".";
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");




                    Uri ccc = Uri.fromFile(f);
                    Log.e(TAG , "XXXXXXXXXX "+f.toString());
                    Log.e(TAG , "XXXXXXXXXX22 "+ccc.toString());

                    File fileWithinMyDir = new File("/storage/emulated/0/CreditNote.pdf");
                    Uri ddd = Uri.fromFile(fileWithinMyDir);
                    Log.e(TAG , "YYYYYYYYYYY "+fileWithinMyDir.toString());
                    Log.e(TAG , "YYYYYYYYYYY22 "+ddd.toString());

                   // Uri uri = FileProvider.getUriForFile(Xyz.this, "com.redacted.redacted.fileprovider", fileWithinMyDir);


                    Uri photoURI = FileProvider.getUriForFile(Xyz.this,
                            "com.sirapp.provider",
                            fileWithinMyDir);

                    File file = new File(Environment.getExternalStorageDirectory(),
                            "CreditNote.pdf");

                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
//                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//                    intent.setDataAndType(photoURI,"application/pdf");

//                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    //intent.setData(Uri.parse("mailto:xyz@gmail.com"));
                    intent.setData(Uri.parse(mailTo));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                  //  intent.setType("*/*");
//                    intent.setType("application/octet-stream");
//                    intent.setType("text/html");
//                    intent.setType("application/pdf");
//
//                    intent.setType("message/rfc822");


                    //startActivity(intent);
                } catch(Exception e)  {
                    System.out.println("is exception raises during sending mail"+e);
                }

                    String to = "abc@gmail.com";
//                    String subject= "Hi I am subject";
//                    String body="Hi I am test body";
                    String mailTo = "mailto:" + to;
//
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                File fileWithinMyDir = new File("/storage/emulated/0/SIR/CreditNote.pdf");
//                Uri photoURI = FileProvider.getUriForFile(Xyz.this,
//                        "com.sirapp.provider",
//                        fileWithinMyDir);
//               // intentShareFile.setType("text/plain");
//                if(fileWithinMyDir.exists()) {
//                   // intentShareFile.setType("application/pdf");
//                    Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
//                    intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
//                    intentShareFile.setData(Uri.parse(mailTo));
//                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                            "subject");
//
//                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                }

//                String[] mimeTypes =
//                        {"application/msword", "text/plain", "application/pdf", "application/zip"};
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
//                    if (mimeTypes.length > 0) {
//                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//                    }
//                } else {
//                    String mimeTypesStr = "";
//                    for (String mimeType : mimeTypes) {
//                        mimeTypesStr += mimeType + "|";
//                    }
//                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
//                }
//                startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 100);

//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SENDTO);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//               // intent.putExtra(Intent.EXTRA_EMAIL, "mailID");
//                intent.setData(Uri.parse(mailTo));
//                // Need to grant this permission
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                // Attachment
//               // intent.setType("vnd.android.cursor.dir/email");
//
//
                File fileWithinMyDir = new File("/storage/emulated/0/CreditNote.pdf");
//                Uri photoURI = FileProvider.getUriForFile(Xyz.this,
//                        "com.sirapp.provider",
//                        fileWithinMyDir);
//
//                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileWithinMyDir));
//
//                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
//
//
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//
//
////                if (isNetworkAvailable(context)) {
////                    if (isAppAvailable(context, "com.google.android.gm"))
//                        intent.setPackage("com.google.android.gm");
//                    startActivityForResult(intent, 101);
////                }



                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                //File fileWithinMyDir = new File(message);
                Uri photoURI = FileProvider.getUriForFile(Xyz.this,
                        "com.sirapp.provider",
                        fileWithinMyDir);

                if(fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/pdf");
                    Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "subject");
                    if (isAppAvailable(Xyz.this, "com.google.android.gm")){
                        intentShareFile.setPackage("com.google.android.gm");
                    }
                    startActivityForResult(intentShareFile, 101);
                   // startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }

            }
        });




}

    public static Boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled;
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }


    public Intent createEmailOnlyChooserIntent(Intent source,
                                               CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "info@domain.com", null));
        List<ResolveInfo> activities = getPackageManager()
                .queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }

    void share3() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.thanksimg);

//        Intent share = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                "mailto","abc@gmail.com", null));

//        Intent share = new Intent(Intent.ACTION_SENDTO);

        Intent share = new Intent(Intent.ACTION_SENDTO);
        share.setData(Uri.parse("mailto:abc@xyz.com"));

        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "share.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_TEXT, "hello #test");
//        share.putExtra(Intent.EXTRA_EMAIL, new String[]{ "email@server.com"});
//        share.setData(Uri.parse("mailto:"));
//        share.setType("message/rfc822");
       // share.setType("text/html");

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file:///sdcard/share.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private void share2() {
        int sharableImage = R.drawable.upload;
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), sharableImage);
        String path = getExternalCacheDir()+"/sharable_image.jpg";
        java.io.OutputStream out;
        java.io.File file = new java.io.File(path);

        if(!file.exists()) {
            try {
                out = new java.io.FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        path = file.getPath();

        Uri bmpUri = Uri.parse("file://" + path);

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is a sample body with more detailed description");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent,"Share with"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG , "onActivityResult "+requestCode);

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG , "onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG , "onResume ");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG , "onNewIntent ");
    }
}
