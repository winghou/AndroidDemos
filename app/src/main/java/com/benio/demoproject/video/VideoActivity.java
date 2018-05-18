package com.benio.demoproject.video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.benio.demoproject.R;

import java.io.File;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";
    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 0x0101;
    private VideoView mVideoView;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        findViewById(R.id.btn_system_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                final File videoFile = new File(dir, "video1.mp4");
                if (!videoFile.exists()) {
                    Log.e(TAG, "File: " + videoFile.getAbsolutePath() + " do not exist");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(videoFile.getAbsolutePath()), "video/mp4");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.btn_texture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoActivity.this, TextureViewActivity.class);
                startActivity(intent);
            }
        });


        mVideoView = (VideoView) findViewById(R.id.video);
        MediaController mediaController = new MediaController(VideoActivity.this);
        mediaController.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(mediaController);

        findViewById(R.id.btn_local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    mUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.small);
                    startVideo();
                }
            }
        });
        findViewById(R.id.btn_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUri = Uri.parse("http://www.html5videoplayer.net/videos/toystory.mp4");
                startVideo();
            }
        });
    }

    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVideo();
                }
        }
    }

    private void startVideo() {
        final VideoView videoView = mVideoView;
        Uri uri = mUri;
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "Media file is loaded");
                videoView.requestFocus();
                videoView.start();
            }
        });
    }
}
