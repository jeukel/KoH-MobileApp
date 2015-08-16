package com.bases_datos.koh_mobileapp;

import android.os.Build;

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

/**
 * Created by jeukel on 13/08/15.
 */
public class ServerConnection {
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
    public JSONObject requestWebService(int cas, String data) {
        disableConnectionReuseIfNecessary();
        String SERVER_ADDRESS = "http://172.19.127.63/";
        final int CONNECTION_TIMEOUT = 1000 * 15;
        final int TIMEOUT = 1000 * 15;

        HttpURLConnection urlConnection = null;
        String monthString = "";
        try {
            // create connection
            switch (cas){
                case 1:
                    monthString = "login.php?";
                    break;
                case 2: monthString = "register.php?";
                    break;
                case 3: monthString = "update_coords.php?";
                    break;
                case 4: monthString = "game.php?";
                    break;
                default:
                    break;
            }
            SERVER_ADDRESS = SERVER_ADDRESS.concat(monthString);
            URL urlToRequest = new URL(SERVER_ADDRESS);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(TIMEOUT);

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

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
}
