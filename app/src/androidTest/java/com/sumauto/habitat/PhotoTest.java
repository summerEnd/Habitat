package com.sumauto.habitat;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sumauto.util.ImageUtil;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Created by Lincoln on 16/3/30.
 */
public class PhotoTest extends AndroidTestCase {
    private final String TAG = getClass().getSimpleName();

    public void testList() {
        List<Map<String, Object>> maps = ImageUtil.loadFolders(getContext());
        for (Map<String, Object> folder : maps) {
            Log.d(TAG, "testList: " + folder);
        }
    }
    public void testDirs(){
        Context context = getContext();
        File file = new File(context.getFilesDir().getParent(), ".store");
        file.mkdirs();
        Log.d(TAG, "testDirs: " + file);
        Log.d(TAG, "testDirs: " + context.getObbDir());
        Log.d(TAG, "testDirs: " + context.getFilesDir());
        Log.d(TAG, "testDirs: " + context.getExternalCacheDir());
        Log.d(TAG, "testDirs: " + Environment.getDataDirectory());
        Log.d(TAG, "testDirs: " + Environment.getRootDirectory());
        Log.d(TAG, "testDirs: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
    }
}
