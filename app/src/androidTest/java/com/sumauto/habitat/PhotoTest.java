package com.sumauto.habitat;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sumauto.common.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
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
