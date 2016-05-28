package com.shuttl.testapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView mainListView;
    ProgressDialog progressDialog;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mainListView = (ListView) findViewById(R.id.listView);
        adapter = new MyAdapter(MainActivity.this, R.layout.layout_list_item, new ArrayList<String>());
        mainListView.setAdapter(adapter);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> data = new ArrayList<String>();
                data.add("piyush");
                data.add("manish");
                data.add("shubham");
                data.add("ankit");
                data.add("meet");
                data.add("vaibhav");

                adapter.changeData(data);
            }
        }, 15000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://goplus.in/shuttl/locationForRoute?routeId=3")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //call failed
                        Log.d("DATA", e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Error, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try{

                                    progressDialog.dismiss();

                                    if(response.isSuccessful()){
                                        String json = response.body().string();

                                        Log.d("data", json);

                                        try {
                                            JSONObject obj = new JSONObject(json);
                                            JSONArray dataArray = obj.getJSONArray("data");

                                            ArrayList<String> list = new ArrayList<>();

                                            for(int i=0; i < dataArray.length(); i++){
                                                JSONObject data = dataArray.getJSONObject(i);
                                                String name = data.getString("locationName");
                                                list.add(name);
                                                Log.d("data > name", name);
                                            }

                                            adapter.changeData(list);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });


                    }
                });
            }
        }, 5000);

    }


    public class MyAdapter extends ArrayAdapter{

        Context context;
        int resource;
        ArrayList<String> names;

        public MyAdapter(Context context, int resource, ArrayList<String> names) {
            super(context, resource);
            this.context = context;
            this.resource = resource;
            this.names = names;
        }

        public void changeData(ArrayList<String> newNames){
            this.names = newNames;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public String getItem(int position) {
            return names.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                //this means object is being created for first time
                convertView = getLayoutInflater().inflate(resource, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.list_item_name);
            Log.d("previousName", name.getText().toString());
            name.setText(names.get(position));

            return convertView;
        }
    }
}
