package com.sumauto.habitat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.widget.PickCity;

/**
 * 创建小区
 */
public class CreateCommunity extends BaseActivity {
    @ViewId EditText edit_commit_name;
    @ViewId TextView tv_address;
    private PickCity pickCity;

    public static void start(Context context,String name) {
        Intent starter = new Intent(context, CreateCommunity.class);
        starter.putExtra("name",name);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);
        pickCity = new PickCity(this){
            @Override
            protected void onSelect() {
                tv_address.setText(getCityString());
            }
        };
        edit_commit_name.setText(getIntent().getStringExtra("name"));
    }

    public void onChooseCity(View view) {

        pickCity.show();
    }

    public void onCreateCommunity(View view) {
        String title = edit_commit_name.getText().toString().trim();
        HttpRequest<String> request = Requests.createCommunity(
                title, pickCity.getProvince(), pickCity.getCity(), pickCity.getArea(), "");

        HttpManager.getInstance().post(this, new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {

            }
        });
    }
}
