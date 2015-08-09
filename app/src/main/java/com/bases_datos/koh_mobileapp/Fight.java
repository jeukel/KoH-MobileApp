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
                text1.setText("done!");
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


class uploadPushes {
    /**
     * required in order to prevent issues in earlier Android version.
     */
    private void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    /*
     * Request for RestFul Web Service
     */
    public JSONObject requestWebService(int taps) {
        disableConnectionReuseIfNecessary();
        final String SERVER_ADDRESS = "http://172.19.127.63/index.php";
        final int CONNECTION_TIMEOUT = 1000 * 15;
        final int DATARETRIEVAL_TIMEOUT = 1000 * 15;

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(SERVER_ADDRESS);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStreamWriter d = new OutputStreamWriter(urlConnection.getOutputStream());
            JSONObject qty = new JSONObject();
            qty.put("taps",taps);

            //F*CKING NEEDED LINE!!!!!!
            d.write(qty.toString());

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
                // DO Nothing.
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
                return null;
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException | SocketTimeoutException e) {
            e.printStackTrace();
            // URL is invalid
        } catch (IOException | JSONException e) {
            // could not read response body
            // (could not create input stream)
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
}

