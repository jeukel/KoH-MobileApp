package com.bases_datos.koh_mobileapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONObject;

public class Attackin extends AppCompatActivity {

    TextView time2;
    int down = 120000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_being_attacked);

        time2=(TextView)findViewById(R.id.time);

        CountDownTimer downTimer = new CountDownTimer(down, 1000) {

            public void onTick(long millisUntilFinished) {
                time2.setText("seconds remaining: " + millisUntilFinished / 1000);
                if ((down / 1000) == (105 | 90 | 75 | 60 | 45 | 30 | 15)) {
                    Location loc = mMap.getMyLocation();
                    double X = loc.getLatitude();
                    double Y = loc.getLongitude();
                    LoginActivity lg = (LoginActivity) getApplicationContext();
                    String email = lg.getUsr();
                    ServerConnection conn = new ServerConnection();
                    String query = "un=" + email + "&x=" + X + "&y=" + Y;
                    if (conn.requestWebService(3, query) != null) {
                        startActivity(new Intent(Attackin.this, Fight.class));
                    }
                }

            }

            public void onFinish() {

                // On finish countdown it uploads the taps.
                time2.setText("Zone Won");
                // On countdown finish it uploads the coords


            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_being_attacked, menu);
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
}
