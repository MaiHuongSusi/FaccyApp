package com.mindorks.faccyapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.ImageReader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.Trace;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity extends AppCompatActivity implements ImageReader.OnImageAvailableListener, CameraFragment.ConnectionCallback, TextToSpeech.OnInitListener {

    public static final String TAG = "CameraActivity";
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final float TEXT_SIZE_DIP = 10;
    private Handler handler;
    private HandlerThread handlerThread;
    private Integer sensorOrientation;
    private int previewWidth = 0;
    private int previewHeight = 0;
    private byte[][] yuvBytes;
    private int[] rgbBytes = null;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap;
    private boolean computing = false;
    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private TextView resultsView;
    private long lastProcessingTimeMs;
    private TensorFlowImageClassifier2 classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextToSpeech tts;
    private LinearLayout imageSpeaker, imageVideo, imageDetail;
    RelativeLayout container;
    Dialog dialog;
    Uri imageUri;
    String detail;
    private StorageReference mStorageRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        container = findViewById(R.id.container);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        setFragment();

        dialog = new Dialog(this);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                tts.stop();
            }
        });
    }

    protected void setFragment() {
        final Fragment fragment = new CameraFragment(this);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    // Set up Text To Speech
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                imageSpeaker.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    protected void onPause() {
        if (!isFinishing()) {
            finish();
        }
        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {

        classifier = new TensorFlowImageClassifier2(getAssets(), MODEL_FILE, LABEL_FILE, INPUT_SIZE, IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME);

        resultsView = (TextView) findViewById(R.id.nameResult);
        tts = new TextToSpeech(this, this);
        imageSpeaker = findViewById(R.id.imageSpeaker);
        imageDetail = findViewById(R.id.imageDetail);
        imageVideo = findViewById(R.id.imageVideo);
        imageSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });

        imageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail();
            }
        });

        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    showVideo();
                } else {
                    Toast.makeText(CameraActivity.this, "Please connect internet to view video.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setUpPreviewImage(size, rotation);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void setUpPreviewImage(final Size size, final int rotation) {
        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        final Display display = getWindowManager().getDefaultDisplay();
        final int screenOrientation = display.getRotation();

        sensorOrientation = rotation + screenOrientation;

        rgbBytes = new int[previewWidth * previewHeight];
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        INPUT_SIZE, INPUT_SIZE,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        yuvBytes = new byte[3][];
    }

    private void showVideo() {
        if (detail != "[Not Classify]" && detail != "Not Classify") {
            String codeObject;
            if (detail.contains(" ")) {
                codeObject = detail.substring(1, detail.indexOf(" ")).toLowerCase().concat("_").concat(detail.substring(detail.indexOf(" ") + 1, detail.indexOf("]")).toLowerCase());
            } else {
                codeObject = detail.substring(1, detail.indexOf("]")).toLowerCase();
            }
            mStorageRef.child(codeObject + "/" + codeObject + ".mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Intent intent = new Intent(CameraActivity.this, PlayVideoInArScene.class);
                    intent.putExtra("object", detail);
                    intent.putExtra("pathVideo", uri.toString());
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CameraActivity.this, "Fail to load video.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showDetail() {
        dialog.setContentView(R.layout.dialog_description);
        dialog.show();
        final TextView textDes;
        textDes = (TextView) dialog.findViewById(R.id.textDes);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("imagenet_comp_graph_label_strings1.txt"), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.contains(detail)) {
                    textDes.setText(mLine);
                    break;
                } else {
                    textDes.setText(" Don't have Description!");
                }
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        TextView txtClose;
        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        CircleImageView imageView1 = (CircleImageView) dialog.findViewById(R.id.imageView);
        imageView1.setImageURI(imageUri);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tts.isSpeaking())
                    tts.stop();
                dialog.dismiss();
            }
        });

        ImageView imgBtnSpeaker;
        imgBtnSpeaker = (ImageView) dialog.findViewById(R.id.imgBtnSpeaker);

        imgBtnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textDes.getText().toString();
                tts.setSpeechRate((float) 0.7);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void speakOut() {
        String text = resultsView.getText().toString();
        tts.setSpeechRate((float) 0.7);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    protected synchronized void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    @Override
    public void onImageAvailable(final ImageReader reader) {
        Image image = null;

        try {
            image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (computing) {
                image.close();
                return;
            }
            computing = true;

            Trace.beginSection("imageAvailable");

            final Image.Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);

            final int yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            ImageUtils.convertYUV420ToARGB8888(
                    yuvBytes[0],
                    yuvBytes[1],
                    yuvBytes[2],
                    previewWidth,
                    previewHeight,
                    yRowStride,
                    uvRowStride,
                    uvPixelStride,
                    rgbBytes);

            image.close();
        } catch (final Exception e) {
            if (image != null) {
                image.close();
            }
            Trace.endSection();
            return;
        }

        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);


        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (results.size() > 0) {
                                    detail = results.toString();
                                    resultsView.setText(detail);
                                } else {
                                    resultsView.setText("Not Classify");
                                }
                            }
                        });
                        computing = false;
                    }

                });

        Trace.endSection();
    }


    private void fillBytes(Image.Plane[] planes, byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }
}
