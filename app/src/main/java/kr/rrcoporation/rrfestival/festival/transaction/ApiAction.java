package kr.rrcoporation.rrfestival.festival.transaction;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ApiAction {
    private static ApiAction  instance;
    private        Context    context;
    public static  ApiService apiService;
    private String serviceKey = "n4HqoC9EFsrq1stLyXelZtz4GPjTgjinWix/IT93c9Vr3bP+WA+zgOirr0AmIaGnSGkCiWgHV0YajENvv9vY6w==";

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
        JONGHYUN_TYPE, FETCH_FESTIVALS
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
        ApiManager.apiService.fetchFestivalData(serviceKey, "", "ETC", "AppTesting", "json", "2000", "A02", "A0207")
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

}
