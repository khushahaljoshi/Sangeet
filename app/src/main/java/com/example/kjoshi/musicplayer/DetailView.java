package com.example.kjoshi.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class DetailView extends MainActivity implements SeekBar.OnSeekBarChangeListener {
    SeekBar sSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        name = (TextView) findViewById(R.id._Name);
        artist = (TextView) findViewById(R.id._Artist);
        imgView = (ImageView) findViewById(R.id._image);
        sSeekBar = (SeekBar) findViewById(R.id.seekbar);

        sSeekBar.setOnSeekBarChangeListener(this);

        updateUI(songs.get(currentPosition));

        pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    mMediaPlayer.pause();
                    pause_play.setImageResource(R.drawable.ic_play_circle_filled_black_48dp);
                    flag = 0;
                } else {
                    pause_play.setImageResource(R.drawable.ic_pause_circle_filled_black_48dp);
                    flag = 1;
                    if (mMediaPlayer == null) {
                        Log.d("new", "new");
                        playMusic(songs.get(currentPosition));
                    } else {
                        Log.d("old", "old");
                        mMediaPlayer.start();
                    }
                }
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null) {
                    dur = mMediaPlayer.getCurrentPosition();
                    if (dur / 1000 < 3) {
                        previous();
                    } else {
                        playMusic(songs.get(currentPosition));
                    }
                } else {
                    previous();
                }
                updateUI(songs.get(currentPosition));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                updateUI(songs.get(currentPosition));
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rep == 0) {
                    rep = rep + 1;
                    repeat.setImageResource(R.drawable.ic_repeat_black_36dp);
                } else if (rep == 1) {
                    rep = rep + 1;
                    repeat.setImageResource(R.drawable.ic_repeat_one_black_36dp);
                } else {
                    repeat.setImageResource(R.drawable.ic_repeat_white_36dp);
                    rep = 0;
                }
                Log.d("rep", "" + rep);
            }
        });


        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuf == 0) {
                    shuffle.setImageResource(R.drawable.ic_shuffle_black_36dp);
                    shuf = 1;
                    shuffleAsyncTask = new ShuffleAsyncTask();
                    shuffleAsyncTask.execute();
                } else {
                    shuffle.setImageResource(R.drawable.ic_shuffle_white_36dp);
                    shuf = 0;
                    shuffleAsyncTask.cancel(true);
                    shuffleList.clear();
                }
            }
        });

        DetailView.this.runOnUiThread(new Runnable() {
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                if (mMediaPlayer != null) {
                    sSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                }
                mHandler.postDelayed(this, 1000);
            }
        });

    }

    public void updateUI(Song current) {
        name.setText(current.getName());
        artist.setText(current.getArtist());
        imgView.setImageBitmap(current.getSImgResourceId());
        sSeekBar.setProgress(0);
        sSeekBar.setMax(mMediaPlayer.getDuration());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMediaPlayer != null && fromUser) {
            mMediaPlayer.seekTo(progress);
        }
    }
}
