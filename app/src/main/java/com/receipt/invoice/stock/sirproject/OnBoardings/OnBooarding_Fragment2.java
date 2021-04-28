package com.receipt.invoice.stock.sirproject.OnBoardings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.receipt.invoice.stock.sirproject.R;

public class OnBooarding_Fragment2 extends Fragment {

    public OnBooarding_Fragment2() {
        // Required empty public constructor
    }


    TextView txtonboardheading2,txtonboardingdes2;
    ImageView imgonboard2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_on_booarding__fragment2, container, false);
        txtonboardheading2 = view.findViewById(R.id.txtonboardheading2);
        txtonboardingdes2 = view.findViewById(R.id.txtonboardingdes2);
        imgonboard2 = view.findViewById(R.id.imgonboard2);
        fonts();

        return view;
    }


    public void fonts()
    {
        txtonboardheading2.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtonboardingdes2.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Ubuntu-Light.ttf"));

    }

}
