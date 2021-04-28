package com.receipt.invoice.stock.sirproject.Invoice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.receipt.invoice.stock.sirproject.R;


public class TemplateInvoice extends Fragment {


    public TemplateInvoice() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template_invoice, container, false);
    }
}