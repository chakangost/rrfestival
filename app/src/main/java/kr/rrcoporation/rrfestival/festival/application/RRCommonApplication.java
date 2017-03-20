package kr.rrcoporation.rrfestival.festival.application;

import android.app.Application;

import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.transaction.ApiManager;

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
        ApiManager.init(this);
        ApiAction.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ourInstance = null;
    }
}
