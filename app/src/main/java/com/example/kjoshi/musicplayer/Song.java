package com.example.kjoshi.musicplayer;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String Name;
    private String Artist;
    private Bitmap SImgResourceId;
    private long SAudioResourceId;

    public Song(String name,String artist,Bitmap sImgResourceId,long sAudioResourceId){
        Name=name;
        Artist=artist;
        SImgResourceId=sImgResourceId;
        SAudioResourceId=sAudioResourceId;
    }

    public Song(Parcel in)
    {
        Name=in.readString();
        Artist=in.readString();
        SImgResourceId=in.readParcelable(null);
        SAudioResourceId=in.readLong();
    }

    public int describeContents(){return 0;}

    public void writeToParcel(Parcel dest,int flag){
        dest.writeString(Name);
        dest.writeString(Artist);
        dest.writeParcelable(SImgResourceId,flag);
        dest.writeLong(SAudioResourceId);
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>()
    {
        public Song createFromParcel(Parcel in)
        {
            return new Song(in);
        }
        public Song[] newArray(int size)
        {
            return new Song[size];
        }
    };

    public String getName(){return Name;}
    public String getArtist(){return Artist;}
    public Bitmap getSImgResourceId(){return SImgResourceId;}
    public long getSAudioResourceId(){return SAudioResourceId;}
    public void setName(String name){Name=name;}
    public void setArtist(String artist){Artist=artist;}
    public void setSAudioResourceId(long sAudioResourceId){SAudioResourceId=sAudioResourceId;}
    public void setSImgResourceId(Bitmap sImgResourceId){SImgResourceId=sImgResourceId;}


}

