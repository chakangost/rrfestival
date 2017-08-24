package kr.rrcoporation.rrfestival.festival.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.store.MyFestivalStore;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SplashActivity extends CommonFragmentActivity {

    private static final int DELAYED_TIME = 2000;

    private Activity thisActivity;
    private long startTime;
    private long endTime;

    private boolean isComplete = false;
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
        thisActivity = this;
        startTime = System.currentTimeMillis();

        MyFestivalStore.getInstance().observe().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Void aVoid) {
                endTime = System.currentTimeMillis();
                if(endTime - startTime >= DELAYED_TIME) {
                    callActivity();
                } else {
                    isComplete = true;
                }
            }
        });

        ApiAction.getInstance().fetchFestivals();

        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isComplete) {
                    callActivity();
                }
            }
        }, DELAYED_TIME);
    }

    private void callActivity() {
        startActivity(new Intent(SplashActivity.this, FragmentContainerActivity.class));
        finish();
    }
}
