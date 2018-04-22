package com.shen.defineviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;


/**
 * Created by pp on 2018/4/22.
 */

public class ImageBanner extends FrameLayout implements Banner.ImageBannerListener, Banner.ImageBannerSelect{

    //是否开启自动循环播放默认为true
    private boolean mIsAutoCycle;
    //时间间隔，默认为3000毫秒
    private int mTimeInerval = 3000;

    private LinearLayout linearLayout;
    private Banner banner;


    public OnImageViewSelectClick getOnImageViewSelectClick() {
        return onImageViewSelectClick;
    }

    public void setOnImageViewSelectClick(OnImageViewSelectClick onImageViewSelectClick) {
        this.onImageViewSelectClick = onImageViewSelectClick;
    }

    private OnImageViewSelectClick onImageViewSelectClick;

    @Override
    public void OnClickListener(int pos) {
        onImageViewSelectClick.OnClickListener(pos);
    }

    @Override
    public void selectImage(int pos) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == pos) {
                iv.setImageResource(R.drawable.dot_selectl);
            } else {
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    public interface OnImageViewSelectClick {
        void OnClickListener(int position);
    }
    public ImageBanner(@android.support.annotation.NonNull Context context) {
        this(context, null);
    }

    public ImageBanner(@android.support.annotation.NonNull Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBanner(@android.support.annotation.NonNull Context context, @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs,context);
        initImageBarnner();
        initDotLinearLayout();
    }

    private void initData(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.imageBanner);
        mIsAutoCycle = typedArray.getBoolean(R.styleable.imageBanner_auto_loop, true);
        mTimeInerval = typedArray.getInteger(R.styleable.imageBanner_time_interval, 3000);
    }


    private void initImageBarnner() {
        Banner.TIME_INTERVAL = mTimeInerval;
        banner = new Banner(getContext());
        addView(banner);
        LayoutParams layoutParams = (LayoutParams) banner.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        banner.setImageBannerSelect(this);
        banner.setListener(this);
    }

    public void addImageToImageBarnner(List<Bitmap> bitmaps) {
        for (int i = 0; i < bitmaps.size(); i++) {
            addImageToBanner(bitmaps.get(i));
            addDotLinearLayout();
        }
    }


    private void addDotLinearLayout() {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        lp.setMargins(5, 5, 5, 5);
        imageView.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(imageView);
    }
    private void addImageToBanner(Bitmap bm) {
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        banner.addView(iv);
    }

    private void initDotLinearLayout() {
        linearLayout = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        //    linearLayout.setBackground(Color.RED);
        addView(linearLayout);

        LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);

        //3.0版本使用的是setAlpha() 在3.0之前使用的是setAlpha(),但是调用者不同
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            linearLayout.setAlpha(0.5f);
        } else {
            linearLayout.getBackground().setAlpha(100);
        }
    }

}
