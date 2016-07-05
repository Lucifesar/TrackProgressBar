package com.example.administrator.trackprogressbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class MainActivity extends Activity {

    private SeekBar myseekbar;
    private TrackProgressBar mTrackProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTrackProgressBar = (TrackProgressBar) findViewById(R.id.myprogress);
        myseekbar = (SeekBar) findViewById(R.id.myseekbar);
        mTrackProgressBar = (TrackProgressBar) findViewById(R.id.myprogress);
        mTrackProgressBar.setOnTrackProgressBarListener(new TrackProgressBar.OnTrackProgressBarListener() {
            @Override
            public void onTrackProgressChanged(TrackProgressBar mTrackProgressBar, int mProgress) {

                Log.e("OnProgress",mTrackProgressBar.getProgress()+"");
            }
        });
        myseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTrackProgressBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        simulateProgress();
    }

    private void simulateProgress() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mTrackProgressBar.setProgress(progress);
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(4000);
        animator.start();
    }
}
