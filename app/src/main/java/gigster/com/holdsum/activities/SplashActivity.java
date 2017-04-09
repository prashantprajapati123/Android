package gigster.com.holdsum.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.Logger;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_LENGTH = 800;

    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "SplashActivity.onCreate");
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(endSplashRunnable, SPLASH_LENGTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(endSplashRunnable);
    }

    private final Runnable endSplashRunnable = new Runnable() {
        @Override
        public void run() {
            FlowController.getInstance().setTo(SplashActivity.this, FlowController.Screen.ENTRANCE);
        }
    };

}
