package kr.rrcoporation.rrfestival.festival.application;

import rx.Observable;
import rx.Subscriber;

public class SubscriptionListener<T> implements Observable.OnSubscribe<T> {

    private Subscriber observer;

    public boolean isUnsubscribed() {
        return observer.isUnsubscribed();
    }

    public void emit(T result) {
        try {
            if (!observer.isUnsubscribed()) {
                observer.onNext(result);
            }
        } catch (Exception e) {
            observer.onError(e);
        }
    }

    @Override
    public void call(Subscriber<? super T> observer) {
        this.observer = observer;
    }
}
