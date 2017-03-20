package kr.rrcoporation.rrfestival.festival.fragment;

import android.app.Application;

public class RRCommonApplication extends Application {

    private static RRCommonApplication ourInstance = new RRCommonApplication();

    public static RRCommonApplication getInstance() {
        return ourInstance;
    }

    private RRCommonApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ourInstance = null;
    }
}
