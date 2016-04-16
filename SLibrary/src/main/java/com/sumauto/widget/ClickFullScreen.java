package com.sumauto.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.R;
import com.sumauto.util.DisplayUtil;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ClickFullScreen extends PopupWindow {
    private Context context;
    PhotoView mImageView;
    View      container;
    String    imageUrl;
    private Bitmap runBitmap;
    private float  fromX;
    private float  fromY;
    private float  toX;
    private float  toY;
    private float  pivotX;
    private float  pivotY;
    /**
     * 打开动画监听
     */
    private SimpleAnimationListener openAnimationListener = new SimpleAnimationListener()
    {

        @Override
        public void onAnimationEnd(Animation animation)
        {
            if (!TextUtils.isEmpty(imageUrl))
            {

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();

                ImageLoader.getInstance().displayImage(imageUrl, mImageView, options);
            }
            container.setBackgroundColor(Color.BLACK);
        }
    };

    /**
     * 打开动画监听
     */
    private SimpleAnimationListener closeAnimationListener = new SimpleAnimationListener()
    {

        @Override
        public void onAnimationEnd(Animation animation)
        {
            dismiss();
        }
    };

    public ClickFullScreen(Context context)
    {
        super(context);
        this.context = context;
        container = View.inflate(context, R.layout.image_layout, null);
        mImageView = (PhotoView) container.findViewById(R.id.image);
        setContentView(container);
        setWidth(MATCH_PARENT);
        setHeight(MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ScaleAnimation animation = new ScaleAnimation(toX, fromX, toY, fromY, pivotX, pivotY);
                animation.setDuration(300);
                container.setBackgroundColor(0);
                animation.setAnimationListener(closeAnimationListener);
                container.startAnimation(animation);
            }
        });
    }

    public void setNetWorkImage(String url)
    {
        this.imageUrl = url;
    }

    /**
     * 过度图片
     *
     * @param bm
     */
    public void setRunBitmap(Bitmap bm) {
        runBitmap = bm;
    }

    public void show(View token, Rect fromRect) {
        //设置背景透明
        container.setBackgroundColor(0);
        //获取anchor在屏幕中的位置
        showAtLocation(token, Gravity.NO_GRAVITY, 0, 0);
        //计算动画参数
        DisplayMetrics metrics = DisplayUtil.getDisplayMetrics(context);

        Bitmap bitmap = runBitmap != null ? runBitmap : Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(bitmap);
        //根据bitmap宽高比计算mImageView所占空间
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        //图像宽度满屏，高度自适应
        float scale = metrics.widthPixels / (imageWidth * 1.0f);
        int height = (int) (imageHeight * scale);
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, height));

        animateOpen(fromRect);
    }

    public void showWithUrl(View anchor,String url){
        //设置背景透明
        container.setBackgroundColor(Color.BLACK);
        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
        ImageLoader.getInstance().displayImage(url,mImageView,new DisplayImageOptions.Builder().build());
        mImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener()
        {
            @Override
            public void onViewTap(View view, float v, float v1)
            {
                dismiss();
            }
        });
    }

    public void showFor(View anchor, boolean useCache) {
        //设置背景透明
        container.setBackgroundColor(Color.BLACK);

        //获取anchor在屏幕中的位置
        int location[] = new int[2];
        anchor.getLocationOnScreen(location);
        Rect rect = new Rect(anchor.getLeft(), anchor.getTop(), anchor.getRight(), anchor.getBottom());
        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
        //计算动画参数
        DisplayMetrics metrics = DisplayUtil.getDisplayMetrics(anchor.getContext());
        fromX = anchor.getWidth() / (metrics.widthPixels * 1f);
        toX = 1f;
        fromY = anchor.getHeight() / (metrics.heightPixels * 1f);
        toY = 1f;

        pivotX = rect.width() / 2 + location[0];
        pivotY = rect.height() / 2 + location[1];
        Bitmap bitmap;
        if (useCache) {
            //去除anchor缓存
            anchor.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(anchor.getDrawingCache());
            anchor.setDrawingCacheEnabled(false);
        } else {
            bitmap = runBitmap != null ? runBitmap : Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        }
        mImageView.setImageBitmap(bitmap);
        mImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1)
            {
                dismiss();
            }
        });
    }

    /**
     * PS:
     *
     * @param anchor 弹出位置
     */
    public void showFor(View anchor) {
        showFor(anchor, true);
    }

    private void animateOpen(Rect start) {
        DisplayMetrics me = DisplayUtil.getDisplayMetrics(context);
        float scaleFrom = start.width() / me.widthPixels;
        float translateToX = me.widthPixels / 2;
        float translateToY = me.heightPixels / 2;

        AnimationSet set = new AnimationSet(true);
        set.setDuration(300);
        set.addAnimation(new ScaleAnimation(scaleFrom, 1, scaleFrom, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        int fromXDelta = start.left + start.width() / 2;
        int fromYDelta = start.top + start.height() / 2;
        set.addAnimation(new TranslateAnimation(fromXDelta, translateToX, fromYDelta, translateToY));
        set.setAnimationListener(openAnimationListener);
        mImageView.startAnimation(set);
    }

    private class SimpleAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
