package com.example.ayesha.myapplication;

/**
 * Created by  on 4/3/2018.
 */
 
public class ClassUser 
{
    String username,password,imageUrl; // 0 = user , 1 = admin
    int type;

    public ClassUser()
	{
        username = password = imageUrl = "";
        type = 0;
    }

    public int getType() 
	{
        return type;
    }

    public void setType(int type) 
	{
        this.type = type;
    }

    public ClassUser (String username, String password, String imageUrl, int type)
	{
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getImageUrl() 
	{
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) 
	{
        this.imageUrl = imageUrl;
    }
    public String getPassword() 
	{
        return password;
    }
    public void setPassword(String password) 
	{
        this.password = password;
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
