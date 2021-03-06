package velmalatest.garciano.com.velmalatest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import velmalatest.garciano.com.velmalatest.apiclient.DrawableCalendarEvent;
import velmalatest.garciano.com.velmalatest.apiclient.MyEvent;
import velmalatest.garciano.com.velmalatest.helper.AlarmReceiver;

//import com.alamkanak.weekview.DateTimeInterpreter;
//import com.alamkanak.weekview.MonthLoader;
//import com.alamkanak.weekview.WeekView;
//import com.alamkanak.weekview.WeekViewEvent;


public class LandingActivity extends AppCompatActivity implements CalendarPickerController, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {

    //WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;


    private boolean is_signInBtn_clicked;
    private int request_code;
    private FloatingActionButton fabButton;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final int CREATE_EVENT = 0;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    FloatingActionButton fab;
    AgendaCalendarView mAgendaCalendarView;

    Context mcontext;
    final int CALENDAR_PERMISSION = 42;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildNewGoogleApiClient();
        setContentView(R.layout.activity_activity_landing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAgendaCalendarView = (AgendaCalendarView) findViewById(R.id.agenda_calendar_view);
        mcontext = this;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);


        fab = (FloatingActionButton) findViewById(R.id.fabButton);

        fab.setOnClickListener(this);
//
//        Calendar calNow = Calendar.getInstance();
//        Calendar calSet = (Calendar) calNow.clone();
//
//        calSet.set(Calendar.HOUR_OF_DAY, 10);
//        calSet.set(Calendar.MINUTE, 37);
//        calSet.set(Calendar.SECOND, 0);
//        calSet.set(Calendar.MILLISECOND, 0);
//
//        setAlarm(calSet);


    }


    // region Interface - CalendarPickerController
    @Override
    public void onDaySelected(DayItem dayItem) {

    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        //  Log.d(LOG_TAG, String.format("Selected event: %s", event));
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    // endregion

    private void mockList(List<CalendarEvent> eventList) {

        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.MONTH, 1);
        BaseCalendarEvent event1 = new BaseCalendarEvent("Thibault travels in Iceland", "A wonderful journey!", "Iceland",
                ContextCompat.getColor(this, R.color.colorPrimary), startTime1, endTime1, true);
        eventList.add(event1);

        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_YEAR, 3);
        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
                ContextCompat.getColor(this, R.color.colorPrimaryDark), startTime2, endTime2, true);
        eventList.add(event2);

        // Example on how to provide your own layout
        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event3 = new DrawableCalendarEvent("Visit of Harpa", "", "Dalvík",
                ContextCompat.getColor(this, R.color.colorAccent), startTime3, endTime3, false, R.drawable.common_ic_googleplayservices);
        eventList.add(event3);
    }

    private void buildNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    protected void onStart() {
        super.onStart();
//        google_api_client.connect();

    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();

        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        prefs.edit().putBoolean("isLoggedIn", true).commit();
//        Intent i = new Intent(LandingActivity.this, LandingActivity.class);
//        startActivity(i);
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();

    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();

            }
        }
    }


    @Override
    public void onClick(View view) {

        // Toast.makeText(getBaseContext(), "" + view, Toast.LENGTH_LONG).show();

        if (view == fab) {
            Intent intent = new Intent(LandingActivity.this, OnboardingActivity.class);
            startActivityForResult(intent, CREATE_EVENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }

        if (requestCode == CREATE_EVENT) {

            //Toast.makeText(getBaseContext(), "Here0", Toast.LENGTH_SHORT).show();
            MyEvent myevent = null;

            if (responseCode == RESULT_OK) {

                Toast.makeText(getBaseContext(), "Here1", Toast.LENGTH_SHORT).show();

                Bundle res = intent.getExtras();
                String name = res.getString("name");
                String eventDescription = res.getString("eventDescription");
                String eventLocation = res.getString("eventLocation");
                String startDate = res.getString("startDate");
                String startTime = res.getString("startTime");
                String endDate = res.getString("endDate");
                String endTime = res.getString("param_result");
                String notify = res.getString("endTime");

                myevent = new MyEvent(name, eventDescription, eventLocation, startDate, endDate, startTime, endTime, notify);

                Date sdate = null;
                Date edate = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    sdate = format.parse(startDate);
                    edate = format.parse(endDate);
                } catch (Exception e) {

                }

                //String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
                //String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013

                Calendar stime = Calendar.getInstance();
                stime.set(Calendar.HOUR_OF_DAY, 3);
                stime.set(Calendar.MINUTE, 0);
                stime.set(Calendar.MONTH, 9 - 1);
                stime.set(Calendar.YEAR, 2016);
                Calendar etime = Calendar.getInstance();
                etime.add(Calendar.HOUR, 1);
                etime.set(Calendar.MONTH, 9 - 1);



                //HARDCODED VALUES 10:51
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, 10);
                calSet.set(Calendar.MINUTE, 55);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                setAlarm(calSet);

            }

        }

    }

    private void setAlarm(Calendar targetcal) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetcal.getTimeInMillis(), pendingIntent);
    }


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

        //  setupDateTimeInterpreter(id == R.id.action_monthly_view);


