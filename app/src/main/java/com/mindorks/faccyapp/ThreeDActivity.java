package com.mindorks.faccyapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.CamcorderProfile;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

public class ThreeDActivity extends AppCompatActivity {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<Integer> mRaws = new ArrayList<>();
    String topic;
    private ArFragment arFragment;
    int selected = 0;
    ViewRenderable name_object;
    private ModelRenderable ren1, ren2, ren3, ren4, ren5, ren6, ren7, ren8;
    private VideoRecorder videoRecorder;
    Button record;
    Dialog dialog;
    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.three_d_activity);
        videoView = findViewById(R.id.videoView);
        topic = getIntent().getStringExtra("topic");

        initImageBitmap();
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                MaterialFactory.makeOpaqueWithColor(ThreeDActivity.this, new com.google.ar.sceneform.rendering.Color(Color.RED))
                        .thenAccept(material -> {
                            ModelRenderable renderable = ShapeFactory
                                    .makeSphere(0.3f, new Vector3(0f, 0.3f, 0f), material);

                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            if (selected == 0) {
                                Toast.makeText(ThreeDActivity.this, "Please choose a model.", Toast.LENGTH_LONG).show();
                            }
                            createModel(anchorNode, selected);
                        });
            }
        });

        // Record video
        Button record = findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoRecorder == null) {
                    videoRecorder = new VideoRecorder();
                    videoRecorder.setSceneView(arFragment.getArSceneView());
                    int orientation = getResources().getConfiguration().orientation;
                    videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);
                }
                boolean isRecording = videoRecorder.onToggleRecord();
                if (isRecording) {
                    record.setBackgroundResource(R.drawable.btn_stop);
                    Toast.makeText(ThreeDActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    viewVideoRecord();
                    Toast.makeText(ThreeDActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewVideoRecord() {
        videoView.setVisibility(View.VISIBLE);

        String pathVideo = videoRecorder.getVideoPath().getPath();
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(pathVideo,
                MediaStore.Images.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        videoView.setBackgroundDrawable(bitmapDrawable);
        Animation bounceAni;
        bounceAni = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        videoView.startAnimation(bounceAni);
        bounceAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                videoView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        if (videoView.getVisibility() == View.VISIBLE) {
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogViewVideo(pathVideo);
                }
            });
        }
    }

    public void showDialogViewVideo(String pathVideo) {
        dialog = new Dialog(ThreeDActivity.this);
        dialog.setContentView(R.layout.video_record_screenshot);
        dialog.show();
        VideoView videoView = dialog.findViewById(R.id.videoView);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(pathVideo,
                MediaStore.Images.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        videoView.setBackgroundDrawable(bitmapDrawable);

        Button btnPlay = dialog.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                videoView.setVisibility(View.GONE);
                Intent intent = new Intent(ThreeDActivity.this, ViewVideoRecord.class);
                intent.putExtra("pathVideo", pathVideo);
                startActivity(intent);
            }
        });

        TextView txtClose;
        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> name_object = viewRenderable);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        ModelRenderable.builder()
                .setSource(this, mRaws.get(selected - 1))
                .build().thenAccept(modelRenderable -> {
            switch (selected) {
                case 1:
                    ren1 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren1);
                    break;
                case 2:
                    ren2 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren2);
                    break;
                case 3:
                    ren3 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren3);
                    break;
                case 4:
                    ren4 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren4);
                    break;
                case 5:
                    ren5 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren5);
                    break;
                case 6:
                    ren6 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren6);
                    break;
                case 7:
                    ren7 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren7);
                    break;
                case 8:
                    ren8 = modelRenderable;
                    handleCreateModel(node, anchorNode, ren8);
                    break;
                default:
                    break;
            }
        })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load " + mNames.get(selected - 1) + " model", Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    private void handleCreateModel(TransformableNode node, AnchorNode
            anchorNode, ModelRenderable ren) {
        node.setParent(anchorNode);
        node.setRenderable(ren);
        node.select();
        addName(anchorNode, node, mNames.get(selected - 1));
    }

    public void initImageBitmap() {
        switch (topic) {
            case "Ocean":
                mImages.add(R.drawable.crab);
                mRaws.add(R.raw.crab);
                mNames.add("crab");

                mImages.add(R.drawable.crayfish);
                mRaws.add(R.raw.crayfish);
                mNames.add("crayfish");

                mImages.add(R.drawable.dolphin);
                mRaws.add(R.raw.dolphin);
                mNames.add("dolphin");

                mImages.add(R.drawable.jellyfish);
                mRaws.add(R.raw.jellyfish);
                mNames.add("jellyfish");

                mImages.add(R.drawable.octopus);
                mRaws.add(R.raw.octopus);
                mNames.add("octopus");

                mImages.add(R.drawable.seahorse);
                mRaws.add(R.raw.seahorse);
                mNames.add("seahorse");

                mImages.add(R.drawable.shark);
                mRaws.add(R.raw.shark);
                mNames.add("shark");

                mImages.add(R.drawable.squid);
                mRaws.add(R.raw.squid);
                mNames.add("squid");

                break;

            case "Animal":
                mImages.add(R.drawable.fox);
                mRaws.add(R.raw.fox);
                mNames.add("fox");

                mImages.add(R.drawable.cat);
                mRaws.add(R.raw.cat);
                mNames.add("cat");

                mImages.add(R.drawable.dog);
                mRaws.add(R.raw.dog);
                mNames.add("dog");

                mImages.add(R.drawable.duck);
                mRaws.add(R.raw.duck);
                mNames.add("duck");

                mImages.add(R.drawable.zebra);
                mRaws.add(R.raw.zebra);
                mNames.add("sheep");

                mImages.add(R.drawable.chicken);
                mRaws.add(R.raw.chicken);
                mNames.add("chicken");

                mImages.add(R.drawable.kangaroo);
                mRaws.add(R.raw.kangaroo);
                mNames.add("kangaroo");

                mImages.add(R.drawable.cow);
                mRaws.add(R.raw.cow);
                mNames.add("cow");

                break;

            case "Plant":

                mImages.add(R.drawable.mushroom);
                mRaws.add(R.raw.mushroom);
                mNames.add("mushroom");

                mImages.add(R.drawable.chili);
                mRaws.add(R.raw.chili);
                mNames.add("chili");

                mImages.add(R.drawable.flower);
                mRaws.add(R.raw.flower);
                mNames.add("flower");

                mImages.add(R.drawable.apple);
                mRaws.add(R.raw.apple);
                mNames.add("apple");

                mImages.add(R.drawable.cactus);
                mRaws.add(R.raw.cactus);
                mNames.add("cactus");

                mImages.add(R.drawable.carrot);
                mRaws.add(R.raw.carrot);
                mNames.add("carrot");

                mImages.add(R.drawable.strawberry);
                mRaws.add(R.raw.strawberry);
                mNames.add("strawberry");

                mImages.add(R.drawable.tomato);
                mRaws.add(R.raw.tomato);
                mNames.add("tomato");

                break;

            case "Home":
                mImages.add(R.drawable.house);
                mRaws.add(R.raw.house);
                mNames.add("house");

                mImages.add(R.drawable.modernhouse);
                mRaws.add(R.raw.modernhouse);
                mNames.add("modernhouse");

                mImages.add(R.drawable.livingroom);
                mRaws.add(R.raw.livingroom);
                mNames.add("livingroom");

                mImages.add(R.drawable.couch);
                mRaws.add(R.raw.couch);
                mNames.add("couch");

                mImages.add(R.drawable.tv);
                mRaws.add(R.raw.tv);
                mNames.add("tv");

                mImages.add(R.drawable.remotecontrol);
                mRaws.add(R.raw.remotecontrol);
                mNames.add("remote control");

                mImages.add(R.drawable.strawberry);
                mRaws.add(R.raw.strawberry);
                mNames.add("strawberry");

                mImages.add(R.drawable.table);
                mRaws.add(R.raw.table);
                mNames.add("table");

                break;

            case "Person":
                mImages.add(R.drawable.breifcase);
                mRaws.add(R.raw.breifcase);
                mNames.add("breifcase");

                mImages.add(R.drawable.cap);
                mRaws.add(R.raw.cap);
                mNames.add("cap");

                mImages.add(R.drawable.flipflops);
                mRaws.add(R.raw.flipflops);
                mNames.add("flipflops");

                mImages.add(R.drawable.headphones);
                mRaws.add(R.raw.headphones);
                mNames.add("headphones");

                mImages.add(R.drawable.purse);
                mRaws.add(R.raw.purse);
                mNames.add("purse");

                mImages.add(R.drawable.sandals);
                mRaws.add(R.raw.sandals);
                mNames.add("sandals");

                mImages.add(R.drawable.sneakers);
                mRaws.add(R.raw.sneakers);
                mNames.add("sneakers");

                mImages.add(R.drawable.tshirt);
                mRaws.add(R.raw.tshirt);
                mNames.add("tshirt");

                break;

            case "Vehicle":
                mImages.add(R.drawable.bike);
                mRaws.add(R.raw.bike);
                mNames.add("bike");

                mImages.add(R.drawable.train);
                mRaws.add(R.raw.train);
                mNames.add("train");

                mImages.add(R.drawable.car);
                mRaws.add(R.raw.car);
                mNames.add("car");

                mImages.add(R.drawable.firetruck);
                mRaws.add(R.raw.firetruck);
                mNames.add("firetruck");

                mImages.add(R.drawable.helicopter);
                mRaws.add(R.raw.helicopter);
                mNames.add("helicopter");

                mImages.add(R.drawable.plane);
                mRaws.add(R.raw.plane);
                mNames.add("plane");

                mImages.add(R.drawable.boat);
                mRaws.add(R.raw.boat);
                mNames.add("boat");

                mImages.add(R.drawable.sailboat);
                mRaws.add(R.raw.sailboat);
                mNames.add("sailboat");

                break;
        }

        initRecycleView();
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this, mImages, mNames);
        recycleViewAdapter.setOnItemClick(new RecycleViewAdapter.IOnItemClick() {
            @Override
            public void onClick(int pos) {
                selected = pos + 1;
            }
        });
        recyclerView.setAdapter(recycleViewAdapter);
    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name) {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> {
                    TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
                    nameView.setLocalPosition(new Vector3(0f, model.getLocalPosition().y + 0.5f, 0));
                    nameView.setParent(anchorNode);
                    nameView.setRenderable(viewRenderable);
                    nameView.select();

                    //set text
                    TextView textView = (TextView) viewRenderable.getView();
                    textView.setText(name.toUpperCase());
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            anchorNode.setParent(null);
                        }
                    });
                });

    }
}