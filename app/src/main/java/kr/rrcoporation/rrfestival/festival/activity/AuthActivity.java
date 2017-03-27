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
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.provider.IdpProvider;
import kr.rrcoporation.rrfestival.festival.util.helper.AuthHelper;

public class AuthActivity extends CommonFragmentActivity implements View.OnClickListener {
    private static final String TAG = "AuthMethodPicker";
    private static final int RC_EMAIL_FLOW = 2;
    private static final int RC_ACCOUNT_LINK = 3;

    private AuthHelper mAuthHelper;
    private ArrayList<IdpProvider> mIdpProviders;
    private Button mSignInGoogleButton;
    private Button mSignInFacebookButton;

    @Nullable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initialize();

    }

    private void initialize() {
        initializeField();
        initializeListener();
    }

    private void initializeListener() {
        mSignInGoogleButton.setOnClickListener(this);
        mSignInFacebookButton.setOnClickListener(this);
    }

    private void initializeField() {
        mAuthHelper = AuthHelper.getInstance(this);

        mSignInGoogleButton = (Button) findViewById(R.id.button_google);
        mSignInFacebookButton = (Button) findViewById(R.id.button_facebook);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch ( requestCode ) {
            case AuthHelper.SIGN_IN_GOOGLE_ACCOUNT :
            case AuthHelper.SIGN_IN_FACEBOOK_ACCOUNT :
                mAuthHelper.onActivityResult(requestCode, resultCode, data);
                break;
        }

//        if (requestCode == RC_EMAIL_FLOW) {
//            if (resultCode == ResultCodes.OK) {
//                finish(ResultCodes.OK, data);
//            }
//        } else if (requestCode == RC_ACCOUNT_LINK) {
//            finish(resultCode, data);
//        } else {
//            for (IdpProvider provider : mIdpProviders) {
//                mAuthHelper.onActivityResult(requestCode, resultCode, data);
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mIdpProviders != null) {
//            for (final IdpProvider provider : mIdpProviders) {
//                if (provider instanceof GoogleProvider) {
//                    ((GoogleProvider) provider).disconnect();
//                }
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch ( id ) {
            case R.id.button_google :
                mAuthHelper.signIn();
                break;
            case R.id.button_facebook :
                mAuthHelper.signOut();
                break;
        }
    }
}
