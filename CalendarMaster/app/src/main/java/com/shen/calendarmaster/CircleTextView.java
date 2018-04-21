package com.shen.calendarmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by pp on 2018/4/16.
 */

public class CircleTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint paint = new Paint();
    public boolean isToday = false;
    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }


    private void initControl(Context context){
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor("#ff0000"));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isToday){
            canvas.translate(getWidth()/2, getHeight()/2);
            canvas.drawCircle(0,0, getHeight()/2, paint);
        }
    }
}
