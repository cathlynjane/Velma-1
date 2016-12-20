package velmalatest.garciano.com.velmalatest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 8/1/2016.
 */
public class AddEventActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {
    private EditText title;
    private EditText dateStart;
    private EditText dateEnd;
    private EditText timeStart;
    private EditText timeEnd;
    private AutoCompleteTextView eventLocation;
    private static final int SIGN_IN_CODE = 0;
//    private DatePickerDialog startdatepicker;
//    private DatePickerDialog enddatepicker;
//    private SimpleDateFormat dateFormatter;
    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;
//    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private static final int PLACE_PICKER_FLAG = 1;
    private PlacePicker.IntentBuilder builder;
    private EditText alarming;
    private EditText contacts;
    private AlertDialog alert;
    private EditText people;
    private Button save;
    private Button friendsTrial;

    protected GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    private int request_code;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;



    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    protected GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        buidNewGoogleApiClient();

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();

        setContentView(R.layout.activity_event_form);
        builder = new PlacePicker.IntentBuilder();

        title = (EditText)findViewById(R.id.eventname);
        title.setHintTextColor(getResources().getColor(R.color.white));
        dateStart = (EditText)findViewById(R.id.startdate);
        dateStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        dateStart.setInputType(InputType.TYPE_NULL);
        dateEnd = (EditText)findViewById(R.id.enddate);
        dateEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        timeStart = (EditText)findViewById(R.id.starttime);
        timeStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        timeEnd = (EditText)findViewById(R.id.endtime);
        timeEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        eventLocation = (AutoCompleteTextView) findViewById(R.id.location);
        eventLocation.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        mPlacesAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
//                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
//        eventLocation.setOnItemClickListener(mAutocompleteClickListener);
//        eventLocation.setAdapter(mPlacesAdapter);
        alarming = (EditText) findViewById(R.id.alarm);
        alarming.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        people = (EditText)findViewById(R.id.invitepeople);
        people.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        save = (Button)findViewById(R.id.sumbitevent);
        friendsTrial=(Button)findViewById(R.id.frnd_button);
        dateStart.setOnClickListener(this);
        dateEnd.setOnClickListener(this);
        timeStart.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        alarming.setOnClickListener(this);
        people.setOnClickListener(this);
        save.setOnClickListener(this);
        friendsTrial.setOnClickListener(this);
    }

//    private void buidNewGoogleApiClient(){
//
//        google_api_client =  new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API, Plus.PlusOptions.builder().build())
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .addScope(Plus.SCOPE_PLUS_PROFILE)
//                .build();
//    }


    @Override
    public void onClick(View view) {
        if(view == dateStart) {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            dateStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    },sYear, sMonth, sDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
            datePickerDialog.show();
        } else if(view == dateEnd) {
            final Calendar c = Calendar.getInstance();
            eYear = c.get(Calendar.YEAR);
            eMonth = c.get(Calendar.MONTH);
            eDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dateEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, eYear, eMonth, eDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
            datePickerDialog.show();
        }
        else if (view == timeStart) {

            final Calendar c = Calendar.getInstance();
            sHour = c.get(Calendar.HOUR_OF_DAY);
            sMinute = c.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeStart.setText(hourOfDay + ":" + minute);
                            }
                        }, sHour, sMinute, false);

            timePickerDialog.show();

        }


    else if (view == timeEnd) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        eHour = c.get(Calendar.HOUR_OF_DAY);
        eMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        timeEnd.setText(hourOfDay + ":" + minute);
                    }
                }, eHour, eMinute, false);
        timePickerDialog.show();
    }

        else if (view == alarming) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                    AddEventActivity.this);
            alertBuilder.setIcon(R.drawable.alarm);
            alertBuilder.setTitle("Alarm every: ");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    AddEventActivity.this,
                    android.R.layout.select_dialog_item);
            arrayAdapter.add("At time of event");
            arrayAdapter.add("10 minutes before the event");
            arrayAdapter.add("20 minutes before the event");
            arrayAdapter.add("30 minutes before the event");
            arrayAdapter.add("40 minutes before the event");
            arrayAdapter.add("50 minutes before the event");
            arrayAdapter.add("1 hour before the event");

            alertBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });

            alertBuilder.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            String strOS = arrayAdapter.getItem(which);
                            alarming.setText(strOS);
                            dialog.dismiss();
                        }
                    });

            final AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    ListView listView = alertDialog.getListView();
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(
                                AdapterView<?> parent, View view,
                                int position, long id) {
                            // TODO Auto-generated method stub
                            String strOS = arrayAdapter.getItem(position);
                            Toast.makeText(getApplicationContext(),
                                    "Long Press - Deleted Entry " + strOS,
                                    Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            return true;
                        }
                    });
                }
            });

            alertDialog.show();
        }

//        else if(view == people)
//        {
//                Toast.makeText(this, "G+ Friend List", Toast.LENGTH_LONG).show();
//                Plus.PeopleApi.loadVisible(google_api_client, null)
//                        .setResultCallback(this);
//
//        }

        else if(view == friendsTrial)
        {

            Toast.makeText(this, "G+ Friend List", Toast.LENGTH_LONG).show();
            Plus.PeopleApi.loadVisible(google_api_client, null)
                    .setResultCallback(this);
        }
}




    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        google_api_client.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        google_api_client.disconnect();
        super.onStop();
    }

    protected void onResume(){
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }

    }


//    private AdapterView.OnItemClickListener mAutocompleteClickListener
//            = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
//            final String placeId = String.valueOf(item.placeId);
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//
//        }
//    };
//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
//            = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(PlaceBuffer places) {
//            if (!places.getStatus().isSuccess()) {
//                Log.e("place", "Place query did not complete. Error: " +
//                        places.getStatus().toString());
//                return;
//            }
//            // Selecting the first object buffer.
//            final Place place = places.get(0);
//
//        }
//    };


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            google_api_availability.getErrorDialog(this, connectionResult.getErrorCode(),request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

            connection_result = connectionResult;

        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> img_list= new ArrayList<String>();
            try {
                int count = personBuffer.getCount();

                for (int i = 0; i < count; i++) {
                    list.add(personBuffer.get(i).getDisplayName());
                    img_list.add(personBuffer.get(i).getImage().getUrl());
                }
                Intent intent = new Intent(AddEventActivity.this,FriendActivity.class);
                intent.putStringArrayListExtra("friendsName",list);
                intent.putStringArrayListExtra("friendsPic",img_list);
                startActivity(intent);
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e("circle error", "Error requesting visible circles: " + peopleData.getStatus());
        }
    }
}
