package com.example.kjoshi.musicplayer;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K. Joshi on 11-Mar-17.
 */
public class SplashScreen extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Song>> {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private static final int SONG_LODER_ID = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screeen);
        checkPermission();

    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(SONG_LODER_ID, null, this);

                } else {
                    checkPermission();
                }
                return;
            }
        }
    }

    @Override
    public Loader<ArrayList<Song>> onCreateLoader(int i, Bundle bundle) {
        return new SongsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Song>> loader, ArrayList<Song> songs) {
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.songs = songs;
        startActivity(intent);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Song>> loader) {
        // Loader reset, so we can clear out our existing data.
    }


}





