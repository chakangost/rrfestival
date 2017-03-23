package kr.rrcoporation.rrfestival.festival.util.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.provider.AuthProvider;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by fimtrus on 2017. 3. 23..
 * SingleTone
 */

public final class AuthHelper implements FirebaseAuth.AuthStateListener,
                                            GoogleApiClient.OnConnectionFailedListener {

    public static final String GOOGLE_PROVIDER = GoogleAuthProvider.PROVIDER_ID;
    public static final String FACEBOOK_PROVIDER = FacebookAuthProvider.PROVIDER_ID;

    public static final int SIGN_IN_GOOGLE_ACCOUNT = 20;
    public static final int SIGN_IN_FACEBOOK_ACCOUNT = 30;

    private static AuthHelper mAuthHelper;

    private Context                   mContext;
    private GoogleApiClient           mGoogleApiClient;
    private AuthProvider.AuthCallback mCallback;
    private FirebaseAuth              mFirbaseAuth;
    private int mType;

    private AuthHelper() {
    }

    public static final AuthHelper getInstance(Context context) {

        if ( mAuthHelper == null ) {
            mAuthHelper = new AuthHelper();
            mAuthHelper.mContext = context;
        }

        return mAuthHelper;
    }


    private void initialize(int type) {

        mType = type;

        if ( SIGN_IN_GOOGLE_ACCOUNT == type ) {

            if ( mGoogleApiClient == null ) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(mContext.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                        .enableAutoManage((FragmentActivity)mContext /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }
            if ( mFirbaseAuth == null ) {

                mFirbaseAuth = FirebaseAuth.getInstance();
                mFirbaseAuth.addAuthStateListener(this);
            }

        } else if ( SIGN_IN_FACEBOOK_ACCOUNT == type ) {

        }
    }
    /**
     *
     * @param type : SIGN_IN_GOOGLE_ACCOUNT, SIGN_IN_FACEBOOK_ACCOUNT
     */
    public void signIn(int type) {

        initialize(type);

        if ( SIGN_IN_GOOGLE_ACCOUNT == mType ) {

            signInWithGoogleAccount();
        } else if ( SIGN_IN_FACEBOOK_ACCOUNT == mType ) {
            signInWithFacebookAccount();
        }
    }

    private void signInWithGoogleAccount () {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        ((Activity)mContext).startActivityForResult(signInIntent, SIGN_IN_GOOGLE_ACCOUNT);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirbaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                    }
                });
    }
    public void SignOutWithGoogleAccount () {
        FirebaseAuth.getInstance().signOut();
    }



    public void signInWithFacebookAccount () {

    }

    public void setAuthenticationCallback(AuthProvider.AuthCallback callback) {
        mCallback = callback;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_GOOGLE_ACCOUNT) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                if (result.isSuccess()) {
                    onSuccess(result);
                } else {
                    onError(result);
                }
            } else {
                onError(null);
            }
        }
    }

    private void onSuccess(GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        firebaseAuthWithGoogle(account);
    }

    private void onError(GoogleSignInResult result) {

        if ( mCallback != null ) {
            Bundle bundle = new Bundle();
            mCallback.onFailure(bundle);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
