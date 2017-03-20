package kr.rrcoporation.rrfestival.festival.application;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static final ActivityManager activityManager = new ActivityManager();
    private              List<Activity>  listActivity    = null;

    private ActivityManager() {
        listActivity = new ArrayList<>();
    }

    public static ActivityManager getInstance() {
        return activityManager;
    }

    public void addActivity(Activity activity) {
        listActivity.add(activity);
    }

    public boolean removeActivity(Activity activity) {
        return listActivity.remove(activity);
    }

    public void finishAllActivity() {
        for (Activity activity : listActivity) {
            activity.finish();
        }
    }

    public List<Activity> getListActivity() {
        return listActivity;
    }

    public void setListActivity(List<Activity> listActivity) {
        this.listActivity = listActivity;
    }
}
