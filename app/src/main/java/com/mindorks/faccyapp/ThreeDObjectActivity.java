package com.mindorks.faccyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThreeDObjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_3d_object);
    }

    public void show3DObject(View view) {
        Intent intent = new Intent(ThreeDObjectActivity.this, ThreeModelElectionActivity.class);
        startActivity(intent);
    }

    public void showAnimation(View view) {
        Intent intent = new Intent(ThreeDObjectActivity.this, ListAnimationActivity.class);
        startActivity(intent);
    }
}
