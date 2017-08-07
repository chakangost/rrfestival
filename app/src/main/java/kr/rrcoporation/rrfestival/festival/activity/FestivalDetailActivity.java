/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.rrcoporation.rrfestival.festival.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.LocationTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.adapter.DetailImageAdapter;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.DetailInformation;
import kr.rrcoporation.rrfestival.festival.model.DetailSummary;
import kr.rrcoporation.rrfestival.festival.model.ExtraConstants;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FestivalDetailActivity extends CommonFragmentActivity implements View.OnClickListener {

    private static final String TAG = "FestivalDetailActivity";
    private Toolbar                 toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DetailInformation       detailInformation;
    private BodyItem                bodyItem;
    private ViewPager               imageViewPager;
    private DetailImageAdapter      imageAdapter;
    private FloatingActionButton    floatingActionButton;
    private SQLiteDatabase          db;
    private TextView                eventDate;
    private TextView                address;
    private TextView                program;
    private TextView                subEvent;
    private TextView                payType;
    private TextView                summaryIntro;
    private TextView                telephone;
    private ImageView               shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_detail);

        initialize();
    }

    private void initialize() {
        initializeField();
        initializeListener();
        intializeView();
    }

    private void intializeView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        int typeId = bundle.getInt(ExtraConstants.EXTRA_CONTENT_TYPE_ID);
        int contentId = bundle.getInt(ExtraConstants.EXTRA_CONTENT_ID);
        bodyItem = bundle.getParcelable(ExtraConstants.EXTRA_ITEM);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setTitle(" ");
        ApiAction.getInstance().getFestivalDetailInformation(typeId, contentId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DetailInformation>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(DetailInformation o) {
                detailInformation = o;
                renderDetailView();
            }
        });
    }

    private void initializeListener() {
        address.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    private void initializeField() {
        eventDate = (TextView) findViewById(R.id.textview_event_date);
        address = (TextView) findViewById(R.id.textview_address);
        telephone = (TextView) findViewById(R.id.textview_tel);
        program = (TextView) findViewById(R.id.textview_program);
        subEvent = (TextView) findViewById(R.id.textview_subevent);
        payType = (TextView) findViewById(R.id.textview_type);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        summaryIntro = (TextView) findViewById(R.id.textview_summary_intro);
        imageViewPager = (ViewPager) findViewById(R.id.viewpager_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        shareButton = (ImageView) findViewById(R.id.imageview_share);
    }

    private void renderDetailView() {

        collapsingToolbarLayout.setTitle(detailInformation.getTitle());
        eventDate.setText(detailInformation.getEventDate());

        String addressText = detailInformation.getAddr1();
        if(detailInformation.getAddr2() != null) {
            addressText += " " + detailInformation.getAddr2();
        }
        address.setText(addressText);
        telephone.setText(detailInformation.getTel());
        program.setText(Html.fromHtml(detailInformation.getProgram()));
        subEvent.setText(Html.fromHtml(detailInformation.getSubEvent()));
        payType.setText(Html.fromHtml(detailInformation.getType()));

        String summary = "";

        for (DetailSummary c : detailInformation.getSummaries()) {
            summary += c.getInfoname() + "<br/>";
            summary += c.getInfotext() + "<br/>";
        }
        summaryIntro.setText(Html.fromHtml(summary));
        imageAdapter = DetailImageAdapter.newInstance(FestivalDetailActivity.this, detailInformation.getImages());
        imageViewPager.setAdapter(imageAdapter);
        imageViewPager.setCurrentItem(detailInformation.getImages().size() * 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.button_facebook:
                break;
            case R.id.textview_address:
                //TODO: check.
//                MapFragment.newInstance(bodyItem.getMapy(), bodyItem.getMapx(), "FROM_DETAIL");
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("address", address.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "주소정보를 클립보드에 복사하였습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageview_share:
                sendLocationToKakao();
                break;
            case R.id.fab:
                if (db == null) {
                    db = this.openOrCreateDatabase("FESTIVALS.db", Context.MODE_PRIVATE, null);
                }
                saveBookmark();
                break;
        }
    }

    private void saveBookmark() {
        String existDataQuery = "select * from festival where contentid = '" + detailInformation.getContentId() + "';";
        Cursor cursor = db.rawQuery(existDataQuery, null);

        int columnCnt = cursor.getCount();
        cursor.close();
        if (columnCnt != 0) {
            Toast.makeText(this, "이미 추가 되어있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String tableName = "festival";
        String colums = "contentid, title, lat, lng, contenttypeid, addr1, firstimage";
        String str1 = "INSERT INTO " + tableName + " (" + colums + ") values(";
        String str2 = ");";

        db.beginTransaction();

        StringBuilder sb = new StringBuilder(str1);
        sb.append("'" + detailInformation.getContentId() + "',");
        sb.append("'" + detailInformation.getTitle() + "',");
        sb.append("'" + detailInformation.getMapy() + "',");
        sb.append("'" + detailInformation.getMapx() + "',");
        sb.append("'" + detailInformation.getContentTypeId() + "',");
        sb.append("'" + detailInformation.getAddr1() + "',");
        sb.append("'" + detailInformation.getFirstimage() + "'");
        sb.append(str2);

        db.execSQL(sb.toString());
        db.setTransactionSuccessful();
        db.endTransaction();

        ApiAction.getInstance().fetchBookmarks();
        Toast.makeText(this, detailInformation.getTitle() + " : 추가됨", Toast.LENGTH_SHORT).show();
    }

    private void sendLocationToKakao() {

        LocationTemplate params = LocationTemplate.newBuilder(detailInformation.getAddr1() + " " + detailInformation.getAddr2(),
                ContentObject.newBuilder(detailInformation.getTitle(),
                        detailInformation.getImages().get(0).getOriginimgurl(),
                        LinkObject.newBuilder()
                                .build())
                        .setDescrption(detailInformation.getTitle() + " 위치정보입니다.")
                        .build())
                .setAddressTitle(detailInformation.getTitle())
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });

    }
}
