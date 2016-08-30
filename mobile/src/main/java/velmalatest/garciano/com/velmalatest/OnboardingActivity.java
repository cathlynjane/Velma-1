package velmalatest.garciano.com.velmalatest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.ogaclejapan.smarttablayout.SmartTabLayout;


import java.util.Calendar;

/**
 * Created by admin on 8/10/2016.
 */
public class OnboardingActivity extends AppCompatActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button skip;
    private Button next;

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

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2016, 0, 19, 7, 30);
                Calendar endTime = Calendar.getInstance();
                endTime.set(2016, 0, 19, 8, 30);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(Events.TITLE, "Yoga")
                        .putExtra(Events.DESCRIPTION, "Group class")
                        .putExtra(Events.EVENT_LOCATION, "The gym")
                        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                startActivity(intent);



                Intent main = new Intent(OnboardingActivity.this, LandingActivity.class);
                startActivity(main);
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
