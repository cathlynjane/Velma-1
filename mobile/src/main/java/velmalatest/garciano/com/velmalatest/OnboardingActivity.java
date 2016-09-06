package velmalatest.garciano.com.velmalatest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ogaclejapan.smarttablayout.SmartTabLayout;


import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * Created by admin on 8/10/2016.
 */
public class OnboardingActivity extends AppCompatActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    public Button skip;
    public Button next;
    public EditText event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        event = (EditText) findViewById(R.id.eventname);
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (SmartTabLayout) findViewById(R.id.indicator);
//        skip = (Button)findViewById(R.id.skip);
        next = (Button) findViewById(R.id.next);

        next.setVisibility(View.GONE);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new OnboardingFragment1();
                    case 1:
                        return new OnboardingFragment2();
                    case 2:
                        return new OnboardingFragment3();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        pager.setAdapter(adapter);

        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    next.setVisibility(View.GONE);
                } else if (position == 1) {
                    next.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }

            }

        });

//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishOnboarding();
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    finishOnboarding();

//                Calendar beginTime = Calendar.getInstance();
//                beginTime.set(2016, 0, 19, 7, 30);
//                Calendar endTime = Calendar.getInstance();
//                endTime.set(2016, 0, 19, 8, 30);
//                Intent intent = new Intent(Intent.ACTION_INSERT)
//                        .setData(Events.CONTENT_URI)
//                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
//                        .putExtra(Events.TITLE, "Yoga")
//                        .putExtra(Events.DESCRIPTION, "Group class")
//                        .putExtra(Events.EVENT_LOCATION, "The gym")
//                        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
//                        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//                startActivity(intent);


                Uri uri = CalendarContract.Calendars.CONTENT_URI;
                String[] projection = new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.CALENDAR_COLOR
                };

                Cursor calendarCursor = managedQuery(uri, projection, null, null, null);

                long startMillis = 0;
                long endMillis = 0;
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2016, 9, 14, 7, 30);
                startMillis = beginTime.getTimeInMillis();
                Calendar endTime = Calendar.getInstance();
                endTime.set(2016, 9, 14, 8, 45);
                endMillis = endTime.getTimeInMillis();

// Insert Event
                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                TimeZone timeZone = TimeZone.getDefault();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                values.put(CalendarContract.Events.TITLE, "Walk The Dog");
                values.put(CalendarContract.Events.DESCRIPTION, "My dog is bored, so we're going on a really long walk!");
                values.put(CalendarContract.Events.CALENDAR_ID, 2);
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                Toast.makeText(OnboardingActivity.this,"Event Added",Toast.LENGTH_SHORT).show();

// Retrieve ID for new event
                String eventID = uri.getLastPathSegment();


//                final String name = event.getText().toString();
//                final String eventDescription = OnboardingFragment1.descrip.getText().toString();
//                final String eventLocation = OnboardingFragment1.locate.getText().toString();
//                final String startDate = OnboardingFragment2.dateStart.getText().toString();
//                final String endDate = OnboardingFragment2.dateEnd.getText().toString();
//                final String startTime = OnboardingFragment2.timeStart.getText().toString();
////                final String endTime = OnboardingFragment2.timeEnd.getText().toString();
//                final String notify = OnboardingFragment2.alarming.getText().toString();
//
//                if (name.isEmpty())
//                {
//                    event.setError("This field is required");
//                }
//                else if(eventDescription.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must enter event description.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//
//                }
//
//                else if(eventLocation.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must pick the event location.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//                else if(eventDescription.isEmpty() && eventLocation.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must enter the event description and location.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//                else if (startDate.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must enter start date.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//                else if (endDate.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must enter end date.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//                else if (startTime.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must enter start time.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//                else if (endTime.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must enter end time.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//                else if (notify.isEmpty())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OnboardingActivity.this);
//                    builder1.setMessage("You must choose alarm notification.");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//
//
//                else {
                    Intent main = new Intent(OnboardingActivity.this, LandingActivity.class);
                    startActivity(main);
//                }
            }
        });
    }


//    private void finishOnboarding() {
//        SharedPreferences preferences =
//                getSharedPreferences("my_preferences", MODE_PRIVATE);
//
//        preferences.edit()
//                .putBoolean("onboarding_complete",false).commit();
//
//        Intent main = new Intent(this, LandingActivity.class);
//        startActivity(main);
//
//        finish();
//    }
}
