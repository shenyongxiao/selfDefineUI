package com.shen.touchpullmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private float mTouchStartY;
    //y轴移动的最大值
    private static final float TOUCH_MOVE_MAX_Y = 600;
    private TouchPullView touchPullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        touchPullView = findViewById(R.id.touchView);
        findViewById(R.id.ll_mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionMasked = event.getActionMasked();
                switch (actionMasked){
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float y = event.getY();
                        if(y > mTouchStartY){
                            float moveSize = y- mTouchStartY;
                            float progress = moveSize > TOUCH_MOVE_MAX_Y ?
                                    1: moveSize/ TOUCH_MOVE_MAX_Y;
                            touchPullView.setProgress(progress);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        touchPullView.release();
                        return true;
                    default:
                        break;

                }
                return false;
            }
        });
    }
}
