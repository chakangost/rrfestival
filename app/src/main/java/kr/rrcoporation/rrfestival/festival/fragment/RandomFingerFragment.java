package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.wenchao.cardstack.CardStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import kr.rrcoporation.rrfestival.festival.store.MyFestivalStore;
import kr.rrcoporation.rrfestival.festival.util.Util;
import kr.rrcoporation.rrfestival.festival.view.CardsDataAdapter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class RandomFingerFragment extends CommonFragment {

    private RelativeLayout   rootLayout;
    private CardStack        mCardStack;
    private CardsDataAdapter mCardAdapter;
    private Subscription     subscription;
    private static List<BodyItem>                  bodyItems;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_random_finger, null);
        if (Util.getSharedPreference(getContext(), "FESTIVAL_LIST").equals("")) {
            observeFestivalStore();
        } else {
            String festivalGsonStr = Util.getSharedPreference(getContext(), "FESTIVAL_LIST");
            FestivalResult festivalItem = gson.fromJson(festivalGsonStr, FestivalResult.class);
            bodyItems = new LinkedList<>(Arrays.asList(festivalItem.getResponse().getBody().getItems().getItem()));

            mCardStack = (CardStack) rootLayout.findViewById(R.id.container);
            mCardStack.setContentResource(R.layout.festival_detail);

            mCardStack.setStackMargin(20);

//            for (BodyItem bodyItem : bodyItems) {
//
//            }

            mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), bodyItems);
            mCardStack.setAdapter(mCardAdapter);
        }
        return rootLayout;
    }

    private void observeFestivalStore() {
        subscription = MyFestivalStore.getInstance().observe().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                List<BodyItem> festivals = MyFestivalStore.getInstance().getFestivals();
                mCardStack = (CardStack) rootLayout.findViewById(R.id.container);
                mCardStack.setContentResource(R.layout.festival_detail);

                mCardStack.setStackMargin(20);

                mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), festivals);
                mCardStack.setAdapter(mCardAdapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
