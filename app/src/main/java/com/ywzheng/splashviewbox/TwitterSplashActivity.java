package com.ywzheng.splashviewbox;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.ywzheng.splashviewbox.view.ContentView;
import com.ywzheng.splashviewbox.view.SplashView;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TwitterSplashActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final boolean DO_XML = false;

    @InjectView(R.id.splashview)
    SplashView mSplashview;
    @InjectView(R.id.mainview)
    FrameLayout mMainView;

    private Handler mHandler = new Handler();
    private ContentView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_splash);
        ButterKnife.inject(this);
        startLoadingData();
    }

    private void startLoadingData() {
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadingDataEnded();
            }
        }, 1000 + random.nextInt(2000));
    }

    private void onLoadingDataEnded() {
        Context context = getApplicationContext();
        // now that our data is loaded we can initialize the content view
        mContentView = new ContentView(context);
        // add the content view to the background
        mMainView.addView(mContentView, 0);

        // start the splash animation
        mSplashview.splashAndDisappear(new SplashView.ISplashListener() {
            @Override
            public void onStart() {
                // log the animation start event
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "splash started");
                }
            }

            @Override
            public void onUpdate(float completionFraction) {
                // log animation update events
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
                }
            }

            @Override
            public void onEnd() {
                // log the animation end event
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "splash ended");
                }
                // free the view so that it turns into garbage
                mSplashview = null;
//                if (!DO_XML) {
//                    // if inflating from code we will also have to free the reference in MainView as well
//                    // otherwise we will leak the View, this could be done better but so far it will suffice
//                    ((MainView) mMainView).unsetSplashView();
//                }
            }
        });
    }
}
