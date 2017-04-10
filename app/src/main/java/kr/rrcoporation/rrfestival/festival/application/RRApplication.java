package kr.rrcoporation.rrfestival.festival.application;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import kr.rrcoporation.rrfestival.festival.BuildConfig;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.transaction.ApiManager;

public class RRApplication extends MultiDexApplication {

    private static volatile RRApplication instance = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
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
