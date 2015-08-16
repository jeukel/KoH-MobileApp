package com.bases_datos.koh_mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    String ema;
    String pass;
    String sch;
    AutoCompleteTextView mEmailView1;
    EditText mPasswordView1;
    EditText mSchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mPasswordView1 = (EditText) findViewById(R.id.editText);
        mSchView = (EditText) findViewById(R.id.editText2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void regClick(View view) {
        ema = mEmailView1.getText().toString();
        pass = mPasswordView1.getText().toString();
        sch = mSchView.getText().toString();
        ServerConnection conn = new ServerConnection();
        String query = "un=" + ema + "&" + "pwd=" + pass + "sch=" + sch + "&" + "x=0&y=0";
        conn.requestWebService(2, query);
    }


}
