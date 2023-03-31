package com.example.aqm.plant;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.progresviews.ProgressWheel;
import com.example.aqm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import pl.droidsonroids.gif.GifDrawable;

public class PlantFragment extends Fragment {

    private TextView plants;
    private ProgressWheel progressWheel;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private DatabaseReference database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_plant, container, false);

        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());

        progressWheel = myFragmentView.findViewById(R.id.plant_moisture);
        plants = myFragmentView.findViewById(R.id.plant_status);


        database.child("air").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    Long temperature = (Long) map.get("temp");
                    Long humidity = (Long) map.get("humidity");
                    Long mq135 = (Long) map.get("mq135q");

                    Log.d("TAG", mq135.toString());

                    int soilWet = 500;
                    int soilDry = 750;

                    Double moisture_percentage =  (mq135 / 1023.00) * 100;
                    Double moisture_percentage360 =  (mq135 / 1023.00) * 360;

                    progressWheel.setStepCountText(String.format("%.2f",moisture_percentage)+" %");
                    progressWheel.setStepCountText(mq135.toString());
                    progressWheel.setPercentage(moisture_percentage360.intValue());


//                    try {
//                        c = Calendar.getInstance(TimeZone.getDefault());
//                        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//                        if (timeOfDay >= 6 && timeOfDay <= 18) {
//                            if (temperature < 20) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.sunny_snow);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature >= 20 && humidity <= 70) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.sunny_cold);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature < 50 && humidity > 70) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.sunny_normal);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature < 32 && humidity > 88) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.sunny_rain);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature < 32 && humidity > 100) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.sunny_heavy_rain);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            }
//                        }
//
//                        if (timeOfDay >= 18 || timeOfDay <= 6) {
//                            if (temperature < 20) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.night_snow);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature >= 20 && humidity <= 70) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.night_cold);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature < 50 && humidity > 70) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.night_normal);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            } else if (temperature < 32 && humidity > 88) {
//                                GifDrawable gifFromResource;
//                                gifFromResource = new GifDrawable(getResources(), R.raw.night_rain);
//                                gifImageView.setImageDrawable(gifFromResource);
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    if (mq135 < 500) {
//                        aqi.setText(mq135.toString() + " PPM \n FRESH AIR");
//                        aqi.setTextColor(Color.GREEN);
//                    } else if (mq135 > 500 && mq135 <= 1000) {
//                        aqi.setText(mq135.toString() + " PPM \n POOR AIR");
//                        aqi.setTextColor(Color.YELLOW);
//                    } else if (mq135 > 1000) {
//                        aqi.setText(mq135.toString() + " PPM \n HAZARDOUS");
//                        aqi.setTextColor(Color.RED);
//                    }
//
//                    temp.setText(temperature.toString() + " °C");
//                    humi.setText(humidity.toString() + " %");
                }
            }
//        database.child("plant").addValueEventListener(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.exists()) {
//                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//
//                    Long plantmo = (Long) map.get("moisture");
//                    Double plantmod = plantmo.doubleValue();
//                    int soilWet = 500;
//                    int soilDry = 750;
//
//                    Double moisture_percentage =  (plantmod / 1023.00) * 100;
//                    Double moisture_percentage360 =  (plantmod / 1023.00) * 360;
//
//                    progressWheel.setStepCountText(String.format("%.2f",moisture_percentage)+" %");
//                    progressWheel.setPercentage(moisture_percentage360.intValue());
//
//                    if (plantmod < soilWet) {
//                        plants.setText("Status:\n Soil is too wet");
//                    } else if (plantmod >= soilWet && plantmod < soilDry) {
//                        plants.setText("Status: \n Soil moisture is perfect");
//                    } else {
//                        plants.setText("Status:\n Soil is too dry - time to water!");
//                    }
//                }
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
        return myFragmentView;
    }
}

