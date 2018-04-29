package com.example.ayesha.myapplication;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by on 4/16/2018.
 */
public class FirebaseHandler 
{
    public boolean returnValue;
    Firebase rootUrl;
    public FirebaseHandler()
	{
        rootUrl = new Firebase(FirebaseConstants.rootURL);
        returnValue = false;
    }
    public boolean IsUsernameAvailable (String userName)
	{
        Query usernameQuery = rootUrl.child(FirebaseConstants.userClassURL).child(userName);
        returnValue = true;
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() 
		{
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) 
			{
                if (dataSnapshot != null)
                    returnValue = false;
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) 
			{

            }
        });
        return returnValue;
    }
    public void AddUser(ClassUser newUser)
	{
        rootUrl.child(FirebaseConstants.userClassURL).child(newUser.getUsername()).setValue(newUser);
    }
}
