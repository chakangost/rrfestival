package kr.rrcoporation.rrfestival.festival.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import kr.rrcoporation.rrfestival.festival.application.ActivityManager;

public class CommonFragmentActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();
    public ActivityManager getActManager() {
        return actManager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
