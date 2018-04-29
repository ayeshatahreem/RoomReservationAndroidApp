package com.example.ayesha.myapplication;

/**
 * Created by on 4/27/2018.
 */
public class ClassReview 
{
    String roomNumber, username, review;
    int rating;
    public ClassReview()
	{
        roomNumber = username = review = "";
        rating = 0;
    }
    public  ClassReview (String roomNumber,String username, String review, int rating)
	{
        this.roomNumber = roomNumber;
        this.username = username;
        this.review = review;
        this.rating = rating;
    }
    public int getRating() 
	{
        return rating;
    }
    public void setRating(int rating) 
	{
        this.rating = rating;
    }
    public String getReview() 
	{
        return review;
    }
    public void setReview(String review) 
	{
        this.review = review;
    }
    public String getRoomNumber() 
	{
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) 
	{
        this.roomNumber = roomNumber;
    }

    public String getUsername() 
	{
        return username;
    }

    public void setUsername(String username)
	{
        this.username = username;
    }
}
