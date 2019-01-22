package com.example.mtlc.opencv;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.opencv.objdetect.HOGDescriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    RelativeLayout intrusions;
    RelativeLayout live;
    RelativeLayout settings;


    RelativeLayout logs;
    ImageView imageProtection;
    FancyButton button;
    NotificationBadge badge;
    boolean protectionDisabled ;
    boolean serviceStarted;
    TextView txtProtection;



    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }


    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

boolean useService ;
    @Override
    protected void onResume() {
        super.onResume();
        File check = new File(getCacheDir(),"intrusions");
        badge.clear();
        badge.setNumber(check.listFiles().length,true);

        Intent ii= new Intent(MainActivity.this, OpenCvService.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        useService=   prefs.getBoolean("background",false);
        if( useService){
            if(!isMyServiceRunning(OpenCvService.class))
                startService(ii);


        }

/*
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                Log.e("checking folder","checking folder ");
                //do something
                File myDir = new File(getCacheDir(), "intrusions");
                badge.clear();
                badge.setNumber( myDir.listFiles().length);
                handler.postDelayed(this, delay);
            }
        }, delay);
        */

    }
    Handler handler = new Handler();
    int delay = 3000; //milliseconds
    String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.e("hh","hh");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       useService=   prefs.getBoolean("background",false);
       protectionDisabled =prefs.getBoolean("protection",false);



        if(useService){
            Intent ii= new Intent(MainActivity.this, OpenCvService.class);
                startService(ii);
                serviceStarted=true;

        }

        File check = new File(getCacheDir(),"intrusions");
        path=check.toString();
        if(!check.exists()){
            File myDir = new File(getCacheDir(), "intrusions");
            myDir.mkdir();
        }




/*
       Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.example.mtlc.opencv/cache/IMG_1542118271036.jpeg");
        try (FileOutputStream out = new FileOutputStream(getCacheDir()+"/intrusions/"+System.currentTimeMillis()+"_profile.jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
      //  File ff = new File("/storage/emulated/0/Android/data/com.example.mtlc.opencv/cache");
        File ff= check;
        int f= 0;
        if(ff!=null){
            File [] d = ff.listFiles();
              f = ff.listFiles().length ;
        }



            txtProtection=(TextView) findViewById(R.id.txtSystem) ;

           button = (FancyButton) findViewById(R.id.btn_public);
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                   SharedPreferences.Editor editor = prefs.edit();


                   if(!protectionDisabled){
                       button.setText("Enable Protection");
                       imageProtection.setImageResource(R.drawable.error2);
                       try {
                           DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                           String date = df.format(Calendar.getInstance().getTime());
                           txtProtection.setText("Border Not Secured");
                           txtProtection.setTextColor(Color.rgb(216, 0, 39));

                           JsonUtils.addActivity("Protection Disabled", date,"error2",MainActivity.this);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                       button.setBackgroundColor(getResources().getColor(R.color.flatGreen));
                       protectionDisabled=true;
                   }else{
                       button.setText("Disable Protection");

                       button.setBackgroundColor(Color.rgb(216, 0, 39));
                       protectionDisabled=false;
                       txtProtection.setText("Border Secured");
                       txtProtection.setTextColor(getResources().getColor(R.color.flatGreen));
                       imageProtection.setImageResource(R.drawable.shieldon2);
                       try {
                           DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                           String date = df.format(Calendar.getInstance().getTime());
                           JsonUtils.addActivity("Protection Enabled", date,"shieldon2",MainActivity.this);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }

                   editor.putBoolean("protection",protectionDisabled );
                   editor.apply();

               }
           });
           intrusions = (RelativeLayout) findViewById(R.id.intrusions);
           live = (RelativeLayout) findViewById(R.id.track);
           settings = (RelativeLayout) findViewById(R.id.settings);
            logs = (RelativeLayout) findViewById(R.id.activities);
           badge =(NotificationBadge) findViewById(R.id.badge);
           badge.clear();
           badge.setNumber(f,true);
            intrusions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.removeCallbacksAndMessages(null);
                    Intent ii= new Intent(MainActivity.this, OpenCvService.class);
                    stopService(ii);
                    serviceStarted=false;
                    Intent i = new Intent(MainActivity.this,IntrusionsActivity.class);
                    startActivity(i);
                }
            });

            imageProtection = (ImageView) findViewById(R.id.imgProtection);



        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                Intent ii= new Intent(MainActivity.this, OpenCvService.class);

                stopService(ii);
                serviceStarted=false;
                Intent i = new Intent(MainActivity.this,LiveDetectionActivity.class);
                startActivity(i);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });




        logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                Intent i = new Intent(MainActivity.this,Activities.class);
                startActivity(i);
            }
        });


        if(protectionDisabled){
            button.setText("Enable Protection");
            imageProtection.setImageResource(R.drawable.error2);


                txtProtection.setText("Border Not Secured");
                txtProtection.setTextColor(Color.rgb(216, 0, 39));


            button.setBackgroundColor(getResources().getColor(R.color.flatGreen));

        }else{
            button.setText("Disable Protection");

            button.setBackgroundColor(Color.rgb(216, 0, 39));

            txtProtection.setText("Border Secured");
            txtProtection.setTextColor(getResources().getColor(R.color.flatGreen));
            imageProtection.setImageResource(R.drawable.shieldon2);

        }


          Intent i= new Intent(this, OpenCvService.class);
       //this.startService(i);

   //     Intent i= new Intent(this, OpenCvService.class);


// potentially add data to the intent


      //  this.startService(i);
/*
        Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);

        String[] images = getResources().getStringArray(R.array.unsplash_images);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, new ArrayList<>(Arrays.asList(images)));
        bundle.putString(ImageGalleryActivity.KEY_TITLE, "Unsplash Images");
        intent.putExtras(bundle);

        startActivity(intent);
*/
    }
}
