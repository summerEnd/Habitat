package com.sumauto.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lincoln on 2016/3/16.
 */
public class GifUtils {
    public AnimationDrawable decodeGif(File file) {

        try {
            FileInputStream is = new FileInputStream(file);
            return decodeGif(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AnimationDrawable decodeGif(InputStream is) {
        GifHelper gifHelper = new GifHelper();
        try {
            if (GifHelper.STATUS_OK == gifHelper.read(is)) {
                AnimationDrawable animationDrawable = new AnimationDrawable();
                int frameCount = gifHelper.getFrameCount();
                //animationDrawable.addFrame(new BitmapDrawable(getResources(), onCreateFrame(gifHelper.getImage())), gifHelper.getDelay(50));
                for (int i = 0; i < frameCount; i++) {
                    Bitmap frame = gifHelper.getFrame(i);
                    int delay = gifHelper.getDelay(i);
                    if (delay == 0) delay = 150;
                    animationDrawable.addFrame(new BitmapDrawable(Resources.getSystem(), frame), delay);
                }
                animationDrawable.setOneShot(false);
                return animationDrawable;
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
