package com.example.ayesha.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by on 4/27/2018.
 */
public class ActivitySingleRoom extends AppCompatActivity {


    ClassRoom currentRoom;
    ArrayList<ClassReview> reviewList;
    RecyclerView reviewRecyclerView;
    AdapterReviewList adapterReviewList;
    Firebase rootURL;
    LinearLayout roomPicturesLL;

    TextView roomNumberTV, roomPriceTV;
    ImageView f1, f2, f3, f4, f5, f6;

    float totalRating;
    RatingBar mainRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        setContentView(R.layout.activity_singleroom);
        LoadDataFromIntent();
        InitializeVariables();
        new LoadReviewsFromFirebase().execute();
        DisplayData();
    }
    public void LoadDataFromIntent(){
        currentRoom = (ClassRoom) getIntent().getSerializableExtra("CurrentRoom");
    }
    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        reviewList = new ArrayList<>();
        roomPicturesLL = (LinearLayout) findViewById(R.id.singleRoomPicturesLL);
        roomNumberTV = (TextView) findViewById(R.id.singleRoomRoomNumberTV);
        roomPriceTV = (TextView) findViewById(R.id.singleRoomRoomPriceTV);

        f1 = (ImageView) findViewById(R.id.singleRoomFeature1);
        f2 = (ImageView) findViewById(R.id.singleRoomFeature2);
        f3 = (ImageView) findViewById(R.id.singleRoomFeature3);
        f4 = (ImageView) findViewById(R.id.singleRoomFeature4);
        f5 = (ImageView) findViewById(R.id.singleRoomFeature5);
        f6 = (ImageView) findViewById(R.id.singleRoomFeature6);

        totalRating = 0;
        mainRating = (RatingBar) findViewById(R.id.singleRoomMainRating);

    }
    public void DataLoaded(){
        reviewRecyclerView = (RecyclerView) findViewById(R.id.singleRoomReviewRecyclerView);
        adapterReviewList = new AdapterReviewList(ActivitySingleRoom.this,reviewList,1);
        reviewRecyclerView.setAdapter(adapterReviewList);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        reviewRecyclerView.setLayoutManager(gridLayoutManager);
        mainRating.setRating(totalRating/reviewList.size());

    }
    public void LoadSingleImage(final String imageURL){
        int tempPadding = 10;
        ImageView tempImageView = new ImageView(ActivitySingleRoom.this);
        tempImageView.setLayoutParams(new RelativeLayout.LayoutParams(HelperFunctions.DpToPx(100,ActivitySingleRoom.this),
                HelperFunctions.DpToPx(100,ActivitySingleRoom.this)));
        tempImageView.setPadding(tempPadding,tempPadding,tempPadding,tempPadding);
        Picasso.with(ActivitySingleRoom.this).load(imageURL).resize(HelperFunctions.DpToPx(100, ActivitySingleRoom.this),
                HelperFunctions.DpToPx(100, ActivitySingleRoom.this)) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(tempImageView);
         tempImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(ActivitySingleRoom.this,imageURL, Toast.LENGTH_SHORT).show();
           }
       });
        roomPicturesLL.addView(tempImageView);

    }
    public void DisplayData(){
        roomNumberTV.setText("Room #" + currentRoom.getRoomNumber());
        roomPriceTV.setText(currentRoom.getRoomPrice() + "$");
        if(currentRoom.getFeature1() == 1)
            f1.setVisibility(View.VISIBLE);
        if(currentRoom.getFeature1() == 2)
            f2.setVisibility(View.VISIBLE);
        if(currentRoom.getFeature1() == 3)
            f3.setVisibility(View.VISIBLE);
        if(currentRoom.getFeature1() == 4)
            f4.setVisibility(View.VISIBLE);
        if(currentRoom.getFeature1() == 5)
            f5.setVisibility(View.VISIBLE);
        if(currentRoom.getFeature1() == 6)
            f6.setVisibility(View.VISIBLE);
        ArrayList<String> pictureURLList = currentRoom.getRoomPictures();
        for (int i = 0 ; i < pictureURLList.size(); i++){
            LoadSingleImage(pictureURLList.get(i));
        }
    }
    private class LoadReviewsFromFirebase extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            rootURL.child(FirebaseConstants.reviewClassURL).child(String.valueOf(currentRoom.getRoomNumber())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds != null) {
                                String roomNumber = dataSnapshot.getKey();
                                String username = ds.getKey();
                                int rating = Integer.valueOf(ds.child(FirebaseConstants.reviewRating).getValue().toString());
                                String review = ds.child(FirebaseConstants.reviewReview).getValue().toString();
                                reviewList.add(new ClassReview(roomNumber,username,review,rating));
                                totalRating += rating;
                            }
                        }
                    }
                    DataLoaded();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }
}
