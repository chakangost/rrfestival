package kr.rrcoporation.rrfestival.festival.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import kr.rrcoporation.rrfestival.festival.R;

public class SplashActivity extends CommonFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
