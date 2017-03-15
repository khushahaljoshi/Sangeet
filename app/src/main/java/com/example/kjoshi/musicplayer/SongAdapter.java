package com.example.kjoshi.musicplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<Song> {

    public SongAdapter(Activity Contex,ArrayList<Song> songs){
        super(Contex,0,songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Song currentSong=getItem(position);

        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView nameTextView=(TextView) listItemView.findViewById(R.id.Name);
        nameTextView.setText(currentSong.getName());

        TextView artistTextView=(TextView) listItemView.findViewById(R.id.Artist);
        artistTextView.setText(currentSong.getArtist());

        ImageView imgView=(ImageView)listItemView.findViewById(R.id.image);
        imgView.setImageBitmap(currentSong.getSImgResourceId());

        return listItemView;
    }
}
