package com.sumauto.habitat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.util.IntentUtils;
import com.sumauto.util.DisplayUtil;
import com.sumauto.util.ImageUtil;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoActivity extends BaseActivity {
    public static final int CAPTURE = 1001;
    public static final int CROP = 1002;

    TextView mToolBarTitle;
    RecyclerView mRecyclerView;
    private FolderListWindow mFolderListWindow;

    private List<Map<String, Object>> mLocaleImages = new ArrayList<>();
    private List<Map<String, Object>> mFolders;
    private List<Map<String, Object>> mAdapterData = new ArrayList<>();
    private String[] mFolderNames;
    private Adapter mAdapter;
    private Uri outUri;
    private int width;
    private int height;

    private File IMAGES_DIR;

    public static void start(Activity context, int width, int height, int requestCode) {
        Intent starter = new Intent(context, PhotoActivity.class);
        starter.putExtra("width", width);
        starter.putExtra("height", height);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mToolBarTitle = (TextView) findViewById(R.id.toolBar_title);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new ItemPaddingDecoration(1, 1, 1, 1));

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        width = getIntent().getIntExtra("width", 100);
        height = getIntent().getIntExtra("height", 100);

        initDirs();

        mToolBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFolderListWindow == null)
                    mFolderListWindow = new FolderListWindow(v.getContext());
                int location[] = new int[2];
                v.getLocationInWindow(location);
                mFolderListWindow.showAtLocation(v, Gravity.TOP, 0, location[1] + v.getHeight());
            }
        });

    }

    private void initDirs() {
        //加载本程序拍摄的照片
        IMAGES_DIR = new File(getFilesDir(), "my_photos");
        loadImages(IMAGES_DIR);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            IMAGES_DIR = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_photos");
            loadImages(IMAGES_DIR);
        }

        //加载系统图片
        mFolders = ImageUtil.loadFolders(this);
        if (mFolders == null || mFolders.size() == 0) return;

        ArrayList<String> itemsArray = new ArrayList<>();
        for (Map<String, Object> map : mFolders) {
            itemsArray.add(map.get(MediaStore.Images.Media.BUCKET_DISPLAY_NAME).toString());
        }
        mFolderNames = new String[itemsArray.size()];
        itemsArray.toArray(mFolderNames);
        if (mLocaleImages.size()>0){
            displayFolder(getString(R.string.app_name), mLocaleImages);
        }else{
            displayFolder(mFolderNames[0], ImageUtil.listPhotos(this, mFolderNames[0]));
        }
    }

    private void loadImages(File dir) {
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        } else {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    Map<String, Object> photo = new HashMap<>();
                    photo.put(MediaStore.Images.Media.DATA, f.getAbsolutePath());
                    mLocaleImages.add(photo);
                }
            }
        }
    }

    /**
     * 打开一个图片目录
     *
     * @param folderName 目录名称
     */
    private void displayFolder(String folderName, List<Map<String, Object>> photos) {
        mToolBarTitle.setText(folderName);
        mAdapterData.clear();
        mAdapterData.addAll(photos);
        mAdapter.notifyDataSetChanged();
    }

    private Uri getSaveFileUri() {
        if (outUri == null) {
            outUri = Uri.fromFile(new File(IMAGES_DIR, SystemClock.uptimeMillis() + ".png"));
        }
        return outUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = getSaveFileUri();

            switch (requestCode) {
                case CAPTURE: {
                    startActivityForResult(IntentUtils.cropImageUri(uri, width, height), CROP);
                    break;
                }
                case CROP: {
                    Intent intent = getIntent();
                    intent.setData(getSaveFileUri());
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                }
            }
        }
    }

    /**
     * 图片列表
     */
    class Adapter extends RecyclerView.Adapter<PictureHolder> {
        DisplayImageOptions options;

        public Adapter() {
            options = ImageOptions.options();
        }

        @Override
        public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_photo, parent, false);
            return new PictureHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PictureHolder holder, int position) {
            int listPosition = position - 1;
            ImageView imageView = holder.imageView;
            if (listPosition >= 0) {
                imageView.setBackgroundColor(0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                String uri = (String) mAdapterData.get(listPosition).get(MediaStore.Images.Media.DATA);
                ImageLoader.getInstance().displayImage(Uri.fromFile(new File(uri)).toString(), imageView, options);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                imageView.setImageResource(R.mipmap.ic_camera);
                imageView.setBackgroundColor(0xff3E4963);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(IntentUtils.capture(getSaveFileUri()), CAPTURE);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mAdapterData.size() + 1;
        }
    }

    private class PictureHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PictureHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }


    private class FolderListWindow extends PopupWindow {
        public FolderListWindow(Context context) {
            super(context);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new FolderAdapter());
            recyclerView.addItemDecoration(new DividerDecoration(0xffe5e5e5));
            setContentView(recyclerView);
            setBackgroundDrawable(ViewUtil.getDrawable(context, R.drawable.round_corner));
            setFocusable(true);
            setWidth((int) DisplayUtil.dp(170, context.getResources()));
            setHeight((int) DisplayUtil.dp(210, context.getResources()));
        }
    }

    /**
     * 弹窗 图片目录列表
     */
    private class FolderAdapter extends RecyclerView.Adapter<FolderHolder> {
        @Override
        public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FolderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photo_folder, parent, false));
        }

        @Override
        public void onBindViewHolder(FolderHolder holder, int position) {
            if (position == 0) {
                holder.tv_photo_count.setText(String.valueOf(mLocaleImages.size()));
                holder.tv_folder_name.setText(R.string.app_name);
            } else {
                Map<String, Object> folder = mFolders.get(position-1);
                holder.tv_folder_name.setText(folder.get(MediaStore.Images.Media.BUCKET_DISPLAY_NAME).toString());
                holder.tv_photo_count.setText(folder.get(MediaStore.Images.Media._COUNT).toString());
            }
        }

        @Override
        public int getItemCount() {
            return mFolders.size() + 1;
        }
    }

    private class FolderHolder extends RecyclerView.ViewHolder {
        public final TextView tv_photo_count, tv_folder_name;

        public FolderHolder(View itemView) {
            super(itemView);
            tv_photo_count = (TextView) itemView.findViewById(R.id.tv_photo_count);
            tv_folder_name = (TextView) itemView.findViewById(R.id.tv_folder_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFolderListWindow) {
                        mFolderListWindow.dismiss();
                    }
                    String folderName = tv_folder_name.getText().toString();

                    if (getAdapterPosition() == 0) {
                        displayFolder(folderName, mLocaleImages);
                    } else {
                        displayFolder(folderName, ImageUtil.listPhotos(PhotoActivity.this, folderName));
                    }
                }
            });
        }
    }
}
