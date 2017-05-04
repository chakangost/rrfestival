package kr.rrcoporation.rrfestival.festival.store;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import kr.rrcoporation.rrfestival.festival.application.SubscriptionListener;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import rx.Observable;

public class MyBookmarkStore {
    private        List<SubscriptionListener<Void>> observers;
    private static MyBookmarkStore                  instance;
    private        List<BodyItem>                   bookmarks;

    public static MyBookmarkStore getInstance() {
        if (instance == null) {
            instance = new MyBookmarkStore();
        }
        return instance;
    }

    public Observable<Void> observe() {
        SubscriptionListener<Void> subscriptionListener = new SubscriptionListener<>();
        this.observers.add(subscriptionListener);
        return Observable.create(subscriptionListener);
    }

    public List<BodyItem> getBookmarks() {
        return this.bookmarks;
    }

    private MyBookmarkStore() {
        bookmarks = new ArrayList<>();
        observers = new LinkedList<>();
        ApiAction.getInstance().subscribe(ApiAction.ActionType.FETCH_BOOKMARKS, new ApiAction.ActionListener<List<BodyItem>>() {
            @Override
            public void onActionTriggered(List<BodyItem> result) {
                bookmarks = result;
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