//        if(id == R.id.action_logout){
//        gPlusSignOut();
//        }

        switch (id) {
            case R.id.action_logout: {

                gPlusSignOut();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putBoolean("isLoggedOut", false).apply();
                prefs.edit().putBoolean("isLoggedIn", false).apply();
                Intent i = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(i);


                return true;
            }
            case R.id.action_monthly_view: {
                return true;
            }
            case R.id.action_today: {
                //  mWeekView.goToToday();
                return true;
            }
            case R.id.action_day_view: {
//                if (mWeekViewType != TYPE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(1);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }

                return true;
            }
            case R.id.action_three_day_view:
//                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_THREE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(3);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
                return true;
            case R.id.action_week_view:
//                if (mWeekViewType != TYPE_WEEK_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_WEEK_VIEW;
//                    mWeekView.setNumberOfVisibleDays(7);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                }
                return true;
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

//    private void setupDateTimeInterpreter(final boolean shortDate) {
//        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
//            @Override
//            public String interpretDate(Calendar date) {
//                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
//                String weekday = weekdayNameFormat.format(date.getTime());
//                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());
//
//                // All android api level do not have a standard way of getting the first letter of
//                // the week day name. Hence we get the first char programmatically.
//                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
//                if (shortDate)
//                    weekday = String.valueOf(weekday.charAt(0));
//                return weekday.toUpperCase() + format.format(date.getTime());
//            }
//
//            @Override
//            public String interpretTime(int hour) {
//                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
//            }
//        });
//    }


//    @Override
//    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

    // Populate the week view with some events.


    // Toast.makeText(getBaseContext(), "Hi", Toast.LENGTH_LONG).show();

//        Calendar startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
//        //event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 30);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 4);
//        endTime.set(Calendar.MINUTE, 30);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 4);
//        startTime.set(Calendar.MINUTE, 20);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 5);
//        endTime.set(Calendar.MINUTE, 0);
//        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 5);
//        startTime.set(Calendar.MINUTE, 30);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 2);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 5);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        startTime.add(Calendar.DATE, 1);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 15);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_04));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 1);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
//        startTime.set(Calendar.HOUR_OF_DAY, 15);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);

//        return events;
//    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

//    @Override
//    public void onEventClick(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onEmptyViewLongPress(Calendar time) {
//        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
//    }
//
//    public WeekView getWeekView() {
//        return mWeekView;
//    }


    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();


//                Auth.GoogleSignInApi.signOut(google_api_client).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status status) {
////                                Plus.AccountApi.clearDefaultAccount(google_api_client);
////                                google_api_client.disconnect();
//                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//                                prefs.edit().putBoolean("isLoggedIn", false).commit();
//                                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                            }
//                        });

        }
    }

//    private void gPlusSignOut() {
////        if (google_api_client.isConnected()) {
////            Plus.AccountApi.clearDefaultAccount(google_api_client);
////            google_api_client.disconnect();
////            google_api_client.connect();
////
////        }
////    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (!connectionResult.hasResolution()) {
            google_api_availability.getErrorDialog(this, connectionResult.getErrorCode(), request_code).show();
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


    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }
}
