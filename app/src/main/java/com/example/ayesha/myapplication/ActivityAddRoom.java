package com.example.ayesha.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by on 4/24/2018.
 */
public class ActivityAddRoom extends AppCompatActivity {

    LinearLayout addPicturesMainLL;
    ImageView addRoomPicIB;

    final static int PERMISSION_ALL = 1;
    public static final int GalleryRequestCodeID = 1;

    Button addRoomButton;
    CheckBox feature1,feature2,feature3,feature4,feature5,feature6;
    EditText addRoomNumberET,addRoomPriceET;

    ArrayList<Uri> uploadProfilePictureURIAL, downloadProfilePictureURIAL;
    int imageCounter;

    Firebase rootURL;
    StorageReference mStorage;

    ClassRoom currentRoom;
    RelativeLayout loadingRL;

    boolean isUpdate = false;
    //Permissions
    public Boolean CheckPermissions(){
        int writeStorage = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return writeStorage == PackageManager.PERMISSION_GRANTED;
    }
    public void GetPermissions(){
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(ActivityAddRoom.this, PERMISSIONS, PERMISSION_ALL);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        setContentView(R.layout.activity_addroom);
        InitializeVariables();
        LoadPreviousData();
        SetListners();

    }
    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        mStorage = FirebaseStorage.getInstance().getReference();

        addPicturesMainLL = (LinearLayout) findViewById(R.id.addRoomPictureMainLL);
        addRoomPicIB = (ImageView) findViewById(R.id.addRoomUploadPic);
        addRoomButton = (Button) findViewById(R.id.addRoomAddButton);
        addRoomNumberET = (EditText) findViewById(R.id.addRoomRoomNumber);
        addRoomPriceET = (EditText) findViewById(R.id.addRoomPrice);

        feature1 = (CheckBox) findViewById(R.id.addRoomFeature1);
        feature2 = (CheckBox) findViewById(R.id.addRoomFeature2);
        feature3 = (CheckBox) findViewById(R.id.addRoomFeature3);
        feature4 = (CheckBox) findViewById(R.id.addRoomFeature4);
        feature5 = (CheckBox) findViewById(R.id.addRoomFeature5);
        feature6 = (CheckBox) findViewById(R.id.addRoomFeature6);

        uploadProfilePictureURIAL = new ArrayList<>();
        downloadProfilePictureURIAL = new ArrayList<>();
        imageCounter = 0;

