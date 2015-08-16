package com.bases_datos.koh_mobileapp;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public class Fight extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    MediaPlayer player;
    private int taps = 0;
    TextView text1;
    private static final String FORMAT = "%02d:%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);
        player = new MediaPlayer();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        text1=(TextView)findViewById(R.id.textView1);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                text1.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //get email.
                LoginActivity lg = (LoginActivity) getApplicationContext();
                String email = lg.getUsr();

                // On finish countdown it uploads the taps.
                text1.setText("done!");
                ServerConnection conn = new ServerConnection();
                String query = "un=" + email + "&" + "tps=" + taps;
                conn.requestWebService(4, query);

            }
        }.start();

        try{
            AssetManager manager = this.getAssets();
            AssetFileDescriptor descriptor = manager.openFd("lase2.ogg");
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        }catch(Exception e){};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerClick(View view) {
        taps = taps++;
        if(player.isPlaying()==false){

            try{
                player.prepare();
            }catch(Exception e){};
            player.start();
            player.setOnCompletionListener(this);

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        player.stop();
    }
}

