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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import io.github.lucasfsc.html2pdf.Html2Pdf;

import com.lowagie.text.Font;

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


                MyAsyncTask asyncTask = new MyAsyncTask();
                asyncTask.execute();


//                pd = new ProgressDialog(Abc.this);
//                pd.setMessage("Downloading image, please wait ...");
//                pd.setIndeterminate(true);
//                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pd.setCancelable(false);
//                pd.setProgressNumberFormat("%1d KB/%2d KB");
//
//                DownloadImage di = new DownloadImage(Abc.this);
//                di.execute("http://13.233.155.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf");
//
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        di.cancel(true);
//                    }
//                });
                //downloadImage("http://13.233.155.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf");
//                DownloadFromUrl("http://13.233.155.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf", "601e40db1f4ed.pdf" );

               // DownloadFile("http://13.233.155.0/saad/app/uploads/invoice/pdf/", "DCIM" , "601e40db1f4ed.pdf" );

//                MyAsyncTasks asyncTasks = new MyAsyncTasks();
//                asyncTasks.execute("http://13.233.155.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf");




//                try {
//                    URL url = new URL("http://13.233.155.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf");
//                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
//                    c.setRequestMethod("GET");
//                    c.setDoOutput(true);
//                    c.connect();
//
//                    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/down";
//                    File file = new File(PATH);
//                    if(!file.exists()) {
//                        file.mkdirs();
//                    }
//                    String name=String.valueOf("601e40db1f4ed")+".pdf";
//                    File outputFile = new File(file,name);
//                    FileOutputStream fos =
//                            new FileOutputStream(outputFile);//this line
//
//                    InputStream is = c.getInputStream();
//                    int fileLength = 0;
//
//                    byte[] buffer = new byte[4096];
//                    int len1 = 0;
//                    long total = 0;
//                    while ((len1 = is.read(buffer)) != -1) {
//                        total += len1;
//                        if (fileLength > 0)
//                            //publishProgress((int) (total));
//                        fos.write(buffer, 0, len1);
//                    }
//                    fos.close();
//                    is.close();
//
//                } catch (Exception e) {
//                    Log.e("Error: ", e.getMessage());
//                }
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.e(TAG , "Abc ");
//
////                final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
////                PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, " <!DOCTYPE html>\n" +
////                        "<html>\n" +
////                        "<body>\n" +
////                        "\n" +
////                        "<h1>My First Heading</h1>\n" +
////                        "<p>My first paragraph.</p>\n" +
////                        " <a href='https://www.example.com'>This is a link</a>" +
////                        "\n" +
////                        "</body>\n" +
////                        "</html> ", new PDFPrint.OnPDFPrintListener() {
////                    @Override
////                    public void onSuccess(File file) {
////
////                        Log.e(TAG, "file!!! "+file);
////
////                        // Open Pdf Viewer
////                       // Uri pdfUri = Uri.fromFile(file);
////
////
////                        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
//////                        Intent intent = new Intent(Intent.ACTION_VIEW);
//////                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//////                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//////                        startActivity(intent);
////
////
////
////                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
////                        //File fileWithinMyDir = new File(pdfUri);
////
////                        if(file.exists()) {
////                            Uri photoURI = FileProvider.getUriForFile(Abc.this,
////                                    "com.receipt.invoice.stock.sirproject.provider",
////                                    file);
////                            intentShareFile.setType("application/pdf");
////                            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(""+photoURI));
////
////                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
////                                    "Share As Pdf");
////                          //  intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
////
////                            startActivity(Intent.createChooser(intentShareFile, "Share File"));
////                        }
////
//////                        Intent intentPdfViewer = new Intent(Abc.this, PDFViewerActivity.class);
//////                        intentPdfViewer.putExtra(PDFViewerActivity.PDF_FILE_URI, pdfUri);
//////
//////                        startActivity(intentPdfViewer);
////                    }
////
////                    @Override
////                    public void onError(Exception exception) {
////                        exception.printStackTrace();
////                    }
////                });
//
//                Intent intent = new Intent(Abc.this, ChooseTemplate.class);
//                intent.putExtra("companycolor", "#ffffff");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 121);
//
//            }
//        });
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
                        int myValue = Integer.parseInt(cc)+1;
                        String vvvvv = sss.substring(0, sss.length() - cc.length());

                        Log.e(TAG , "bbbbbb "+vvvvv);
                        valueIs = vvvvv+myValue;
                    }
                }
            }else{
                valueIs = "Inv # 1";
                Log.e(TAG, "falsee ");
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
                URL url = new URL("http://13.233.155.0/saad/app/uploads/invoice/pdf/601e40db1f4ed.pdf");
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
