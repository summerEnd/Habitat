package com.sumauto.habitat.activity.mine;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sumauto.common.util.DisplayUtil;
import com.sumauto.common.util.ImageUtil;
import com.sumauto.common.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotoActivity extends BaseActivity {
    TextView toolBar_title;
    RecyclerView recyclerView;
    private List<Map<String, Object>> adapterData = new ArrayList<>();
    private String[] folderNames;
    private Adapter adapter;
    private List<Map<String, Object>> folders;
    private FolderListWindow folderListWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolBar_title = (TextView) findViewById(R.id.toolBar_title);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new ItemPaddingDecoration(1, 1, 1, 1));
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        listFolderNames();
        toolBar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderListWindow == null)
                    folderListWindow = new FolderListWindow(v.getContext());
                int location[] = new int[2];
                v.getLocationInWindow(location);
                folderListWindow.showAtLocation(v, Gravity.TOP, 0, location[1] + v.getHeight());
            }
        });

    }

    /**
     * 打开一个图片目录
     *
     * @param folderName 目录名称
     */
    void displayFolder(String folderName) {
        toolBar_title.setText(folderName);
        adapterData.clear();
        adapterData.addAll(ImageUtil.listPhotos(PhotoActivity.this, folderName));
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载所有的目录
     */
    private void listFolderNames() {
        folders = ImageUtil.loadFolders(PhotoActivity.this);

        if (folders == null || folders.size() == 0) return;

        ArrayList<String> itemsArray = new ArrayList<>();
        for (Map<String, Object> map : folders) {
            itemsArray.add(map.get(MediaStore.Images.Media.BUCKET_DISPLAY_NAME).toString());
        }
        folderNames = new String[itemsArray.size()];
        itemsArray.toArray(folderNames);
        displayFolder(folderNames[0]);
    }


    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        DisplayImageOptions options;

        public Adapter() {
            options = new DisplayImageOptions.Builder()
                    .delayBeforeLoading(500)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .build();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_photo, parent, false);
            return viewType == 0 ? new CameraHolder(itemView) : new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int listPosition = position - 1;
            if (listPosition >= 0) {
                String uri = (String) adapterData.get(listPosition).get(MediaStore.Images.Media.DATA);
                ImageLoader.getInstance().displayImage(Uri.fromFile(new File(uri)).toString(), holder.imageView, options);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount() {
            return adapterData.size() + 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    class CameraHolder extends ViewHolder {
        public CameraHolder(View itemView) {
            super(itemView);
            imageView.setImageResource(R.mipmap.ic_camera);
            imageView.setBackgroundColor(0xff3E4963);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    class FolderListWindow extends PopupWindow {
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

    class FolderAdapter extends RecyclerView.Adapter<FolderHolder> {
        @Override
        public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FolderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photo_folder, parent, false));
        }

        @Override
        public void onBindViewHolder(FolderHolder holder, int position) {
            Map<String, Object> folder = folders.get(position);
            holder.init(folder);
        }

        @Override
        public int getItemCount() {
            return folders.size();
        }
    }

    class FolderHolder extends RecyclerView.ViewHolder {
        public final TextView tv_photo_count, tv_folder_name;
        public Map<String, Object> folder;

        public FolderHolder(View itemView) {
            super(itemView);
            tv_photo_count = (TextView) itemView.findViewById(R.id.tv_photo_count);
            tv_folder_name = (TextView) itemView.findViewById(R.id.tv_folder_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != folderListWindow) {
                        folderListWindow.dismiss();
                    }
                    displayFolder(folder.get(MediaStore.Images.Media.BUCKET_DISPLAY_NAME).toString());
                }
            });
        }

        public void init(Map<String, Object> folder) {
            this.folder = folder;
            tv_folder_name.setText(folder.get(MediaStore.Images.Media.BUCKET_DISPLAY_NAME).toString());
            tv_photo_count.setText(folder.get(MediaStore.Images.Media._COUNT).toString());
        }
    }
}
