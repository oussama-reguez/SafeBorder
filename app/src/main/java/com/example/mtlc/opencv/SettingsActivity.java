package com.example.mtlc.opencv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.zcw.togglebutton.ToggleButton;

import java.io.File;

import mehdi.sakout.fancybuttons.FancyButton;

public class SettingsActivity extends AppCompatActivity {

ToggleButton btnBackground;
    ToggleButton median;
    ToggleButton gaussian;
    ToggleButton btnBlur;
    FancyButton btnRemove;
    FancyButton btnClear;

    void getSettings(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if( prefs.getBoolean("blur",false))
        {
            btnBlur.toggleOn();

        }
        else {
            btnBlur.toggleOff();
        }

        if( prefs.getBoolean("gaussian",false))
        {
          gaussian.toggleOn();
        }
        else {
gaussian.toggleOff();
        }


        if( prefs.getBoolean("median",false))
        {
median.toggleOn();
        }
        else {
median.toggleOff();
        }
        if( prefs.getBoolean("background",false))
        {
btnBackground.toggleOn();
        }
        else {
btnBackground.toggleOff();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new         Intent(getApplicationContext(),MainActivity.class));
            }
        });

        median =(ToggleButton) findViewById(R.id.btn_median);
        btnBlur =(ToggleButton) findViewById(R.id.btn_blur);
        gaussian =(ToggleButton) findViewById(R.id.btn_gaussian);
        btnRemove= (FancyButton) findViewById(R.id.btn_remove);
        btnClear= (FancyButton) findViewById(R.id.btn_clear);
        btnBackground = (ToggleButton) findViewById(R.id.btn_detection);

        getSettings();
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("activities", "{}");
                editor.apply();

                Toast.makeText(SettingsActivity.this, "log cleared successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File check = new File(getCacheDir(),"intrusions");
                for(File  f: check.listFiles() ){
                    f.delete();
                }

                Toast.makeText(SettingsActivity.this, "Images removed successfully", Toast.LENGTH_SHORT).show();
            }
        });

        median.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {

                JsonUtils.updateSetting("median",on,SettingsActivity.this);
                if(on){
                    btnBlur.setToggleOff();
                    gaussian.setToggleOff();
                }

            }
        });

        btnBlur.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                JsonUtils.updateSetting("blur",on,SettingsActivity.this);
                if(on){
                    gaussian.toggleOff();
                    median.toggleOff();
                }


            }
        });



        gaussian.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                JsonUtils.updateSetting("gaussian",on,SettingsActivity.this);
                if(on){
                    btnBlur.toggleOff();
                    median.toggleOff();
                }

            }
        });


         btnBackground.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
             @Override
             public void onToggle(boolean on) {
                 JsonUtils.updateSetting("background",on,SettingsActivity.this);
                 Intent ii= new Intent(SettingsActivity.this, OpenCvService.class);

                 if(on){

                     startService(ii);
                     Toast.makeText(SettingsActivity.this, "Background detection service started ", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     stopService(ii);
                     Toast.makeText(SettingsActivity.this, "Background detection service stopped ", Toast.LENGTH_SHORT).show();
                 }





             }
         });

    }
}
