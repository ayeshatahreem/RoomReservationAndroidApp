package com.example.ayesha.myapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * Created by on 4/15/2018.
 */
@SuppressLint("ValidFragment")
public class FragmentSignup extends Fragment 
{
    View view;
    ImageView profilePicIV;
    EditText usernameET, passwordET;
    Button signupButton;
    Firebase rootUrl;
    StorageReference mStorage;
    Bitmap profilePictureBitmap;
    Uri uploadProfilePictureURI, downloadProfilePictureURI;
    final static int PERMISSION_ALL = 1;
    public static final int GalleryRequestCodeID = 1;
  
    public Boolean CheckPermissions()
	{
        int writeStorage = getContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return writeStorage == PackageManager.PERMISSION_GRANTED;
    }
    public void GetPermissions()
	{
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
    }
    public void AddUser(ClassUser newUser)
	{
        rootUrl.child(FirebaseConstants.userClassURL).child(newUser.getUsername()).setValue(newUser);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        view = inflater.inflate(R.layout.fragment_signup,null,false);
        InitializeVariables();
        SetListners();
        return view;
    }
    public void InitializeVariables()
	{
        rootUrl = new Firebase(FirebaseConstants.rootURL);
        mStorage = FirebaseStorage.getInstance().getReference();
        profilePicIV = view.findViewById(R.id.fragmentSignupProfilePicIV);
        usernameET = view.findViewById(R.id.fragmentSignupUsernameET);
        passwordET = view.findViewById(R.id.fragmentSignupPasswordET);
        signupButton = view.findViewById(R.id.fragmentSignupButton);
        profilePictureBitmap = null;
        uploadProfilePictureURI = downloadProfilePictureURI = null;
    }
    public void SetListners()
	{
        signupButton.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View v) 
			{
                UploadImageOnFirebase();
            }
        });
        profilePicIV.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View v) 
			{
                if(!CheckPermissions())
                    GetPermissions();
                else
                    GetImageFromGallery();
            }
        });
    }
    public void SignupUser()
	{
        final String userName  = usernameET.getText().toString();
        final String password = passwordET.getText().toString();
        if(userName.equals("") || password.equals(""))
		{
            Toast.makeText(getContext(),"Incomplete info",Toast.LENGTH_SHORT).show();
            return;
        }
        Query usernameQuery = rootUrl.child(FirebaseConstants.userClassURL).child(userName);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() 
		{
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) 
			{
                if (dataSnapshot.exists())
				{
                    Toast.makeText(getContext(), "Have a unique user name", Toast.LENGTH_SHORT).show();
                }
                else
                    AddUser(new ClassUser(userName,password,downloadProfilePictureURI.toString(),1));
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) 
			{

            }
        });
    }

    public void GetImageFromGallery()
	{
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GalleryRequestCodeID);
    }

    public void UploadImageOnFirebase()
	{
        if(uploadProfilePictureURI == null) 
		{
            SignupUser();
        }
        else 
		{
            StorageReference filePath = mStorage.child(FirebaseConstants.profilePicturePath).child(uploadProfilePictureURI.getLastPathSegment());
            filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() 
			{
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) 
				{
                    downloadProfilePictureURI = taskSnapshot.getDownloadUrl();
                    SignupUser();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) 
	{
        switch (requestCode) 
		{
            case PERMISSION_ALL: 
			{
                if(CheckPermissions())
				{
                    GetImageFromGallery();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryRequestCodeID && resultCode == Activity.RESULT_OK)
		{
            Uri uri = data.getData();
            try 
			{
                uploadProfilePictureURI = data.getData();
                profilePictureBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                profilePicIV.setImageBitmap(profilePictureBitmap);
                profilePicIV.setPadding(0,0,0,0);
            } 
			catch (IOException e) 
			{
                e.printStackTrace();
            }
        }
    }
}
