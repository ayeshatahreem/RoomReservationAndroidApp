package com.example.ayesha.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by on 4/25/2018.
 */
public class ClassRoom implements Serializable 
{
    String roomNumber, roomPrice;
    int feature1, feature2, feature3, feature4, feature5, feature6, availability; // 0 = N, 1 = Y
    ArrayList<String> roomPictures;
    public ClassRoom()
	{
        roomNumber = roomPrice = "";
        feature1 = feature2 = feature3 = feature4 = feature5 = feature6 = availability = 0;
        roomPictures = new ArrayList<>();
    }
    public ClassRoom(String roomNumber, String roomPrice, int availability,int f1, int f2, int f3, int f4, int f5, int f6)
	{
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.feature1 = f1;
        this.feature2 = f2;
        this.feature3 = f3;
        this.feature4 = f4;
        this.feature5 = f5;
        this.feature6 = f6;
        this.availability = availability;
        roomPictures = new ArrayList<>();
    }
    public void addRoomPicture(String pictureURL)
	{
        roomPictures.add(pictureURL);
    }
    public ArrayList<String> getRoomPictures()
	{
        return roomPictures;
    }
    public int getAvailability() 
	{
        return availability;
    }
    public void setAvailability(int availability) 
	{
        this.availability = availability;
    }
    public int getFeature1() 
	{
        return feature1;
    }
    public void setFeature1(int feature1) 
	{
        this.feature1 = feature1;
    }
    public int getFeature2() 
	{
        return feature2;
    }
    public void setFeature2(int feature2) 
	{
        this.feature2 = feature2;
    }
    public int getFeature3() 
	{
        return feature3;
    }
    public void setFeature3(int feature3) 
	{
        this.feature3 = feature3;
    }
    public int getFeature4() 
	{
        return feature4;
    }
    public void setFeature4(int feature4) 
	{
        this.feature4 = feature4;
    }
    public int getFeature5() 
	{
        return feature5;
    }
    public void setFeature5(int feature5) 
	{
        this.feature5 = feature5;
    }
    public int getFeature6() 
	{
        return feature6;
    }
    public void setFeature6(int feature6) 
	{
        this.feature6 = feature6;
    }
    public String getRoomNumber() 
	{
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) 
	{
        this.roomNumber = roomNumber;
    }
    public String getRoomPrice() 
	{
        return roomPrice;
    }
    public void setRoomPrice(String roomPrice) 
	{
        this.roomPrice = roomPrice;
    }

}
