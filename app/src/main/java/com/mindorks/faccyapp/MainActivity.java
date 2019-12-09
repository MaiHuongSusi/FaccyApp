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
    private PrefManager prefManager;
    private TextView nameApp;
    Intent intent;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager( this );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        final String detail;
        setContentView(R.layout.activity_main);

//        nameApp = (TextView) findViewById( R.id.nameApp );
//        Typeface myCustomFont = Typeface.createFromAsset( getAssets(), "font/comic2.ttf" );
//        nameApp.setTypeface( myCustomFont );
//
//        mVideoView = (VideoView) findViewById(R.id.mVideoView);
//        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg_video_init);
//        mVideoView.setVideoURI(uri);
//        mVideoView.start();
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setLooping(true);
//            }
//        });
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
//        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
//        // layouts of all welcome sliders
//        // add few more layouts if you want
//        layouts = new int[]{
//                R.layout.slider1,
//                R.layout.slider2,
//                R.layout.slider3,
//                R.layout.slider4,
//        };
//        // adding bottom dots
//        addBottomDots(0);
//        // making notification bar transparent
//        changeStatusBarColor();
//        myViewPagerAdapter = new MyViewPagerAdapter();
//        viewPager.setAdapter(myViewPagerAdapter);
//        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

//        btnGetStarted = findViewById(R.id.btnGetStarted);
//        btnGetStarted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
//                startActivity(intent);
//            }
//        });
        imageButton = findViewById( R.id.imageButton );
        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dirAct = new Intent(MainActivity.this, DirectoryActivity.class);
                //dirAct.putExtra("isActiveDescription", String.valueOf(isActiveDescription));
                startActivity( dirAct );
            }
        } );
        threeDButton = findViewById( R.id.threeDButton );
        threeDButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modAct = new Intent(MainActivity.this, ThreeDObjectActivity.class);
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

//    private void addBottomDots(int currentPage) {
//        dots = new TextView[layouts.length];
//        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
//        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText( Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(colorsInactive[currentPage]);
//            dotsLayout.addView(dots[i]);
//        }
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
//    }
//    private int getItem(int i) {
//        return viewPager.getCurrentItem() + i;
//    }
//
//    //  viewpager change listener
//    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageSelected(int position) {
//            addBottomDots(position);
//        }
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//    };
//
//    /**
//     * Making notification bar transparent
//     */
//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor( Color.TRANSPARENT);
//        }
//    }
//
//    public class MyViewPagerAdapter extends PagerAdapter {
//        private LayoutInflater layoutInflater;
//        public MyViewPagerAdapter() {
//        }
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE);
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//            return view;
//        }
//        @Override
//        public int getCount() {
//            return layouts.length;
//        }
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mVideoView.start();
//    }
}