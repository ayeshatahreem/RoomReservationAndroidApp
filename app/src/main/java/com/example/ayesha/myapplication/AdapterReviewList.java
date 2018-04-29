package com.example.ayesha.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by  on 4/27/2018.
 */
public class AdapterReviewList extends RecyclerView.Adapter<AdapterReviewList.MyViewHolder> 
{
    public ArrayList<ClassReview> reviewList;
    public Context context;
    int userType; // 0 = user , 1 = admin
    public class MyViewHolder extends RecyclerView.ViewHolder 
	{
        TextView usernameTV, reviewTV;
        RatingBar ratingBar;
        CircleImageView profilePictute;
        ImageButton deleteReviewButton;
        public MyViewHolder(View view) 
		{
            super(view);
            profilePictute = view.findViewById(R.id.adapterReviewListProfilePic);
            usernameTV = view.findViewById(R.id.adapterReviewListUsername);
            reviewTV = view.findViewById(R.id.adapterReviewListReview);
            ratingBar  = view.findViewById(R.id.adapterReviewListRating);
            deleteReviewButton = view.findViewById(R.id.adapterReviewListDeleteButton);
        }
    }
    public AdapterReviewList(Context context,ArrayList<ClassReview> reviewList, int userType) 
	{
        this.context = context;
        this.reviewList = reviewList;
        this.userType = userType;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) 
	{
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reviewlist, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) 
	{
        final Firebase rootURL = new Firebase(FirebaseConstants.rootURL);
        final ClassReview singleReview = reviewList.get(position);
        holder.usernameTV.setText(singleReview.getUsername());
        holder.reviewTV.setText(singleReview.getReview());
        holder.ratingBar.setRating(singleReview.getRating());
        if(userType == 1) 
		{
            holder.deleteReviewButton.setVisibility(View.VISIBLE);
            holder.deleteReviewButton.setOnClickListener(new View.OnClickListener() 
			{
                @Override
                public void onClick(View v) 
				{
                    rootURL.child(FirebaseConstants.reviewClassURL).child(String.valueOf(singleReview.getRoomNumber())).
                            child(singleReview.getUsername()).removeValue();
                    reviewList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        rootURL.child(FirebaseConstants.userClassURL).child(singleReview.getUsername()).child(FirebaseConstants.userPictureURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String picPath = dataSnapshot.getValue().toString();
                    if(!picPath.equals(""))
                        Picasso.with(context).load(picPath).resize(HelperFunctions.DpToPx(80, context), HelperFunctions.DpToPx(80, context)) // resizes the image to these dimensions (in pixel)
                            .centerCrop().into(holder.profilePictute);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}