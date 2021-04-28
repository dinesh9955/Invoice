package com.receipt.invoice.stock.sirproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Invoice.ChooseTemplate;
import com.receipt.invoice.stock.sirproject.Invoice.Fragment_Create_Invoice;

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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createShortDialog();

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
