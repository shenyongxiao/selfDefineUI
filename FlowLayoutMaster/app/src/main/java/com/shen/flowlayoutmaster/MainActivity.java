package com.shen.flowlayoutmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String[] mVals = new String[]
            { "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView" };

    private FlowLayout mFlowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);

        initData();
    }

    public void initData(){
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++) {
            TextView textView = (TextView) inflater.inflate(R.layout.tv, mFlowLayout, false);
            textView.setText(mVals[i]);
            mFlowLayout.addView(textView);
        }
    }
}