        loadingRL = (RelativeLayout) findViewById(R.id.addRoomLoadingRL);
    }
    public void LoadPreviousData(){
        ClassRoom tempRoom = (ClassRoom) getIntent().getSerializableExtra("CurrentRoom");
        if(tempRoom == null)
            return;

        isUpdate = true;
        addRoomNumberET.setText(tempRoom.getRoomNumber());
        addRoomNumberET.setFocusable(false);
        addRoomNumberET.setFocusableInTouchMode(false);
        addRoomNumberET.setClickable(false);

        addRoomPriceET.setText(tempRoom.getRoomPrice());

        if(tempRoom.getFeature1() == 1)
            feature1.setChecked(true);
        if(tempRoom.getFeature2() == 1)
            feature2.setChecked(true);
        if(tempRoom.getFeature3() == 1)
            feature3.setChecked(true);
        if(tempRoom.getFeature4() == 1)
            feature4.setChecked(true);
        if(tempRoom.getFeature5() == 1)
            feature5.setChecked(true);
        if(tempRoom.getFeature6() == 1)
            feature6.setChecked(true);

        for(int i = 0 ; i < tempRoom.getRoomPictures().size(); i++)
            AddImageFromFirebase(tempRoom.getRoomPictures().get(i));

    }
    public void AddImageFromFirebase(final String imageURL){
        int tempPadding = 10;

        ImageView tempImageView = new ImageView(ActivityAddRoom.this);
        tempImageView.setLayoutParams(new RelativeLayout.LayoutParams(HelperFunctions.DpToPx(100,ActivityAddRoom.this),
                HelperFunctions.DpToPx(100,ActivityAddRoom.this)));
        tempImageView.setPadding(tempPadding,tempPadding,tempPadding,tempPadding);
        Picasso.with(ActivityAddRoom.this).load(imageURL).resize(HelperFunctions.DpToPx(100, ActivityAddRoom.this), HelperFunctions.DpToPx(100, ActivityAddRoom.this)) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(tempImageView);


        RelativeLayout tempRelativeLayout = new RelativeLayout(ActivityAddRoom.this);
        tempRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tempTextView = new TextView(ActivityAddRoom.this);
        tempTextView.setLayoutParams(new RelativeLayout.LayoutParams(0,
                0));
        tempTextView.setText(imageURL);
        tempRelativeLayout.addView(tempTextView);
        tempRelativeLayout.addView(tempImageView);
        tempRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ActivityAddRoom.this)
                        .setTitle("Remove Room")
                        .setMessage("Do you want to remove this room?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String tempImageURL = ((TextView) ((RelativeLayout)v).getChildAt(0)).getText().toString();
                                for (int i = 0; i < downloadProfilePictureURIAL.size(); i++){
                                    if(downloadProfilePictureURIAL.get(i).toString().equals(tempImageURL)){
                                        downloadProfilePictureURIAL.remove(i);
                                        break;
                                    }
                                }
                                ((ViewManager)v.getParent()).removeView(v);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        addPicturesMainLL.addView(tempRelativeLayout);
        downloadProfilePictureURIAL.add(Uri.parse(imageURL));
    }
    public void SetListners(){
        addRoomPicIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckPermissions())
                    GetPermissions();
                else
                    GetImageFromGallery();
            }
        });

        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoom();
            }
        });
    }
    public void AddRoom(){
        String roomNumber ="", roomPrice = "";
        roomNumber = addRoomNumberET.getText().toString();
        roomPrice = addRoomPriceET.getText().toString();
        if(roomNumber.isEmpty() || roomPrice.isEmpty() || (uploadProfilePictureURIAL.size() ==0 && downloadProfilePictureURIAL.size() == 0) ){
            Toast.makeText(getApplication(),"Please fill in the complete form.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isUpdate) {
            rootURL.child(FirebaseConstants.roomClassURL).child(roomNumber).removeValue();
            RoomApproved();
        }
        else {
            rootURL.child(FirebaseConstants.roomClassURL).child(roomNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplication(), "Room is already present!", Toast.LENGTH_SHORT).show();
                    } else {
                        RoomApproved();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
    public void RoomApproved(){
        String roomNumber ="", roomPrice = "";
        int f1 = 0 , f2 = 0, f3 = 0, f4 = 0,f5 = 0, f6 = 0;
        roomNumber = addRoomNumberET.getText().toString();
        roomPrice = addRoomPriceET.getText().toString();
        if(feature1.isChecked())
            f1 = 1;
        if(feature2.isChecked())
            f2 = 1;
        if(feature3.isChecked())
            f3 = 1;
        if(feature4.isChecked())
            f4 = 1;
        if(feature5.isChecked())
            f5 = 1;
        if(feature6.isChecked())
            f6 = 1;
        loadingRL.setVisibility(View.VISIBLE);
        currentRoom = new ClassRoom(roomNumber,roomPrice,1,f1,f2,f3,f4,f5,f6);

        if(isUpdate && uploadProfilePictureURIAL.size() == 0)
            UploadRoomOnFirebase();
        for(int i = 0 ; i < uploadProfilePictureURIAL.size(); i++)
            UploadImageOnFirebase(uploadProfilePictureURIAL.get(i));
    }
    public void UploadRoomOnFirebase(){
        Firebase roomURL = rootURL.child(FirebaseConstants.roomClassURL).child(currentRoom.getRoomNumber());
        roomURL.child(FirebaseConstants.roomPrice).setValue(currentRoom.getRoomPrice());
        roomURL.child(FirebaseConstants.roomFeature1).setValue(currentRoom.getFeature1());
        roomURL.child(FirebaseConstants.roomFeature2).setValue(currentRoom.getFeature2());
        roomURL.child(FirebaseConstants.roomFeature3).setValue(currentRoom.getFeature3());
        roomURL.child(FirebaseConstants.roomFeature4).setValue(currentRoom.getFeature4());
        roomURL.child(FirebaseConstants.roomFeature5).setValue(currentRoom.getFeature5());
        roomURL.child(FirebaseConstants.roomFeature6).setValue(currentRoom.getFeature6());
        roomURL.child(FirebaseConstants.roomAvailability).setValue(currentRoom.getAvailability());
        Firebase roomPictureURL = roomURL.child(FirebaseConstants.roomPicturesURL);
        for(int i = 0 ; i < downloadProfilePictureURIAL.size() ; i++)
            roomPictureURL.push().setValue(downloadProfilePictureURIAL.get(i).toString());
        loadingRL.setVisibility(View.GONE);
    }
    public void UploadRoomPicture(Bitmap imageBitmap, String imageURL){
        int tempPadding = 10;
        Bitmap recizedBitmap = HelperFunctions.RecizeBitmap(imageBitmap,imageBitmap.getWidth()/5,imageBitmap.getHeight()/5);
        ImageView tempImageView = new ImageView(ActivityAddRoom.this);
        tempImageView.setLayoutParams(new RelativeLayout.LayoutParams(HelperFunctions.DpToPx(100,ActivityAddRoom.this),
                HelperFunctions.DpToPx(100,ActivityAddRoom.this)));
        tempImageView.setImageBitmap(recizedBitmap);
        tempImageView.setPadding(tempPadding,tempPadding,tempPadding,tempPadding);
        tempImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayout tempRelativeLayout = new RelativeLayout(ActivityAddRoom.this);
        tempRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tempTextView = new TextView(ActivityAddRoom.this);
        tempTextView.setLayoutParams(new RelativeLayout.LayoutParams(0,
                0));
        tempTextView.setText(imageURL);
        tempRelativeLayout.addView(tempTextView);
        tempRelativeLayout.addView(tempImageView);
        tempRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ActivityAddRoom.this)
                        .setTitle("Remove Room")
                        .setMessage("Do you want to remove this room?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String tempImageURL = ((TextView) ((RelativeLayout)v).getChildAt(0)).getText().toString();
                                for (int i = 0; i < uploadProfilePictureURIAL.size(); i++){
                                    if(uploadProfilePictureURIAL.toString().equals(tempImageURL)){
                                        uploadProfilePictureURIAL.remove(i);
                                        break;
                                    }
                                }
                                ((ViewManager)v.getParent()).removeView(v);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        addPicturesMainLL.addView(tempRelativeLayout);
    }
    public void GetImageFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GalleryRequestCodeID);
    }
    public void UploadImageOnFirebase(Uri uploadProfilePictureURI){
            StorageReference filePath = mStorage.child(FirebaseConstants.roomPicturePath).child(uploadProfilePictureURI.getLastPathSegment());
            filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadProfilePictureURIAL.add(taskSnapshot.getDownloadUrl());
                    imageCounter++;

                    if(imageCounter == uploadProfilePictureURIAL.size()) {
                        UploadRoomOnFirebase();
                    }
                }
            });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if(CheckPermissions()){
                    GetImageFromGallery();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryRequestCodeID && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {
                uploadProfilePictureURIAL.add(data.getData());
                UploadRoomPicture(MediaStore.Images.Media.getBitmap(ActivityAddRoom.this.getContentResolver(),uri), uri.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
