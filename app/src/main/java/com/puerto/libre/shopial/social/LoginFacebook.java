package com.puerto.libre.shopial.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.puerto.libre.shopial.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginFacebook extends AppCompatActivity {

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback;

    @Bind(R.id.but_access_facebook)LoginButton but_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_facebook);
        ButterKnife.bind(this);
        accessFacebook();
        voidCallbackManager();
    }

    public void accessFacebook(){
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback = new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile profile = Profile.getCurrentProfile();
                        nextActivity(profile);
                    }
                    @Override
                    public void onCancel(){}
                    @Override
                    public void onError(FacebookException e){}
                };
                but_login.setReadPermissions("user_friends");
                but_login.registerCallback(callbackManager, callback);
            }
        });
    }

    public void voidCallbackManager(){
        callbackManager = CallbackManager.Factory.create();
        ButterKnife.bind(this);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            String first_name = profile.getFirstName();
            String last_name = profile.getLastName();
            String avatar = profile.getProfilePictureUri(200,200).toString() ;
            System.out.println(first_name + "," + last_name + "," + avatar);
            System.out.println("Profile: " + profile.toString());
        }
    }

}
