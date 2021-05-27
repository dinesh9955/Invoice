package com.receipt.invoice.stock.sirproject;

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
import android.os.Bundle;
import android.os.Environment;
//import android.print.PdfConverter;
//import android.print.PdfConverter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import io.github.lucasfsc.html2pdf.Html2Pdf;

import com.lowagie.text.Font;
import com.receipt.invoice.stock.sirproject.InvoiceReminder.SendInvoiceReminderActivity;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
//import com.tejpratapsingh.pdfcreator.utils.FileManager;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

public class Abc extends AppCompatActivity{


    private static final String TAG = "Abc";
    private ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

        Button button = (Button) findViewById(R.id.button2);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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


                String dd = "wew009";

                String sss = getRealValue2(dd);

                Log.e(TAG, "savedPDFFile "+sss);

            }
        });


    }



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



    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "newFile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font(Font.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
//            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        //viewPdf("newFile.pdf", "Dir");
    }


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


}
