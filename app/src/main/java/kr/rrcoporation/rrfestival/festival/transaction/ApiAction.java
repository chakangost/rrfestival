package kr.rrcoporation.rrfestival.festival.transaction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.DetailInformation;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func4;
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
        JONGHYUN_TYPE,
        FETCH_FESTIVALS,
        FETCH_BOOKMARKS,
        GET_FESTIVAL_DETAIL
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
        if (!apiDB.isOpen()) {
            return;
        }
        String query = "select * from festival;";
        Cursor cursor = apiDB.rawQuery(query, null);
        while (cursor.moveToNext()) {
            result.add(new BodyItem(Integer.valueOf(cursor.getString(cursor.getColumnIndex("contentid"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contenttypeid"))), cursor.getString(cursor.getColumnIndex("title")), Double.valueOf(cursor.getString(cursor.getColumnIndex("lat"))), Double.valueOf(cursor.getString(cursor.getColumnIndex("lng"))), cursor.getString(cursor.getColumnIndex("addr1")), cursor.getString(cursor.getColumnIndex("firstimage"))));
        }
        cursor.close();

        for (ActionListener listener : actionListeners.get(ActionType.FETCH_BOOKMARKS)) {
            listener.onActionTriggered(result);
        }
    }

    public Observable getFestivalDetailInformation(int typeId, int contentId) {

        Observable<JsonObject> commonObservable = ApiManager.apiService.getFestivalCommonInformation(typeId, contentId);
        Observable<JsonObject> detailObservable = ApiManager.apiService.getFestivalDetailInformation(typeId, contentId);
        Observable<JsonObject> summaryObservable = ApiManager.apiService.getFestivalSummaryInformation(typeId, contentId);
        Observable<JsonObject> imageObservable = ApiManager.apiService.getFestivalImageInformation(typeId, contentId);
        return Observable.zip(commonObservable, detailObservable, summaryObservable, imageObservable, new Func4<JsonObject, JsonObject, JsonObject, JsonObject, Object>() {
            @Override
            public DetailInformation call(JsonObject jsonObject, JsonObject jsonObject2, JsonObject jsonObject3, JsonObject jsonObject4) {
                Gson gson = new Gson();
                JsonObject combinedJsonObject = new JsonObject();
                for(Map.Entry<String, JsonElement> c : jsonObject.getAsJsonObject("response").getAsJsonObject("body").getAsJsonObject("items").getAsJsonObject("item").entrySet()) {
                    combinedJsonObject.add(c.getKey(), c.getValue());
                }
                for(Map.Entry<String, JsonElement> c : jsonObject2.getAsJsonObject("response").getAsJsonObject("body").getAsJsonObject("items").getAsJsonObject("item").entrySet()) {
                    combinedJsonObject.add(c.getKey(), c.getValue());
                }
                combinedJsonObject.add("summaries", jsonObject3.getAsJsonObject("response").getAsJsonObject("body").getAsJsonObject("items").getAsJsonArray("item"));
                if("0".equals(jsonObject4.getAsJsonObject("response").getAsJsonObject("body").get("totalCount").toString())) {

                    combinedJsonObject.add("images", jsonObject4.getAsJsonObject("response").getAsJsonObject("body").getAsJsonObject("items").getAsJsonArray("item"));
                }
                return gson.fromJson(combinedJsonObject, DetailInformation.class);
                }
        });
    }

}
