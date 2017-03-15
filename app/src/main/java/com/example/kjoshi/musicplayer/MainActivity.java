package com.example.kjoshi.musicplayer;


import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    static ArrayList<Song> songs;
    SongAdapter songAdapter;
    MediaPlayer mMediaPlayer = null;
    ImageView imgView, background;
    Bitmap bitmap = null;
    ImageButton pause_play, pre, next, repeat, shuffle;
    TextView name, artist;
    int flag = 0, currentPosition = 0, last = 0, rep = 0, dur = 0;
    SeekBar sSeekBar;
    public Handler mHandler = new Handler();


    AudioManager sAudioManager;


    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Permanent loss of audio focus
                // Pause playback immediately
                pauseMusic();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                pauseMusic();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                sAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, 1);
                flag = 2;
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                if (flag == 1) {
                    startMusic();
                    flag = 0;
                } else if (flag == 2) {
                    sAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, 1);
                    flag = 0;
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        pause_play = (ImageButton) findViewById(R.id.play);
        name = (TextView) findViewById(R.id.Name);
        artist = (TextView) findViewById(R.id.Artist);
        imgView = (ImageView) findViewById(R.id.image);
        pre = (ImageButton) findViewById(R.id.pre);
        next = (ImageButton) findViewById(R.id.next);
        repeat = (ImageButton) findViewById(R.id.repeat);
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        sSeekBar = (SeekBar) findViewById(R.id.seekbar);


        ListView listView = (ListView) findViewById(R.id.list);
        final SongAdapter songAdapter = new SongAdapter(this, songs);
        listView.setAdapter(songAdapter);
        updateBar(songs.get(currentPosition));


        last = songAdapter.getCount();
        sSeekBar.setOnSeekBarChangeListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                playMusic(songs.get(currentPosition));

            }
        });

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
                if(mMediaPlayer!=null) {
                    dur = mMediaPlayer.getCurrentPosition();
                    if (dur / 1000 < 3){
                        previous();
                    }else{
                        playMusic(songs.get(currentPosition));
                    }
                }
                else{
                    previous();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rep == 0) {
                    rep = rep+1;
                    repeat.setImageResource(R.drawable.ic_repeat_black_36dp);
                } else if(rep==1){
                    rep = rep + 1;
                    repeat.setImageResource(R.drawable.ic_repeat_one_black_36dp);
                }
                else{
                    repeat.setImageResource(R.drawable.ic_repeat_white_36dp);
                    rep=0;
                }
                Log.d("rep", "" + rep);
            }
        });


        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle.setImageResource(R.drawable.ic_shuffle_black_36dp);
            }
        });

        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                if (mMediaPlayer != null) {
                    sSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    public void playMusic(Song current) {
        release();

        int result = sAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            long thisId = current.getSAudioResourceId();

            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
                mMediaPlayer.prepare();
            } catch (IOException ex) {
                Log.d("ioexception", "ioexception");
            }

            startMusic();
            updateBar(current);
            flag = 1;
            sSeekBar.setProgress(0);
            sSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.setOnCompletionListener(this);
        }
    }

    public void updateBar(Song current) {
        name.setText(current.getName());
        artist.setText(current.getArtist());
        imgView.setImageBitmap(current.getSImgResourceId());
    }


    public void pauseMusic() {
        mMediaPlayer.pause();
        pause_play.setImageResource(R.drawable.ic_play_circle_filled_black_48dp);

    }

    public void startMusic() {
        mMediaPlayer.start();
        pause_play.setImageResource(R.drawable.ic_pause_circle_filled_black_48dp);
    }

    public void next() {
        if (rep == 0) {
            if (currentPosition == last - 1) {
                currentPosition = 0;
                release();
            } else {
                currentPosition = currentPosition + 1;
                playMusic(songs.get(currentPosition));
            }
        } else if (rep == 1) {
            if (currentPosition == last - 1) {
                currentPosition = 0;
            } else {
                currentPosition = currentPosition + 1;
            }
            playMusic(songs.get(currentPosition));
        } else {
            playMusic(songs.get(currentPosition));
        }
    }

    public void previous() {
            if (rep == 0) {
                if (currentPosition != 0) {
                    currentPosition = currentPosition - 1;
                }
                playMusic(songs.get(currentPosition));
            } else if (rep == 1) {
                if (currentPosition != 0) {
                    currentPosition = currentPosition - 1;
                } else {
                    currentPosition = last - 1;
                }
                playMusic(songs.get(currentPosition));
            } else {
                playMusic(songs.get(currentPosition));
            }
    }

    public void release() {

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            flag = 0;
            sAudioManager.abandonAudioFocus(afChangeListener);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
