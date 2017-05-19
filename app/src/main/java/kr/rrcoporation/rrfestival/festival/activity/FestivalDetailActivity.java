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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.model.DetailInformation;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class FestivalDetailActivity extends CommonFragmentActivity implements View.OnClickListener {

    private static final String TAG = "FestivalDetailActivity";
    private Toolbar                 toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DetailInformation       detailInformation;

    private TextView eventDate;

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
        collapsingToolbarLayout.setTitle("Details");
        ApiAction.getInstance().getFestivalDetailInformation().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DetailInformation>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DetailInformation o) {
                detailInformation = o;
                eventDate.setText(detailInformation.getEventDate());
            }
        });
    }

    private void initializeListener() {
    }

    private void initializeField() {
        eventDate = (TextView) findViewById(R.id.textview_event_date);
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
