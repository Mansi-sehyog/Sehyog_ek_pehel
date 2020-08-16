package com.muskan.sehyog_ekpehel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class ClothesFragment extends Fragment {

    public ClothesFragment(){
        //Required empty public constructor
    }

    ImageButton ImageButtonClothes, ImageButtonFootWare;
    private FragmentActivity myContext;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_clothes,container,false);

        ImageButtonClothes = view.findViewById(R.id.imageButtonClothes);

//        ImageView ImageView = view.findViewById(R.id.ImageClothes);
        Toast.makeText(myContext, "click on screen for donating clothes..", Toast.LENGTH_LONG).show();

        ImageButtonClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
/*
        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });*/
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void showDialog() {
        FragmentManager fm = myContext.getSupportFragmentManager();
        ClothesDialog editNameDialogFragment = ClothesDialog.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }



}
