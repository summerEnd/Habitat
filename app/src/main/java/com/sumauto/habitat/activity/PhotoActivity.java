package com.sumauto.habitat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.sumauto.util.IntentUtils;
import com.sumauto.util.DisplayUtil;
import com.sumauto.util.ImageUtil;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.MediaStore.Images.ImageColumns.*;

public class PhotoActivity extends BaseActivity {
    public static final int CAPTURE = 1001;
    public static final int CROP = 1002;

    private TextView tv_dirName;//显示文件夹名字
    private FolderListWindow mFolderListWindow;//文件夹列表弹窗

    private List<Map<String, Object>> mAppImages = new ArrayList<>();//本应用的图片
    private List<Map<String, Object>> mFolders = new ArrayList<>();//文件夹集合
    private Adapter mPicturesAdapter;//照片列表adapter
    private Uri mOutUri;//输出uri
    private File CAPTURE_SAVE_DIR;//本app拍照保存的目录
    private Options mOptions;

    /**
     * 图片的uri为onActivityResult的intent.getData()
     *
     * @param context     启动的Activity
     * @param options     启动参数
     * @param requestCode onActivityResult的参数
     */
    public static void start(Activity context, Options options, int requestCode) {
        Intent starter = new Intent(context, PhotoActivity.class);
        if (options != null) {
            starter.putExtra("options", options);
        }

        context.startActivityForResult(starter, requestCode);
    }

    public static List<Uri> handleResult(Intent data) {
        ArrayList<Uri> uris = new ArrayList<>();
        if (data != null) {
            if (data.getData() != null) {
                uris.add(data.getData());
            } else {
                Serializable photo_result_images = data.getSerializableExtra("photo_result_images");
                if (photo_result_images != null) {
                    //noinspection unchecked
                    uris.addAll((Collection<? extends Uri>) photo_result_images);
                }
            }
        }
        return uris;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);
        tv_dirName = (TextView) findViewById(R.id.toolBar_title);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new ItemPaddingDecoration(1, 1, 1, 1));

        mPicturesAdapter = new Adapter();
        mRecyclerView.setAdapter(mPicturesAdapter);
        initIntent();

        initDirs();

        tv_dirName.setOnClickListener(new View.OnClickListener() {
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

    private void initIntent() {
        mOptions=getIntent().getParcelableExtra("options");
        if (mOptions==null){
            mOptions=new Options();
            mOptions.multiply=false;
            mOptions.outHeight=100;
            mOptions.outWidth=100;
        }
        TextView btn_title= (TextView) findViewById(R.id.btn_title);
        if (mOptions.multiply){
            btn_title.setText(mOptions.actionName);
            btn_title.setVisibility(View.VISIBLE);
        }else{
            btn_title.setVisibility(View.INVISIBLE);
        }
    }

    private void returnData(ArrayList<Uri> uris) {
        Intent intent = getIntent();
        intent.putExtra("photo_result_images", uris);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initDirs() {
        //加载本程序拍摄的照片
        CAPTURE_SAVE_DIR = new File(getFilesDir(), "my_photos");
        loadImages(CAPTURE_SAVE_DIR);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            CAPTURE_SAVE_DIR = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_photos");
            loadImages(CAPTURE_SAVE_DIR);
        }

        //加载系统图片
        List<Map<String, Object>> maps = ImageUtil.loadFolders(this);
        if (maps != null) {
            mFolders.addAll(maps);
        }

        if (mAppImages.size() > 0) {
            displayFolder(getString(R.string.app_name), mAppImages);
        } else if (mFolders.size() != 0) {
            String folderName = mFolders.get(0).get(BUCKET_DISPLAY_NAME).toString();
            displayFolder(folderName, ImageUtil.listPhotos(this, folderName));
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
                    mAppImages.add(photo);
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
        tv_dirName.setText(folderName);
        mPicturesAdapter.reload(photos);
    }

    private Uri getSaveFileUri() {
        if (mOutUri == null) {
            mOutUri = Uri.fromFile(new File(CAPTURE_SAVE_DIR, SystemClock.uptimeMillis() + ".png"));
        }
        return mOutUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = getSaveFileUri();

            switch (requestCode) {
                case CAPTURE: {
                    startActivityForResult(IntentUtils.cropImageUri(uri, mOptions.outWidth, mOptions.outHeight), CROP);
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

    public void onCompleted(View view) {
        returnData(mPicturesAdapter.selectedUris);
    }

    /**
     * 图片列表
     */
    class Adapter extends RecyclerView.Adapter<PictureHolder> {
        DisplayImageOptions options;
        ArrayList<Uri> selectedUris = new ArrayList<>();
        List<Map<String, Object>> mAdapterData = new ArrayList<>();//当前正在展示的数据集合

        public Adapter() {
            options = ImageOptions.options(20,20);
        }

        void reload(List<Map<String, Object>> photos) {
            mAdapterData.clear();
            mAdapterData.addAll(photos);
            selectedUris.clear();
            notifyDataSetChanged();
        }

        @Override
        public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item_photo, parent, false);
            return new PictureHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final PictureHolder holder, int position) {
            int listPosition = position - 1;
            ImageView imageView = holder.imageView;
            if (listPosition >= 0) {
                imageView.setBackgroundColor(0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Map<String, Object> itemData = mAdapterData.get(listPosition);
                String path = (String) itemData.get(MediaStore.Images.Media.DATA);

                Uri uri = Uri.fromFile(new File(path));
                ImageLoader.getInstance().displayImage(uri.toString(), new ImageViewAware(imageView), options);
                holder.imageView.setTag(position);
                holder.layout_checkedMark.setVisibility(selectedUris.contains(uri) ? View.VISIBLE : View.INVISIBLE);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        Map<String, Object> itemData = mAdapterData.get(position - 1);
                        String path = (String) itemData.get(MediaStore.Images.Media.DATA);
                        Uri uri = Uri.fromFile(new File(path));
                        if (mOptions.multiply){
                            if (selectedUris.contains(uri)) {
                                selectedUris.remove(uri);
                            } else {
                                selectedUris.add(uri);
                            }
                            notifyItemChanged(position);
                        }else{
                            ArrayList<Uri> uris=new ArrayList<Uri>();
                            uris.add(uri);
                            returnData(uris);
                        }
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
        View layout_checkedMark;

        public PictureHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            layout_checkedMark = itemView.findViewById(R.id.layout_checkedMark);
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
                holder.tv_photo_count.setText(String.valueOf(mAppImages.size()));
                holder.tv_folder_name.setText(R.string.app_name);
            } else {
                Map<String, Object> folder = mFolders.get(position - 1);
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
                        displayFolder(folderName, mAppImages);
                    } else {
                        displayFolder(folderName, ImageUtil.listPhotos(PhotoActivity.this, folderName));
                    }
                }
            });
        }
    }

    public static class Options implements Parcelable {
        public int outWidth;
        public int outHeight;
        public boolean multiply;
        public String actionName;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.outWidth);
            dest.writeInt(this.outHeight);
            dest.writeByte(multiply ? (byte) 1 : (byte) 0);
            dest.writeString(this.actionName);
        }

        public Options() {
        }

        protected Options(Parcel in) {
            this.outWidth = in.readInt();
            this.outHeight = in.readInt();
            this.multiply = in.readByte() != 0;
            this.actionName = in.readString();
        }

        public static final Creator<Options> CREATOR = new Creator<Options>() {
            @Override
            public Options createFromParcel(Parcel source) {
                return new Options(source);
            }

            @Override
            public Options[] newArray(int size) {
                return new Options[size];
            }
        };
    }
}
