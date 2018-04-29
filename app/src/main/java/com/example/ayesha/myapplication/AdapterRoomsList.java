package com.example.ayesha.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by on 4/26/2018.
 */
public class AdapterRoomsList  extends RecyclerView.Adapter<AdapterRoomsList.MyViewHolder> 
{

    public ArrayList<ClassRoom> roomsList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder 
	{
        TextView roomNumberTV, roomPriceTV;
        ImageView roomImage;
        Button viewRoom;
        public MyViewHolder(View view) 
		{
            super(view);
            roomNumberTV = view.findViewById(R.id.adapterRoomListRoomNumberTV);
            roomPriceTV = view.findViewById(R.id.adapterRoomListRoomPriceTV);
            roomImage = view.findViewById(R.id.adapterRoomListRoomImage);
            viewRoom = view.findViewById(R.id.adapterRoomListViewRoom);
        }

    }
    public AdapterRoomsList(Context context,ArrayList<ClassRoom> roomsList) 
	{
        this.context = context;
        this.roomsList = roomsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) 
	{
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_roomslist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) 
	{
        ClassRoom singleRoom = roomsList.get(position);
        holder.roomNumberTV.setText("Room # " + singleRoom.getRoomNumber());
        holder.roomPriceTV.setText(singleRoom.getRoomPrice() + "$");
        Picasso.with(context).load(singleRoom.getRoomPictures().get(0)).fit().into(holder.roomImage);
        holder.viewRoom.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View v) 
			{
                Intent intent = new Intent(context,ActivityAddRoom.class);
                intent.putExtra("CurrentRoom", roomsList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() 
	{
        return roomsList.size();
    }
}