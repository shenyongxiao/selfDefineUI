package com.shen.calendarmaster;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pp on 2018/4/16.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    private ArrayList<Date> cells;
    private Context mContext;
    private LayoutInflater inflater;
    Calendar calendar;
    Date now ;
    Date curDate;

    CalendarClickListener calendarClickListener;

    public CalendarAdapter(ArrayList<Date> cells, Context mContext, Date now){
        this.cells = cells;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        calendar = Calendar.getInstance();
        this.now = now;
        curDate = new Date();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.calendar_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Date date = cells.get(position);
        int day = date.getDate();

        boolean isSameMonth = (date.getMonth() == now.getMonth());
        holder.calendar_item.setText(day +"");
        if(isSameMonth){
            holder.calendar_item.setTextColor(Color.BLACK);
        }else{
            holder.calendar_item.setTextColor(Color.GREEN);
        }
        if(date.getYear() == curDate.getYear() && date.getMonth() == curDate.getMonth() && date.getDate() == curDate.getDate()){
            holder.calendar_item.isToday = true;
            holder.calendar_item.setTextColor(Color.parseColor("#ff0000"));
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(calendarClickListener !=null){
                    calendarClickListener.onClck(date);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cells.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public CircleTextView calendar_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            calendar_item = (CircleTextView) itemView.findViewById(R.id.calendar_item);
        }
    }

    public interface CalendarClickListener{
        void onClck(Date date);
    }

    public CalendarClickListener getCalendarClickListener() {
        return calendarClickListener;
    }

    public void setCalendarClickListener(CalendarClickListener calendarClickListener) {
        this.calendarClickListener = calendarClickListener;
    }
}
