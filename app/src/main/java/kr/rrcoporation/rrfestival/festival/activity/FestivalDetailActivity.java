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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.adapter.DetailImageAdapter;
import kr.rrcoporation.rrfestival.festival.model.DetailInformation;
import kr.rrcoporation.rrfestival.festival.model.DetailSummary;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FestivalDetailActivity extends CommonFragmentActivity implements View.OnClickListener {

    private static final String TAG = "FestivalDetailActivity";
    private Toolbar                 toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DetailInformation       detailInformation;
    private ViewPager               imageViewPager;
    private DetailImageAdapter      imageAdapter;

    private TextView eventDate;
    private TextView address;
    private TextView program;
    private TextView subEvent;
    private TextView payType;
    private TextView summaryIntro;
    private TextView telephone;

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

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ApiAction.getInstance().getFestivalDetailInformation().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DetailInformation>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DetailInformation o) {
                detailInformation = o;
                renderDetailView();
            }
        });
    }

    private void initializeListener() {
    }

    private void initializeField() {
        eventDate = (TextView) findViewById(R.id.textview_event_date);
        address = (TextView) findViewById(R.id.textview_address);
        telephone = (TextView) findViewById(R.id.textview_tel);
        program = (TextView) findViewById(R.id.textview_program);
        subEvent = (TextView) findViewById(R.id.textview_subevent);
        payType = (TextView) findViewById(R.id.textview_type);
        summaryIntro = (TextView) findViewById(R.id.textview_summary_intro);
        imageViewPager = (ViewPager) findViewById(R.id.viewpager_image);
    }

    private void renderDetailView() {

        collapsingToolbarLayout.setTitle(detailInformation.getTitle());
        eventDate.setText(detailInformation.getEventDate());
        address.setText(detailInformation.getAddr1() + " " + detailInformation.getAddr2());
        telephone.setText(detailInformation.getTel());
        program.setText(detailInformation.getProgram());
        subEvent.setText(detailInformation.getSubEvent());
        payType.setText(detailInformation.getType());

        String summary = "";

        for(DetailSummary c : detailInformation.getSummaries()) {
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
        }
    }
}
