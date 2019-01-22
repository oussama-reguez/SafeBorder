package com.example.mtlc.opencv;

/**
 * Created by oussama_2 on 12/1/2017.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import static android.app.Activity.RESULT_OK;


/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class ManagePhotoFragment extends Fragment {

Toolbar toolbar;
    RecyclerView recyclerView;
    ImageGalleryAdapter imageGalleryAdapter;
    ArrayList<String> photos = new ArrayList<>();
    ArrayList<String>idPhotos= new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v = inflater.inflate(R.layout.manage_photo, container, false);


     //    dialog.setMessage("uploading");
        initToolbar(v);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv);



        setUpRecyclerView();
        getPhotos("");







        return v;
    }

    Bitmap bitmap;
    List<Bitmap> images;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


/*

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){




                try {
                    Uri uri = data.getData();

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);



                    ImageUploadToServerFunction(bitmap,Login.connectedUser);



                    Log.i("","");

                } catch (Exception e) {

                }


        }

*/
    }

    private void setUpRecyclerView() {
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(getActivity())) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        imageGalleryAdapter = new ImageGalleryAdapter(getContext(), photos);
        recyclerView.setAdapter(imageGalleryAdapter);
        recyclerView.addOnItemTouchListener(new RecycleItemClickListener(getContext(),recyclerView, new RecycleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Intent i = new Intent(getActivity(), FullScreenImageGalleryActivity.class);
              //  i.putStringArrayListExtra(FullScreenImageGalleryActivity.KEY_IMAGES,photos);

                      i.putStringArrayListExtra(FullScreenImageGalleryActivity.KEY_IMAGES,photos);
                      i.putStringArrayListExtra("positions",idPhotos);
                i.putExtra("enable",true);
                i.putExtra(FullScreenImageGalleryActivity.KEY_POSITION,position);

                startActivity(i);



            }



            @Override
            public void onItemLongClick(View view, int position) {
                Log.i(" dfs","sdf");
            }
        }));




    }

          // imageGalleryAdapter.notifyDataSetChanged();



    private  void refreshRecyleView(List<String>photos){
this.photos.clear();
this.photos.addAll(photos);
imageGalleryAdapter.notifyDataSetChanged();
    }


    void initToolbar(View v){

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();



            }
        });
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("photos");
        }

    }







    @Override
    public void onResume() {
        super.onResume();

    }

    private void getPhotos(final String idUser) {

        File ff = new File(getActivity().getCacheDir(),"intrusions");
        List<String> photos = new ArrayList<>();
        for (File f : ff.listFiles()){
            String d = "file:"+f.toString();
            photos.add("file:"+f.toString());
        }


      //  photos.add("file:/storage/sdcard1/DCIM/Camera/IMG_20150101_010127.jpg");
        refreshRecyleView(photos);
        /*
        List<String> photos = new ArrayList<>();
        JSONArray skillsJson = jsonObject.getJSONArray("photos");
        for (  int i =0 ;i<skillsJson.length();i++){
            JSONObject skill = skillsJson.getJSONObject(i);

            idPhotos.add(skill.getString("id"));
            photos.add(AppController.IMAGE_SERVER_ADRESS+skill.getString("photo"));
        }

        refreshRecyleView(photos);
        */

    }



}
