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

import java.util.ArrayList;
import java.util.List;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.provider.IdpProvider;
import kr.rrcoporation.rrfestival.festival.util.AuthUI;

public class AuthActivity extends CommonFragmentActivity {
    private static final String TAG = "AuthMethodPicker";
    private static final int RC_EMAIL_FLOW = 2;
    private static final int RC_ACCOUNT_LINK = 3;

    private ArrayList<IdpProvider> mIdpProviders;
    @Nullable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
//        mSaveSmartLock = mActivityHelper.getSaveSmartLockInstance();
//        findViewById(R.id.email_provider).setOnClickListener(this);
//
//        populateIdpList(mActivityHelper.getFlowParams().providerInfo);
//
//        int logoId = mActivityHelper.getFlowParams().logoId;
//        if (logoId == AuthUI.NO_LOGO) {
//            findViewById(R.id.logo_layout).setVisibility(View.GONE);
//        } else {
//            ImageView logo = (ImageView) findViewById(R.id.logo);
//            logo.setImageResource(logoId);
//        }
    }

    private void populateIdpList(List<AuthUI.IdpConfig> providers) {
//        mIdpProviders = new ArrayList<>();
//        for (AuthUI.IdpConfig idpConfig : providers) {
//            switch (idpConfig.getProviderId()) {
//                case AuthUI.GOOGLE_PROVIDER:
//                    mIdpProviders.add(new GoogleProvider(this, idpConfig));
//                    break;
//                case AuthUI.FACEBOOK_PROVIDER:
//                    mIdpProviders.add(new FacebookProvider(
//                            this, idpConfig, mActivityHelper.getFlowParams().themeId));
//                    break;
//                case AuthUI.TWITTER_PROVIDER:
//                    mIdpProviders.add(new TwitterProvider(this));
//                    break;
//                case AuthUI.EMAIL_PROVIDER:
//                    findViewById(R.id.email_provider).setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    Log.e(TAG, "Encountered unknown IDPProvider parcel with type: "
//                            + idpConfig.getProviderId());
//            }
//        }
//
//        ViewGroup btnHolder = (ViewGroup) findViewById(R.id.btn_holder);
//        for (final IdpProvider provider : mIdpProviders) {
//            View loginButton = null;
//            switch (provider.getProviderId()) {
//                case GoogleAuthProvider.PROVIDER_ID:
//                    loginButton = getLayoutInflater()
//                            .inflate(R.layout.idp_button_google, btnHolder, false);
//                    break;
//                case FacebookAuthProvider.PROVIDER_ID:
//                    loginButton = getLayoutInflater()
//                            .inflate(R.layout.idp_button_facebook, btnHolder, false);
//                    break;
//                case TwitterAuthProvider.PROVIDER_ID:
//                    loginButton = getLayoutInflater()
//                            .inflate(R.layout.idp_button_twitter, btnHolder, false);
//                    break;
//                default:
//                    Log.e(TAG, "No button for provider " + provider.getProviderId());
//            }
//
//            if (loginButton != null) {
//                loginButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mActivityHelper.showLoadingDialog(R.string.progress_dialog_loading);
//                        provider.startLogin(AuthMethodPickerActivity.this);
//                    }
//                });
//                provider.setAuthenticationCallback(this);
//                btnHolder.addView(loginButton, 0);
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_EMAIL_FLOW) {
//            if (resultCode == ResultCodes.OK) {
//                finish(ResultCodes.OK, data);
//            }
//        } else if (requestCode == RC_ACCOUNT_LINK) {
//            finish(resultCode, data);
//        } else {
//            for (IdpProvider provider : mIdpProviders) {
//                provider.onActivityResult(requestCode, resultCode, data);
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
}
