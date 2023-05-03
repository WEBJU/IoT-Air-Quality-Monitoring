package com.example.aqm.miscellaneous;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aqm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import pl.droidsonroids.gif.GifDrawable;

public class DetailedReportActivity extends AppCompatActivity {
    private TextView temp,humid,mq;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_report);
        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());
        temp = findViewById(R.id.temperature);
        humid = findViewById(R.id.humidity);
        mq = findViewById(R.id.mq135);
    }

    @Override
    protected void onStart() {
        super.onStart();

        database.child("air").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    Long temperature = (Long) map.get("temp");
                    Long humidity = (Long) map.get("humidity");
                    Long mq135 = (Long) map.get("mq135q");

                    temp.setText(temperature.toString() + " °C");
                    humid.setText(humidity.toString() + " %");
                    mq.setText(mq135.toString() + " PPM");

                    LinearLayout layout = findViewById(R.id.warning);

                    if (mq135 >= 1000) {
                        layout.setVisibility(View.VISIBLE);

                        // Get the FCM token from Realtime Firebase
                        DatabaseReference ref = database.child("fcm_token");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fcmToken = snapshot.getValue(String.class);

                                Log.d("TAG","yorfcm:"+fcmToken);
                                // Send the notification to the user

//                                // Send the notification to the user
//                                Message message = Message.builder()
//                                        .setNotification(Notification.builder()
//                                                .setTitle("Test")
//                                                .setBody("This is a test notification")
//                                                .build())
//                                        .setToken(fcmToken)
//                                        .build();
//
//
//
//
//
//                                try {
//                                    String response = FirebaseMessaging.getInstance().send(message);
//                                } catch (FirebaseMessagingException e) {
//                                    e.printStackTrace();
//                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("TAG", "Failed to get FCM token: " + error.getMessage());
                            }
                        });
                    } else {
                        layout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }
}