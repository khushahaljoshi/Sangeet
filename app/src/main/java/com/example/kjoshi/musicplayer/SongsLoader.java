package com.example.kjoshi.musicplayer;


import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K. Joshi on 11-Mar-17.
 */
public class SongsLoader extends AsyncTaskLoader<ArrayList<Song>> {
    ArrayList<Song> songs;
    Bitmap bitmap = null;

    public SongsLoader(Context context){
        super( context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public ArrayList<Song> loadInBackground(){
        songs = new ArrayList<>();
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);
        if (cursor == null) {
            Log.d("cursor is null", "");
            Toast.makeText(getContext(), "no songs", Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Log.d("nomovefirst", "");
        } else {
            try {
                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                do {
                    long id = cursor.getLong(idColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    long albId = cursor.getLong(albumColumn);

                    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albId);


                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), albumArtUri);
                    } catch (Exception exception) {
                        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);
                    }
                    songs.add(new Song(title,artist,bitmap,id));

                } while (cursor.moveToNext());
            } catch (Exception e) {
                Log.d("AlbumColumn", "AlbumColumn");
            }
            cursor.close();
        }

        return songs;
    }
}
