package com.example.mtlc.opencv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Activities extends AppCompatActivity {

    RecyclerView activities;
    ActivityRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Activities");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        activities = (RecyclerView) findViewById(R.id.recycler_view);
        try {

          JSONArray data =JsonUtils.getArrayList("activities",this);

            activities.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ActivityRecyclerViewAdapter(this, data);
            activities.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            JSONArray data = new JSONArray();
            JsonUtils.saveArrayList("activities",data,this);
            activities.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ActivityRecyclerViewAdapter(this, data);
            activities.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }






    }
}


