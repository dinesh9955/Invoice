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


public class OnBooarding_Fragment1 extends Fragment {


    public OnBooarding_Fragment1() {
        // Required empty public constructor
    }



    TextView txtonboardheading1,txtonboardingdes1;
    ImageView imgonboard1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_on_booarding__fragment1, container, false);
        txtonboardheading1 = view.findViewById(R.id.txtonboardheading1);
        txtonboardingdes1 = view.findViewById(R.id.txtonboardingdes1);
        imgonboard1 = view.findViewById(R.id.imgonboard1);

        fonts();

        return view;

    }

    public void fonts()
    {
        txtonboardheading1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtonboardingdes1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Ubuntu-Light.ttf"));

    }
}
