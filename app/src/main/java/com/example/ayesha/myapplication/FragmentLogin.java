package com.example.ayesha.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by on 4/15/2018.
 */

@SuppressLint("ValidFragment")
public class FragmentLogin extends Fragment 
{
    View view;
    EditText usernameET, passwordET;
    Button loginButton;
    Firebase rootUrl;
    ClassUser currentUser;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        view = inflater.inflate(R.layout.fragment_login,null,false);
        InitializeVariables();
        SetListners();
        return view;
    }
    public void InitializeVariables()
	{
        rootUrl = new Firebase(FirebaseConstants.rootURL);
        usernameET = view.findViewById(R.id.fragmentLoginUsernameET);
        passwordET = view.findViewById(R.id.fragmentLoginPasswordET);
        loginButton = view.findViewById(R.id.fragmentLoginButton);
        currentUser = null;
    }
    public void SetListners()
	{
        loginButton.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View v) 
			{
                LoginConformation();
            }
        });
    }
    public void LoginConformation()
	{
        final String username = usernameET.getText().toString();
        final String password = passwordET.getText().toString();
        if(username.equals("") || password.equals(""))
            Toast.makeText(getContext(), "Incomplete Info.", Toast.LENGTH_SHORT).show();
        else
		{
            rootUrl.child(FirebaseConstants.userClassURL).child(username).addListenerForSingleValueEvent(new ValueEventListener() 
			{
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) 
				{
                    if(!dataSnapshot.exists())
                        Toast.makeText(getContext(),"Incorrect info.",Toast.LENGTH_SHORT).show();
                    else 
					{
                            String rUsername = dataSnapshot.child(FirebaseConstants.userUsernameURL).getValue().toString();
                            String rPassword = dataSnapshot.child(FirebaseConstants.userPasswordURL).getValue().toString();
                            String rPictureURL = dataSnapshot.child(FirebaseConstants.userPictureURL).getValue().toString();
                            if (rUsername.equals(username) && rPassword.equals(password)) 
                                    Toast.makeText(getContext(), "Welcome.", Toast.LENGTH_SHORT).show();
							else
									Toast.makeText(getContext(), "Incorrect info.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) 
				{

                }
            });
        }
    }
}
