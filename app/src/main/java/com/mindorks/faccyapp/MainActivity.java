/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mindorks.faccyapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {
    LinearLayout imageButton, threeDButton, cameraButton;
    private Button btnGetStarted;
    private VideoView mVideoView;
    private ViewPager viewPager;
//    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private TextView nameApp;
    Intent intent;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        final String detail;
        setContentView(R.layout.activity_main);

        imageButton = findViewById( R.id.imageButton );
        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dirAct = new Intent(MainActivity.this, DirectoryActivity.class);
                startActivity( dirAct );
            }
        } );
        threeDButton = findViewById( R.id.threeDButton );
        threeDButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modAct = new Intent(MainActivity.this, ListTopic3DActivity.class);
//                Intent modAct = new Intent(MainActivity.this, ViewVideoRecord.class);
                startActivity( modAct );
            }}
        );
        cameraButton =  findViewById( R.id.cameraButton );
        cameraButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modAct = new Intent(MainActivity.this, CameraActivity.class);
                startActivity( modAct );
            }}
        );
    }
}