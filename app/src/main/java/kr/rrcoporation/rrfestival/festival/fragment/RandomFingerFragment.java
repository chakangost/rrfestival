package kr.rrcoporation.rrfestival.festival.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.wenchao.cardstack.CardStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.activity.FestivalDetailActivity;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.ExtraConstants;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import kr.rrcoporation.rrfestival.festival.store.MyFestivalStore;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.view.CardsDataAdapter;
import rx.Subscription;

public class RandomFingerFragment extends CommonFragment implements View.OnClickListener, CardStack.CardEventListener {

    private RelativeLayout   rootLayout;
    private CardStack        mCardStack;
    private CardsDataAdapter mCardAdapter;
    private Subscription     subscription;
    private SQLiteDatabase   db;
    private String TABLE = "festival";
    private int            festivalsPosition;
    private List<BodyItem> bodyItems;
    private Gson gson = new Gson();
    private InterstitialAd mInterstitialAd;
    private int avdShowingCnt = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createDB();
        rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_random_finger, null);
//        MobileAds.initialize(getActivity(), "ca-app-pub-8748559512063133~2710715408");
        MobileAds.initialize(getActivity(), "ca-app-pub-8076266735501767~7399323896"); // 구글 개발자 계정 애드몹 연동
        mInterstitialAd = new InterstitialAd(getActivity());
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // 테스트 전면광고
//        mInterstitialAd.setAdUnitId("ca-app-pub-8748559512063133/3229590156"); // 우리꺼
        mInterstitialAd.setAdUnitId("ca-app-pub-8076266735501767/3076935501"); // 구글 개발자 계정 애드몹 연동
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        rootLayout.findViewById(R.id.btn_add_favorite).setOnClickListener(this);
        rootLayout.findViewById(R.id.current_position_btn).setOnClickListener(this);
        settingCards();
        return rootLayout;
    }

    @Override
    public void topCardTapped() {
        Intent intent = new Intent(getActivity(), FestivalDetailActivity.class);
        BodyItem item = bodyItems.get(festivalsPosition);
        intent.putExtra(ExtraConstants.EXTRA_ITEM, item);
        intent.putExtra(ExtraConstants.EXTRA_CONTENT_TYPE_ID, item.getContenttypeid());
        intent.putExtra(ExtraConstants.EXTRA_CONTENT_ID, item.getContentid());
        startActivity(intent);
    }

    @Override
    public void discarded(int i, int i1) {
        festivalsPosition = i;
    }

    @Override
    public boolean swipeContinue(int i, float v, float v1) {
        return true;
    }

    @Override
    public boolean swipeStart(int i, float v) {
        return true;
    }

    @Override
    public boolean swipeEnd(int i, float v) {
        avdShowingCnt++;
        if (avdShowingCnt > 5) {
            mInterstitialAd.show();
            avdShowingCnt = 0;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_favorite:
                saveBookmark();
                break;
            case R.id.current_position_btn:
                MapFragment.newInstance(bodyItems.get(festivalsPosition).getMapy(), bodyItems.get(festivalsPosition).getMapx(), "FROM_FINGER");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void settingCards() {
        List<BodyItem> festivals = MyFestivalStore.getInstance().getFestivals();
        Collections.shuffle(festivals);
        bodyItems = new ArrayList<>();
        mCardStack = (CardStack) rootLayout.findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.festival_detail);
        mCardStack.setListener(this);
        mCardStack.setListener(this);
        mCardStack.setStackMargin(20);

        for (BodyItem bodyItem : festivals) {
            if (bodyItem.getFirstimage() != null) {
                bodyItems.add(bodyItem);
            }
        }

        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), bodyItems);
        mCardStack.setAdapter(mCardAdapter);
    }

    private void saveBookmark() {
        String existDataQuery = "select * from festival where contentid = '" + bodyItems.get(festivalsPosition).getContentid() + "';";
        Cursor cursor = db.rawQuery(existDataQuery, null);

        int columnCnt = cursor.getCount();
        cursor.close();
        if (columnCnt != 0) {
            Toast.makeText(getActivity(), "이미 추가 되어있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String tableName = "festival";
        String colums = "contentid, title, eventenddate, lat, lng, contenttypeid, addr1, tel, firstimage";
        String str1 = "INSERT INTO " + tableName + " (" + colums + ") values(";
        String str2 = ");";

        db.beginTransaction();

        StringBuilder sb = new StringBuilder(str1);
        sb.append("'" + bodyItems.get(festivalsPosition).getContentid() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getTitle() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getEventenddate() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getMapy() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getMapx() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getContenttypeid() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getAddr1() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getTel() + "',");
        sb.append("'" + bodyItems.get(festivalsPosition).getFirstimage() + "'");
        sb.append(str2);

        db.execSQL(sb.toString());
        db.setTransactionSuccessful();
        db.endTransaction();

        ApiAction.getInstance().fetchBookmarks();
        Toast.makeText(getActivity(), bodyItems.get(festivalsPosition).getTitle() + " : 추가됨", Toast.LENGTH_SHORT).show();
    }

    private void createDB() {
        db = getActivity().openOrCreateDatabase("FESTIVALS.db", Context.MODE_PRIVATE, null);

        String CREATE_FESTIVAL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + "contentid" + " TEXT," + "title" + " TEXT," + "eventenddate" + " TEXT,"
                + "lat" + " DOUBLE," + "lng" + " DOUBLE," + "contenttypeid" + " TEXT," + "addr1" + " TEXT," + "tel" +" TEXT," + "firstimage" + " TEXT" +")";

        db.execSQL(CREATE_FESTIVAL_TABLE);
    }
}
