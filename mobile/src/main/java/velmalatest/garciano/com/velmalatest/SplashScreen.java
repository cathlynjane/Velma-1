package velmalatest.garciano.com.velmalatest;

/**
 * Created by admin on 7/20/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

public class SplashScreen extends AppCompatActivity {

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    private static final int SIGN_IN_CODE = 0;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;

    Context mcontext;
    boolean isFirstRun;



    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
////        getSupportActionBar().hide();
//
//        buidNewGoogleApiClient();
        setContentView(R.layout.activity_splash_screen);

        mcontext = this;

//        Countdown _tik;
//        _tik=new Countdown(5000,5000,this,LandingActivity.class);
//        _tik.start();
       // StartAnimations();



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        //prefs.edit().putBoolean("isFirstRun", true).commit();
        Boolean isFirstRun = prefs.getBoolean("isFirstRun", false);
        Boolean isLoggedIn = prefs.getBoolean("isLoggedIn", true);
        Toast.makeText(mcontext,""+isFirstRun,Toast.LENGTH_LONG).show();

        if (isFirstRun) {
//maybe you want to check it by getting the sharedpreferences. Use this instead if (locked)
// if (prefs.getBoolean("locked", locked) {\
            prefs.edit().putBoolean("isFirstRun", true).commit();
            Toast.makeText(mcontext,"Login",Toast.LENGTH_SHORT).show();

//             Intent i = new Intent(SplashScreen.this,LoginActivity.class);
//             startActivity(i);

            if(isLoggedIn){
               // "Landing";
                Intent i = new Intent(SplashScreen.this,LandingActivity.class);
                startActivity(i);
            }
            else{

                Intent i = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(i);
               // "Login";
            }



        } else {

            Intent i = new Intent(SplashScreen.this,TutorialActivity.class);
            startActivity(i);
            Toast.makeText(mcontext,"Tutorial",Toast.LENGTH_SHORT).show();


            //startActivity(new Intent(mcontext, Tag1.class));
            //buidNewGoogleApiClient();
           // Intent i = new Intent(SplashScreen.this,LoginActivity.class);
           // startActivity(i);
        }

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        boolean first = prefs.getBoolean("key_first_launch", false);
//
//        if (first)
//        {
//            Intent i = new Intent(SplashScreen.this,TutorialActivity.class);
//            startActivity(i);
//        }
//
//        else{
////
//            Intent i = new Intent(SplashScreen.this,LoginActivity.class);
//            startActivity(i);
//        }

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }

//    private void buidNewGoogleApiClient(){
//
////        google_api_client =  new GoogleApiClient.Builder(this)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
////                .addApi(Plus.API, Plus.PlusOptions.builder().build())
////                .addScope(Plus.SCOPE_PLUS_LOGIN)
////                .addScope(Plus.SCOPE_PLUS_PROFILE)
////                .build();
//    }
//
//    protected void onStart() {
//        super.onStart();
////        google_api_client.connect();
////        if(SIGN_IN_CODE == 0)
////        {
////            changeUI(true);
////        }
////        else if(SIGN_IN_CODE == 1)
////        {
////            changeUI(false);
////        }
//
//    }
//
//    protected void onStop() {
//        super.onStop();
//        if (google_api_client.isConnected()) {
//            google_api_client.disconnect();
//        }
//    }
//
//    protected void onResume(){
//        super.onResume();
////        if (google_api_client.isConnected()) {
////            google_api_client.connect();
////        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!result.hasResolution()) {
//            google_api_availability.getErrorDialog(this, result.getErrorCode(),request_code).show();
//            return;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int responseCode,
//                                    Intent intent) {
//
//        if (requestCode == SIGN_IN_CODE) {
//            request_code = requestCode;
//            if (responseCode != RESULT_OK) {
//                is_signInBtn_clicked = false;
//            }
//
//            is_intent_inprogress = false;
//
//            if (!google_api_client.isConnecting()) {
//                google_api_client.connect();
//            }
//        }
//
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//        is_signInBtn_clicked = false;
//
//        // Update the UI after signin
//        changeUI(true);
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        google_api_client.connect();
//        changeUI(false);
//    }
//
//
//    /*
//      Method to resolve any signin errors
//     */
//
//    private void resolveSignInError() {
//        if (connection_result.hasResolution()) {
//            try {
//                is_intent_inprogress = true;
//                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
//                Log.d("resolve error", "sign in error resolved");
//            } catch (IntentSender.SendIntentException e) {
//                is_intent_inprogress = false;
//                google_api_client.connect();
//            }
//        }
//    }
//
//    private boolean changeUI(boolean signedIn) {
//        if (signedIn) {
//           Intent i = new Intent(SplashScreen.this, LandingActivity.class);
//            startActivity(i);
//        } else {
//
//            Intent notSignedIn = new Intent(SplashScreen.this, LoginActivity.class);
//            startActivity(notSignedIn);
//        }
//        return signedIn;
//    }
//

}