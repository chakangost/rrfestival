package kr.rrcoporation.rrfestival.festival.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.util.Util;

public class SplashActivity extends CommonFragmentActivity {

    private Activity thisActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        thisActivity = this;
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Util.getBooleanPreference(thisActivity, "autoLogin")) {
                    startActivity(new Intent(SplashActivity.this, FragmentContainerActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}
