package com.sumauto.habitat;

import android.test.AndroidTestCase;
import android.util.Log;

import com.sumauto.util.ImageUtil;

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
}
