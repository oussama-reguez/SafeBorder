package com.example.mtlc.opencv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by oussama_2 on 11/21/2017.
 */

public class ActivityRecyclerViewAdapter extends RecyclerView.Adapter<ActivityRecyclerViewAdapter.CustomViewHolder> {
    private JSONArray feedItemList;
    private Context mContext;

    public ActivityRecyclerViewAdapter(Context context, JSONArray feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        JSONObject feedItem=null;
        try {
             feedItem = feedItemList.getJSONObject(i);
            customViewHolder.request.setText(feedItem.getString("name"));
            customViewHolder.remaining.setText(feedItem.getString("date"));
            Context context = customViewHolder.imageView.getContext();
            int id = context.getResources().getIdentifier(feedItem.getString("photo"), "drawable", context.getPackageName());
            customViewHolder.imageView.setImageResource(id);
             //   Date d = DateUtility.TimeStampFormatter.parse(feedItem.getString("date"));

         //   customViewHolder.remaining.setText(DateUtility.printDifference(d,new Date()));
           // customViewHolder.description.setText(feedItem.getString("description"));
       //    Glide.with(mContext).load(AppController.IMAGE_SERVER_ADRESS+feedItem.getString("photo")).transform(new AppController.CircleTransform(mContext)).into(customViewHolder.image);
           /*
            final String id=feedItem.getString("idOffer");
            customViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRequest(LoginActivity.connectedUser,id);
                }
            });
*/

        } catch (Exception e) {
            e.printStackTrace();
        }


        //Render image using Picasso library
       /*
        if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
            Picasso.with(mContext).load(feedItem.getThumbnail())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }

*/
        //Setting text view title
       // customViewHolder.textView.setText(Html.fromHtml(feedItem.getTitle()));
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.length() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
      //  protected ImageView imageView;
        //protected TextView textView;

           ImageView imageView;

        TextView request;
        TextView remaining;









        public CustomViewHolder(View view) {
            super(view);
            request = (TextView) view.findViewById(R.id.txtOffer);
            remaining = (TextView) view.findViewById(R.id.textView8);
            imageView = (ImageView) view.findViewById(R.id.photo);




        }
    }



}