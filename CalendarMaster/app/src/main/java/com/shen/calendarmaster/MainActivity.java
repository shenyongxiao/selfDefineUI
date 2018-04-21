package com.shen.calendarmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    MyCalendarView main_calendar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_calendar_view = findViewById(R.id.main_calendar_view);
        main_calendar_view.setMyOnLongClickListener(new MyCalendarView.MyOnLongClickListener() {
            @Override
            public void onClick(Date date) {
                Toast.makeText(MainActivity.this, date.getDate() + "", Toast.LENGTH_LONG).show();
            }
        });
    }
}
