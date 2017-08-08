package kr.rrcoporation.rrfestival.festival.application;

import android.app.Activity;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import kr.rrcoporation.rrfestival.festival.BuildConfig;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.transaction.ApiManager;

public class RRApplication extends MultiDexApplication {

    private static volatile RRApplication instance = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.VmPolicy policy = new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setVmPolicy(policy);
        }
        MobileAds.initialize(this, "ca-app-pub-8748559512063133~2710715408");
        instance = this;
        ApiManager.init();
        ApiAction.init(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        RRApplication.currentActivity = currentActivity;
    }

    public static RRApplication getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }
}
