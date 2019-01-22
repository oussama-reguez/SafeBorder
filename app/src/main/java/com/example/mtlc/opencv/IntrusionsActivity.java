package com.example.mtlc.opencv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntrusionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrusions);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new ManagePhotoFragment(), "")


                .commit();


    }
}
