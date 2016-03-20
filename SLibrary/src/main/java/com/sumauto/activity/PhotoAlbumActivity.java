package com.sumauto.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sp.lib.R;
import com.sumauto.common.util.ContextUtil;
import com.sumauto.common.util.DisplayUtil;
import com.sumauto.common.util.ViewUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.sumauto.activity.PhotoAlbumActivity.PhotoDirAdapter.PhotoDirInfo;

/**
 * <pre>
 * example;
 * startActivityForResult
 * </pre>
 */
public class PhotoAlbumActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    GridView     grid;
    LinearLayout selectAlbum;

    /**
     * 图片目录和图片url集合
     */
    private       HashMap<String, LinkedList<String>> mAlbumListMap = new HashMap<String, LinkedList<String>>();
    private final int                                 CAPTURE_IMAGE = 100;
    private final int                                 CROP_IMAGE    = 101;

    /**
     * 输出图片的高度
     */
    public static String EXTRA_CAMERA_OUTPUT_HEIGHT = "camera_out_height";
    /**
     * 输出图片的宽度
     */
    public static String EXTRA_CAMERA_OUTPUT_WIDTH  = "camera_out_width";

    private int                outPut_height;
    private int                outPut_width;
    /**
     * 当前相册列表
     */
    private LinkedList<String> mAlbumList;
    PhotoGridAdapter photoAdapter;
    private PhotoDirWindow mPhotoDirWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_albumn);
        initParams();
    }

    /**
     * 初始化参数
     */
    void initParams() {
        outPut_height = getIntent().getIntExtra(EXTRA_CAMERA_OUTPUT_HEIGHT, 30);
        outPut_width = getIntent().getIntExtra(EXTRA_CAMERA_OUTPUT_WIDTH, 30);
        grid = (GridView) findViewById(R.id.grid);
        selectAlbum = (LinearLayout) findViewById(R.id.bottom_ll);
        selectAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });
        loadPictures();
    }

    private File createFile(String fileName) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (dir == null) {
            ContextUtil.toast("sdcard is not available");
            dir = getFilesDir();
        }

        return new File(dir, fileName);
    }

    /**
     * 加载图片
     */
    private void loadPictures() {

        String[] projection = {
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_MODIFIED};
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC");

        LinkedList<String> totalList = new LinkedList<String>();
        mAlbumList = totalList;
        //全部
        String dir_all = getString(R.string.all);
        //其他
        String dir_other = getString(R.string.other);


        mAlbumListMap.put(dir_all, totalList);
        while (cursor.moveToNext()) {
            String url = cursor.getString(0);
            String dirName = url.replace("/sdcard/", "").replace("/mnt", "").replace("/ext_sdcard/", "").replace("/sdcard0/", "");
            //获取目录名称
            try {
                dirName = dirName.substring(0, dirName.indexOf('/'));
            } catch (Exception e) {
                dirName = dir_other;
            }
            //从map中获取一个 list，如果没有就创建一个
            LinkedList<String> tempList = mAlbumListMap.get(dirName);
            if (tempList == null) {
                tempList = new LinkedList<>();
                mAlbumListMap.put(dirName, tempList);
            }
            tempList.add(url);
            totalList.add(url);
        }
        photoAdapter = new PhotoGridAdapter(PhotoAlbumActivity.this, mAlbumListMap.get(getString(R.string.all)));
        grid.setAdapter(photoAdapter);
        grid.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT < 14) {
            //在14以上版本，cursor会自动关闭，如果手动关闭在resume的时候会抛出异常。
            cursor.close();
        }

    }

    /**
     * 创建popupWindow
     */
    private void showWindow() {
        if (mPhotoDirWindow == null) {
            //收集相册信息
            ArrayList<PhotoDirInfo> dirs = new ArrayList<PhotoDirInfo>();
            for (String dirName : mAlbumListMap.keySet()) {
                PhotoDirInfo info = new PhotoDirInfo();
                info.dirName = dirName;
                info.photos = mAlbumListMap.get(dirName);
                dirs.add(info);
            }

            mPhotoDirWindow = new PhotoDirWindow(this, dirs) {
                @Override
                protected void onSelected(PhotoDirInfo info) {
                    //获取目录对应的图片列表
                    String dirName = info.dirName;
                    mAlbumList = mAlbumListMap.get(dirName);
                    //设置标题为目录名称
                    setTitle(dirName);
                    photoAdapter.setData(mAlbumList);
                    mPhotoDirWindow.dismiss();
                }
            };
        }
        mPhotoDirWindow.showWithAnimation(selectAlbum);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //打开相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());

            Uri cameraUri = Uri.fromFile(createFile("SLIB_" + sdf.format(new Date())));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);

            startActivityForResult(intent, CAPTURE_IMAGE);
        }
        else {
            //选择照片，返回照片Uri
            File selected = new File(mAlbumList.get(position - 1));
            cropImage(Uri.fromFile(selected));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, R.string.fail, Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == CAPTURE_IMAGE) {
            cropImage(data.getData());
            //拍照返回
        }
        else if (requestCode == CROP_IMAGE) {
            //裁剪返回
            setResult(RESULT_OK, data);
            finish();
        }
    }

    Map map;

    /**
     * 剪裁图片
     */
    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", outPut_width);
        intent.putExtra("aspectY", outPut_height);
        intent.putExtra("outputX", outPut_width);
        intent.putExtra("outputY", outPut_height);
        //        intent.putExtra("output", cameraUri);// 保存到原文件
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_IMAGE);
    }

    public static class PhotoDirAdapter extends BaseAdapter {
        DisplayImageOptions options;
        Context             context;
        private List<PhotoDirInfo> dirs;
        int selectedItem;

        public PhotoDirAdapter(Context context, List<PhotoDirInfo> dirs) {
            this.context = context;
            this.dirs = dirs;
            options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        }

        public void select(int position) {
            selectedItem = position;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return dirs.size();
        }

        @Override
        public Object getItem(int position) {
            return dirs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.photo_album_window_list_item, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                holder.dir_name = (TextView) convertView.findViewById(R.id.dir_name);
                holder.num_pics = (TextView) convertView.findViewById(R.id.num_pics);
                holder.selected = (ImageView) convertView.findViewById(R.id.selected);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotoDirInfo info = dirs.get(position);
            List<String> list = info.photos;
            if (list.size() > 0) {
                String image = list.get(0);
                ImageLoader.getInstance().displayImage("file://" + image, holder.imageView, options);
            }
            holder.dir_name.setText(info.dirName);
            holder.num_pics.setText(list.size() + "张");
            holder.selected.setVisibility(selectedItem == position ? View.VISIBLE : View.INVISIBLE);
            convertView.setTag(holder);
            return convertView;
        }


        public static class PhotoDirInfo {
            public String       dirName;
            public List<String> photos;
        }

        private static class ViewHolder {
            ImageView imageView;
            TextView  dir_name;
            TextView  num_pics;
            ImageView selected;
        }
    }

    public static class PhotoDirWindow extends PopupWindow implements AdapterView.OnItemClickListener {
        View               contentView;
        ListView           list;
        Context            context;
        List<PhotoDirInfo> dirs;
        PhotoDirAdapter    adapter;

        public PhotoDirWindow(Context context, List<PhotoDirInfo> dirs) {
            super(context);
            this.context = context;
            this.dirs = dirs;
            setHeight(MATCH_PARENT);
            setWidth(MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(0x80000000));
            contentView = View.inflate(context, R.layout.photo_ablum_layout, null);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            list = (ListView) contentView.findViewById(R.id.listView);
            adapter = new PhotoDirAdapter(context, dirs);
            list.setAdapter(adapter);
            list.setOnItemClickListener(this);
            setContentView(contentView);
        }


        /**
         * 动画弹出
         */
        public void showWithAnimation(View v) {
            showAtLocation(v, Gravity.TOP, 0, -v.getHeight());
            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_up_in);
            animation.setDuration(200);
            list.startAnimation(animation);
        }

        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            onSelected(dirs.get(position));
            adapter.select(position);
        }

        protected void onSelected(PhotoDirInfo info) {
        }
    }

    /**
     * 照片
     */
    public  class PhotoGridAdapter extends BaseAdapter {
        private Context      mContext;
        private List<String> photos;
        DisplayImageOptions options;
        int                 width;

        @SuppressWarnings("deprecation")
        public PhotoGridAdapter(Context context, List<String> photos) {
            this.mContext = context;
            this.photos = photos;

            options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnFail(R.drawable.image_failed).showImageForEmptyUri(R.drawable.image_failed).build();
            float screenWidth = DisplayUtil.getScreenWidth(context);
            width = (int) (screenWidth / 3 - DisplayUtil.dp(1, context.getResources()));
        }

        public void setData(List<String> photos) {
            this.photos = photos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return photos.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return "";
            }
            else {
                return photos.get(position - 1);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.camera_header, parent,false);
                ViewUtil.makeSquare(convertView, width);
            }
            else {
                ViewHolder holder;
                if (convertView == null || convertView.getTag() == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_photo,parent,false);
                    holder.imageView = (ImageView) convertView;
                    ViewUtil.makeSquare(convertView,width);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                String imageUrl = photos.get(position - 1);
                ImageLoader.getInstance().displayImage("file://" + imageUrl, holder.imageView, options);
                convertView.setTag(holder);
            }

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0)return 0;
            return 1;
        }

        private class ViewHolder {
            ImageView imageView;
        }
    }
}