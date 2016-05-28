package com.shuttl.testapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends Activity {

    TextView nameView, addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        nameView = (TextView) findViewById(R.id.nameView);
        addressView = (TextView) findViewById(R.id.addressView);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String address = intent.getExtras().getString("address");

        nameView.setText(name);
        addressView.setText(address);

    }

    @Override
    public void onBackPressed(){
        //do something
        super.onBackPressed();
    }
}
