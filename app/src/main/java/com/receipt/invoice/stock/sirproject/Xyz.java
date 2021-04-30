package com.receipt.invoice.stock.sirproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Invoice.ChooseTemplate;
import com.receipt.invoice.stock.sirproject.Invoice.Fragment_Create_Invoice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import project.aamir.sheikh.circletextview.CircleTextView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("android.resource://com.receipt.invoice.stock.sirproject/drawable/white_img.png");

//                Uri uri = FileProvider.getUriForFile(this, "com.example.provider", new File(photoPath));
//                Intent share = ShareCompat.IntentBuilder.from(Xyz.this)
//                        .setStream(uri) // uri from FileProvider
//                        .setType("text/html")
//                        .getIntent()
//                        .setAction(Intent.ACTION_SEND) //Change if needed
//                        .setDataAndType(uri, "image/*")
//                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                startActivity(Intent.createChooser(share, getString(R.string.share_image)));
//
//                String text = "Look at my awesome picture";
//                Uri pictureUri = Uri.parse("file://my_picture");
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/*");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, "Share images..."));

//                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("image/jpg");
//                final File photoFile = new File("temporary_file.jpg");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
//                startActivity(Intent.createChooser(shareIntent, "Share image using"));


//                Intent shareIntent;
//                Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
//                OutputStream out = null;
//                File file=new File(path);
//                try {
//                    out = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                    out.flush();
//                    out.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                path=file.getPath();
//                Uri bmpUri = Uri.parse("file://"+path);
//                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//                shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +getPackageName());
//                shareIntent.setType("image/png");
//                startActivity(Intent.createChooser(shareIntent,"Share with"));


//                Uri imageUri = Uri.parse("android.resource://" + getPackageName()
//                        + "/drawable/" + "thanksimg");
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/jpeg");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, "send"));


//                Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ "/drawable/" + "thanksimg");
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, "Hello");
//                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                intent.setType("*/*");
//                startActivity(intent);


////                String[] TO = {email@server.com};
////               // Uri imageUri = Uri.parse("android.resource://com.examle.tarea/" + R.drawable.thanksimg);
//                Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ R.drawable.thanksimg);
//
//
//
//                String[] TO = {"email@server.com"};
//                Uri uri = Uri.parse("mailto:email@server.com")
//                        .buildUpon()
//                        .appendQueryParameter("subject", "subject")
//                        .appendQueryParameter("body", "txt")
//                        .appendQueryParameter("file", "file:///sdcard/temporary_file.jpg")
//
//                        .build();
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
//                // emailIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//              //  emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" +company_stamp22));
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//                startActivity(Intent.createChooser(emailIntent, "Send mail..."));








//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                //intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                intent.putExtra(Intent.EXTRA_EMAIL, TO);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "imageUri3");
//                intent.putExtra(Intent.EXTRA_TEXT, "imageUrddfdsfi3");
//                startActivity(Intent.createChooser(intent, getString(R.string.facebook_app_id)));


//                String text = "Look at my awesome picture";
//               // Uri pictureUri = Uri.parse("file://my_picture");
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/*");
//                shareIntent.putExtra(Intent.EXTRA_EMAIL, "email@server.com");
////                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//              //  startActivity(Intent.createChooser(shareIntent, "Share images..."));
//
//

//               // Intent shareIntent;
//                Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.thanksimg);
//
//
//                Uri bmpUri = Uri.parse("file://"+bitmap);
//                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//                shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +getPackageName());
//                shareIntent.setType("image/png");
//               // startActivity(Intent.createChooser(shareIntent,"Share with"));
//
//                shareImage();

//                try {
//
//                    String cc = "android.resource://com.receipt.invoice.stock.sirproject/"+R.drawable.a;
//
//                    Uri imgUri=Uri.parse("android.resource://com.receipt.invoice.stock.sirproject/"+R.drawable.a);
//
//                    String company_stamp = "android.resource://com.receipt.invoice.stock.sirproject/"+R.drawable.thanksimg;
//                    //String imageUri = "drawable://" + R.drawable.thanksimg;
//
//                   // String company_stamp22 = "/sdcard/thanksimg.png";
//
//                    String imageUrl = getURLForResource(R.drawable.thanksimg);
//
//                    String company_stamp22 = getURLForResource(R.drawable.thanksimg);
//
//
//
//                    Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ "/drawable/" + "ic_launcher");
//
//
//
//                    String[] TO = {"email@server.com"};
//                    Uri uri = Uri.parse("mailto:email@server.com")
//                            .buildUpon()
//                            .appendQueryParameter("subject", "subject")
//                            .appendQueryParameter("body", "txt")
//                            .build();
//
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//
//                    Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.thanksimg);
//
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Title", null);
//                    Uri imageUri3 =  Uri.parse(path);
//
////                    File image = new File(Uri.parse("android.resource://" + getPackageName() + "/drawable/" + R.drawable.thanksimg).toString());
////                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
//
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri3);
//
//                    startActivity(Intent.createChooser(emailIntent, "Share image using"));
//
//                    // Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//
//
//                   // emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                   // startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//
//
////                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
////                    Uri screenshotUri = Uri.parse("android.resource://"+getPackageName()+"/*");
////
////                    sharingIntent.setType("mailto:email@server.com");
////                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                    //startActivity(Intent.createChooser(sharingIntent, "Share image using"));
//
//
////                    Bitmap b =BitmapFactory.decodeResource(getResources(),R.drawable.accessories);
////
////                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////                    b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
////                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Title", null);
////                    Uri imageUri3 =  Uri.parse(path);
//
////                    Intent share = new Intent(Intent.ACTION_SEND, uri);
////                    share.setType("image/jpeg");
////                    share.putExtra(Intent.EXTRA_STREAM, imageUri3);
////                    share.putExtra(Intent.EXTRA_SUBJECT, "imageUri3");
////                    share.putExtra(Intent.EXTRA_TEXT, "imageUrddfdsfi3");
////                    startActivity(Intent.createChooser(share, "Select"));
//
//                } catch (Exception e) {
//                    //e.toString();
//                }



               // createShortDialog();

//                Intent intent = new Intent(Xyz.this, ChooseTemplate.class);
//                intent.putExtra("companycolor", "#ffffff");
//               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 121);

//                        int mYear, mMonth, mDay;
//                        final Calendar c = Calendar.getInstance();
//                        mYear = c.get(Calendar.YEAR);
//                        mMonth = c.get(Calendar.MONTH);
//                        mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(Xyz.this,
//                                new DatePickerDialog.OnDateSetListener() {
//
//                                    @Override
//                                    public void onDateSet(DatePicker view, int year,
//                                                          int monthOfYear, int dayOfMonth) {
//
//
//                                        button.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//
//
//                                    }
//                                }, mYear, mMonth, mDay);
//                   //     datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                        datePickerDialog.show();
            }
        });




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
