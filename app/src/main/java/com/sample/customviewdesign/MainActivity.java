package com.sample.customviewdesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sample.customviewdesign.widget.InflateLoadingCircle;

public class MainActivity extends AppCompatActivity {

    private InflateLoadingCircle mAnimateInflateCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAnimateInflateCircle = findViewById(R.id.animate_inflate_circle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimateInflateCircle.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onResume();
        mAnimateInflateCircle.stopAnmation();
    }
}
