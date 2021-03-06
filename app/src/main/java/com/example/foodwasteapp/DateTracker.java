package com.example.foodwasteapp;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.foodwasteapp.Notifications.CHANNEL_1_ID;

public class DateTracker extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private static final String TAG = "Expiration activity";
    public TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    long daysToExpiry;
    String expiryDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        notificationManager = NotificationManagerCompat.from(this);
        mDisplayDate = findViewById(R.id.expiration_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DateTracker.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: dd/mm/yy: " + dayOfMonth + "/" + month + "/" + year);
                String formattedMonth = "" + month;
                String formattedDayOfMonth = "" + dayOfMonth;

                if(month < 10){

                     formattedMonth = "0" + (month + 1);
                }
                if(dayOfMonth < 10){

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                expiryDate = formattedDayOfMonth + "/" + formattedMonth + "/" + year;
                mDisplayDate.setText(expiryDate); //For Datepicker
                Tracker(view);
            }
        };
    }

    public void Tracker (View view){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date currentDate1 = sdf.parse(currentDate);
            Date expiryDate1 = sdf.parse(expiryDate);
            daysToExpiry= expiryDate1.getTime() - currentDate1.getTime();
            daysToExpiry = TimeUnit.DAYS.convert(daysToExpiry, TimeUnit.MILLISECONDS);
            if (daysToExpiry > 0) {
                if (daysToExpiry <= 2){
                    System.out.println(expiryDate1 + " Between 3 days " + currentDate1);
                    ExpiresSoon(view);
                }
            }  else if (daysToExpiry == 0) {
                ExpiresToday(view);
            } else if (daysToExpiry < 0) {
                System.out.println(currentDate +"     " + expiryDate);
                Expired(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ExpiresSoon(View v) {
        String title = "Item will expire in: "; //date.getText().toString();
        Notification notification = new Notification.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setContentTitle("Pringles")
                .setContentText(title + daysToExpiry + " day/s")
                .build();
        notificationManager.notify(1,notification);
    }
    public void ExpiresToday(View v) {
        String title = "Item expires today!"; //date.getText().toString();
        Notification notification = new Notification.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setContentTitle("Pringles")
                .setContentText(title)
                .build();
        notificationManager.notify(1,notification);
    }
    public void Expired(View v) {
        String title = "Item has expired."; //date.getText().toString();
        Notification notification = new Notification.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setContentTitle("Pringles")
                .setContentText(title)
                .build();
        notificationManager.notify(1,notification);
    }
}