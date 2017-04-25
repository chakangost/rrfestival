package kr.rrcoporation.rrfestival.festival.transaction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ApiAction {
    private static ApiAction      instance;
    private        Context        context;
    public static  ApiService     apiService;
    private        SQLiteDatabase apiDB;
    private ApiAction(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance != null) {
            throw new IllegalStateException("Already inited");
        }
        instance = new ApiAction(context);
    }

    public static ApiAction getInstance() {
        return instance;
    }

    public void subscribe(ActionType type, ActionListener listener) {
        List<ActionListener> listeners = this.actionListeners.get(type);
        listeners.add(listener);
    }

    private Map<ActionType, List<ActionListener>> actionListeners = new HashMap<ActionType, List<ActionListener>>() {
        @Override
        public List<ActionListener> get(Object key) {
            if (!containsKey(key)) {
                List<ActionListener> blankList = new ArrayList<>();
                put((ActionType) key, blankList);
                return blankList;
            }
            return super.get(key);
        }
    };

    public interface ActionListener<T> {
        void onActionTriggered(T response);
    }

    public enum ActionType {
        JONGHYUN_TYPE, FETCH_FESTIVALS, FETCH_BOOKMARKS
    }

    public void fetchTestApi() {
        apiService.testapi("", "").subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException && ((HttpException) e).code() == 401) {
                    Log.e("", e.toString());
                    return;
                }
                e.printStackTrace();
            }

            @Override
            public void onNext(Void result) {
                for (ActionListener listener : actionListeners.get(ActionType.JONGHYUN_TYPE)) {
                    listener.onActionTriggered(result);
                }
            }
        });
    }

    public void fetchFestivals() {
        ApiManager.apiService.fetchFestivalData("", "ETC", "AppTesting", "json", "2000", "A02", "A0207")
                .subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<FestivalResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException && ((HttpException) e).code() == 401) {
                    Log.e("", e.toString());
                    return;
                }
                e.printStackTrace();
            }

            @Override
            public void onNext(FestivalResult result) {
                for (ActionListener listener : actionListeners.get(ActionType.FETCH_FESTIVALS)) {
                    listener.onActionTriggered(result);
                }
            }
        });
    }

    public void fetchBookmarks() {
        List<BodyItem> result = new ArrayList<>();
        apiDB = context.openOrCreateDatabase("FESTIVALS.db", Context.MODE_PRIVATE, null);
        String query = "select * from festival;";
        Cursor cursor = apiDB.rawQuery(query, null);
        while (cursor.moveToNext()) {
            result.add(new BodyItem(Integer.valueOf(cursor.getString(cursor.getColumnIndex("contentid"))), cursor.getString(cursor.getColumnIndex("title")), Double.valueOf(cursor.getString(cursor.getColumnIndex("lat"))), Double.valueOf(cursor.getString(cursor.getColumnIndex("lng"))), cursor.getString(cursor.getColumnIndex("addr1")), cursor.getString(cursor.getColumnIndex("firstimage"))));
        }

        for (ActionListener listener : actionListeners.get(ActionType.FETCH_BOOKMARKS)) {
            listener.onActionTriggered(result);
        }
    }

}
