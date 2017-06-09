package kr.rrcoporation.rrfestival.festival.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.util.Util;

public class SplashActivity extends CommonFragmentActivity {

    private Activity thisActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_anim);
        findViewById(R.id.splash_image).startAnimation(anim);

        thisActivity = this;
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Util.getBooleanPreference(thisActivity, "autoLogin")) {
                    startActivity(new Intent(SplashActivity.this, FragmentContainerActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
