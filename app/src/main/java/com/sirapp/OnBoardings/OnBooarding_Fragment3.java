package com.sirapp.OnBoardings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sirapp.R;


public class OnBooarding_Fragment3 extends Fragment {

    public OnBooarding_Fragment3() {
        // Required empty public constructor
    }


    TextView txtonboardheading3,txtonboardingdes3;
    ImageView imgonboard3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_on_booarding__fragment3, container, false);
        txtonboardheading3 = view.findViewById(R.id.txtonboardheading3);
        txtonboardingdes3 = view.findViewById(R.id.txtonboardingdes3);
        imgonboard3 = view.findViewById(R.id.imgonboard3);

        fonts();


        return view;
    }

    public void fonts()
    {
        txtonboardheading3.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtonboardingdes3.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Ubuntu-Light.ttf"));
    }
}
