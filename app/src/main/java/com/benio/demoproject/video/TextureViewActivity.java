package com.benio.demoproject.video;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;

import com.benio.demoproject.R;

import java.io.IOException;

/**
 * Created by zhangzhibin on 2018/5/18.
 */
public class TextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private static final String TAG = "TextureViewActivity";
    private MediaPlayer mMediaPlayer;
    private TextureView mTextureView;
    private Uri mUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        setContentView(mTextureView);
        mUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.small);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, final int height) {
        Log.d(TAG, "onSurfaceTextureAvailable() called with: surface = [" + surface + "], width = [" + width + "], height = [" + height + "]");
        Surface s = new Surface(surface);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, mUri);
            mMediaPlayer.setSurface(s);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int nWidth = width;
                    int nHeight = nWidth * mMediaPlayer.getVideoHeight() / mMediaPlayer.getVideoWidth();
                    Log.d(TAG, "onPrepared: " + mMediaPlayer.getVideoWidth() + "," + mMediaPlayer.getVideoHeight() + "," +
                            nWidth + "," + nHeight);
                    ViewGroup.LayoutParams p = mTextureView.getLayoutParams();
                    p.width = width;
                    p.height = nHeight;
                    mTextureView.setLayoutParams(p);
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
