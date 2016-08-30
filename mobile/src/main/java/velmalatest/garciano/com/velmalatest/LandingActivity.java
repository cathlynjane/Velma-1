package velmalatest.garciano.com.velmalatest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

public class LandingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;


    private boolean is_signInBtn_clicked;
    private int request_code;
    private FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        buidNewGoogleApiClient();
        setContentView(R.layout.activity_activity_landing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(this);

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    @Override
    public void onClick(View view) {
        if (view == fabButton) {
            Intent intent = new Intent(LandingActivity.this, OnboardingActivity.class);
            startActivity(intent);
            }
        }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout


    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
    }
//    private void buidNewGoogleApiClient() {
//
//        google_api_client = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API, Plus.PlusOptions.builder().build())
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .addScope(Plus.SCOPE_PLUS_PROFILE)
//                .build();
//    }
//
//
//
//    protected void onStart() {
//        super.onStart();
//        google_api_client.connect();
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
//        if (google_api_client.isConnected()) {
//            google_api_client.connect();
//        }
//    }
//
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!result.hasResolution()) {
//            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
//            return;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int responseCode,
//                                    Intent intent) {
//        // Check which request we're responding to
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
//
//    private void gPlusSignOut() {
//        if (google_api_client.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(google_api_client);
//            google_api_client.disconnect();
//            google_api_client.connect();
//
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//        is_signInBtn_clicked = false;
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        google_api_client.connect();
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_logout){
        gPlusSignOut();
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            gPlusSignOut();
//            Intent logoutIntent = new Intent(LandingActivity.this, LoginActivity.class);
//            startActivity(logoutIntent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putBoolean("isLoggedIn", false).commit();
            Intent logoutIntent = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (!connectionResult.hasResolution()) {
            google_api_availability.getErrorDialog(this, connectionResult.getErrorCode(),request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

//            connection_result = result;

//            if (is_signInBtn_clicked) {
//
//                resolveSignInError();
//            }
        }

    }
}
