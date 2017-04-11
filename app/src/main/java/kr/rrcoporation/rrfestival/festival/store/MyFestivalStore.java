package kr.rrcoporation.rrfestival.festival.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kr.rrcoporation.rrfestival.festival.application.SubscriptionListener;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import rx.Observable;

public class MyFestivalStore {
    private            List<SubscriptionListener<Void>> observers;
    private static MyFestivalStore                      instance;
    private List<BodyItem> festivals;
    private FestivalResult festivalResult;

    public static MyFestivalStore getInstance() {
        if (instance == null) {
            instance = new MyFestivalStore();
        }
        return instance;
    }

    public Observable<Void> observe() {
        SubscriptionListener<Void> subscriptionListener = new SubscriptionListener<>();
        this.observers.add(subscriptionListener);
        return Observable.create(subscriptionListener);
    }

    public List<BodyItem> getFestivals() {
        return this.festivals;
    }

    public FestivalResult getFestivalResult() {
        return this.festivalResult;
    }

    private MyFestivalStore() {
        festivals = new ArrayList<>();
        observers = new LinkedList<>();
        ApiAction.getInstance().subscribe(ApiAction.ActionType.FETCH_FESTIVALS, new ApiAction.ActionListener<FestivalResult>() {
            @Override
            public void onActionTriggered(FestivalResult result) {
                festivalResult = result;
                festivals = new LinkedList<>(Arrays.asList(result.getResponse().getBody().getItems().getItem()));
                List<SubscriptionListener> unsubscribedObservers = new ArrayList<>();
                for(SubscriptionListener<Void> observer : observers) {
                    if (observer.isUnsubscribed()) {
                        unsubscribedObservers.add(observer);
                    } else {
                        observer.emit(null);
                    }
                }
                observers.removeAll(unsubscribedObservers);
            }
        });
    }

}
