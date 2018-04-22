package com.shen.defineviewpager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ImageBanner ban;

    private int[] id = {R.drawable.ban1,R.drawable.ban2,R.drawable.ban3,R.drawable.ic_launcher_round};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ban = (ImageBanner) findViewById(R.id.ban);
        getPhoneWidth();
        initData();


        ban.setOnImageViewSelectClick(new ImageBanner.OnImageViewSelectClick() {
            @Override
            public void OnClickListener(int position) {
                Toast.makeText(MainActivity.this,"hello"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<Bitmap> bitmaps = new ArrayList<>();
    private void initData() {
        for (int i = 0; i < id.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id[i]);
            bitmaps.add(bitmap);
        }
        ban.addImageToImageBarnner(bitmaps);
    }

    private int  getPhoneWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
