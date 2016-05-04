package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.widget.PickCity;
import com.sumauto.habitat.widget.PickerView;

public class CreateCommunity extends BaseActivity {
    @ViewId EditText edit_commit_name;
    private PickCity pickCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);
    }

    public void onChooseCity(View view) {
        if (pickCity==null){
            pickCity = new PickCity(this);
        }
        pickCity.show();
    }

    public void onCreateCommunity(View view) {
//        Requests.createCommunity(string(edit_commit_name),)
    }
}
