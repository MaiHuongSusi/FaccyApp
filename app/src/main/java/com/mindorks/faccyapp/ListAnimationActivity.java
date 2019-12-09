package com.mindorks.faccyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ListAnimationActivity extends AppCompatActivity {

    Button btnSolar, btnOther;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_menu);
    }

    public void showSolarSystem(View view) {
//        Intent intent = new Intent(ListAnimationActivity.this, SolarActivity.class);
//        startActivity(intent);
    }

    public void showOtherAnimation(View view) {
//        Intent intent = new Intent(ListAnimationActivity.this, ListOtherAnimationActivity.class);
//        startActivity(intent);
    }

}
