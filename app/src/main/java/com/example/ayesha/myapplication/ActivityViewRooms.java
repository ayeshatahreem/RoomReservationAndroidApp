package com.example.ayesha.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by on 4/26/2018.
 */
public class ActivityViewRooms  extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterRoomsList adapterRoomsList;

    ArrayList<ClassRoom> roomsList;
    Firebase rootURL;
    int pictureLoadedCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        setContentView(R.layout.activity_viewrooms);
        InitializeVariables();
        LoadRoomsDataFromFirebase();
    }
    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        roomsList = new ArrayList<>();
        pictureLoadedCounter = 0;
    }
    public void LoadRoomsDataFromFirebase(){
        rootURL.child(FirebaseConstants.roomClassURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            String roomNumber = ds.getKey();
                            String roomPrice = ds.child(FirebaseConstants.roomPrice).getValue().toString();
                            int f1 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature1).getValue().toString());
                            int f2 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature2).getValue().toString());
                            int f3 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature3).getValue().toString());
                            int f4 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature4).getValue().toString());
                            int f5 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature5).getValue().toString());
                            int f6 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature6).getValue().toString());
                            int availability = Integer.valueOf(ds.child(FirebaseConstants.roomAvailability).getValue().toString());
                            if(availability == 1)
                                roomsList.add(new ClassRoom(roomNumber,roomPrice,availability,f1,f2,f3,f4,f5,f6));
                        }
                    }
                }
                LoadRoomImagesFromFirebase();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void LoadRoomImagesFromFirebase(){
        Firebase roomsURL = rootURL.child(FirebaseConstants.roomClassURL);
        roomsURL.child(roomsList.get(pictureLoadedCounter).getRoomNumber()).child(FirebaseConstants.roomPicturesURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            roomsList.get(pictureLoadedCounter).addRoomPicture(ds.getValue().toString());
                        }
                    }
                }
                if(++pictureLoadedCounter < roomsList.size()){
                    LoadRoomImagesFromFirebase();
                }
                else
                    DataLoaded();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
    public void DataLoaded(){
        recyclerView = (RecyclerView) findViewById(R.id.viewRoomsRecyclerView);
        adapterRoomsList = new AdapterRoomsList(ActivityViewRooms.this,roomsList);
        recyclerView.setAdapter(adapterRoomsList);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

    }
}