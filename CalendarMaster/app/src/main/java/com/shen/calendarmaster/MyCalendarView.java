package com.shen.calendarmaster;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pp on 2018/4/16.
 */

public class MyCalendarView extends LinearLayout{

    LayoutInflater inflater;
    ImageView last_page;
    ImageView next_page;
    TextView current_date;
    RecyclerView calendar_date;

    Calendar curDate = Calendar.getInstance();

    private MyOnLongClickListener myOnLongClickListener;

    private String disFormat;

    public MyCalendarView(Context context) {
        super(context);
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCalendar(context,attrs);
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCalendar(context, attrs);
    }

    private void initCalendar(Context context, AttributeSet attrs){
        //初始化控件
        initView(context);
        //设置点击事件
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NewCanlender);
        String format = typedArray.getString(R.styleable.NewCanlender_dateFormat);
        disFormat = format;
        if(disFormat ==null){
            disFormat = "MMM yyyy";
        }
        initEvent(context);
        rederCalendar(context);
    }

    private void initView(Context context){
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.new_calendar, this);
        last_page = findViewById(R.id.last_page);
        next_page = findViewById(R.id.next_page);
        current_date = findViewById(R.id.current_date);
        calendar_date = findViewById(R.id.calendar_date);
    }


    private void initEvent(final Context context){
        last_page.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                rederCalendar(context);
            }
        });

        next_page.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, 1);
                rederCalendar(context);
            }
        });

    }

    private void rederCalendar(Context context){
        formatDate(disFormat);
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();
        int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) -1;

        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);

        int maxCellCount = (int)(Math.ceil(((double)prevDays + totalDays)/7))*7;
        while(cells.size() < maxCellCount){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar_date.setLayoutManager(new GridLayoutManager(context, 7));
        CalendarAdapter calendarAdapter = new CalendarAdapter(cells, context, curDate.getTime());
        calendar_date.setAdapter(calendarAdapter);
        calendarAdapter.setCalendarClickListener(new CalendarAdapter.CalendarClickListener() {
            @Override
            public void onClck(Date date) {
                if(myOnLongClickListener !=null){
                    myOnLongClickListener.onClick(date);
                }
            }
        });
    }

    private void formatDate(String formatString){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
        current_date.setText(dateFormat.format(curDate.getTime()));
    }

    public interface MyOnLongClickListener{
        void onClick(Date date);
    }

    public MyOnLongClickListener getMyOnLongClickListener() {
        return myOnLongClickListener;
    }

    public void setMyOnLongClickListener(MyOnLongClickListener myOnLongClickListener) {
        this.myOnLongClickListener = myOnLongClickListener;
    }
}
