package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.wenchao.cardstack.CardStack;
import java.util.ArrayList;
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

public class RandomFingerFragment extends CommonFragment implements View.OnClickListener {

    private        RelativeLayout   rootLayout;
    private        CardStack        mCardStack;
    private        CardsDataAdapter mCardAdapter;
    private        Subscription     subscription;
    private static List<BodyItem>   bodyItems;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_random_finger, null);
        rootLayout.findViewById(R.id.btn_add_favorite).setOnClickListener(this);
        if (Util.getSharedPreference(getContext(), "FESTIVAL_LIST").equals("")) {
            observeFestivalStore();
        } else {
            String festivalGsonStr = Util.getSharedPreference(getContext(), "FESTIVAL_LIST");
            FestivalResult festivalItem = gson.fromJson(festivalGsonStr, FestivalResult.class);
            bodyItems = new LinkedList<>(Arrays.asList(festivalItem.getResponse().getBody().getItems().getItem()));

            mCardStack = (CardStack) rootLayout.findViewById(R.id.container);
            mCardStack.setContentResource(R.layout.festival_detail);
            mCardStack.setListener(new CardStack.CardEventListener() {

                @Override
                public boolean swipeEnd(int i, float v) {
                    return true;
                }

                @Override
                public boolean swipeStart(int i, float v) {
                    return true;
                }

                @Override
                public boolean swipeContinue(int i, float v, float v1) {
                    return true;
                }

                @Override
                public void discarded(int i, int i1) {
                    Toast.makeText(getActivity(), "" + bodyItems.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void topCardTapped() {
                }
            });

            mCardStack.setStackMargin(20);

            List<BodyItem> festivals = new ArrayList<>();
            for (BodyItem bodyItem : bodyItems) {
                if (bodyItem.getFirstimage() != null) {
                    festivals.add(bodyItem);
                }
            }

            mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), festivals);
            mCardStack.setAdapter(mCardAdapter);
        }
        return rootLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_favorite:
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                break;
        }
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
