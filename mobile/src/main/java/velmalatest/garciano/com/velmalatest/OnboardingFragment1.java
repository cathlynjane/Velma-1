package velmalatest.garciano.com.velmalatest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.plus.Plus;

import java.util.Calendar;

/**
 * Created by admin on 8/10/2016.
 */
public class OnboardingFragment1 extends Fragment implements View.OnClickListener{

//    private static final int RESULT_OK = 0;
    View rootView;
    private TextView des;
    private EditText descrip;
    private TextView loc;
    private EditText locate;
    int PLACE_PICKER_REQUEST = 1;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//        Intent intent;
//
//        try {
//            intent = builder.build(getActivity());
//            startActivityForResult(intent, PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
        rootView = inflater.inflate(R.layout.onboarding_screen1, container, false);

        des = (TextView)rootView.findViewById(R.id.description);
        descrip = (EditText)rootView.findViewById(R.id.descriptionText);
        loc = (TextView)rootView.findViewById(R.id.location);
        locate = (EditText)rootView.findViewById(R.id.locationText);
        locate.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        locate.setOnClickListener(this);


        return rootView;


    }


    @Override
    public void onClick(View view) {
        if(view == locate)
        {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            Intent intent;

            try {
                intent = builder.build(getActivity());
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PLACE_PICKER_REQUEST)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data, getActivity());
                String address = String.format("Place: %s", place.getAddress());
                locate.setText(address);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}