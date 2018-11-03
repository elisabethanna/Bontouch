package com.example.android.optimeradse;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anna on 2018-10-29.
 */


public class HttpGetRequest extends AsyncTask<String, Void, String> {

    //instantiate an interface variable
    public AsyncResponse delegate = null;

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];
        String result;
        String inputLine;


        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);

            //Create a connection
            HttpURLConnection connection = (HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream(), "ISO-8859-1");

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();


            //Check if the line we are reading is not null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine + " ");
            }




            //for testing smaller set of words, makes calc faster
            /*for (int i = 0; i < 100; i++) {
                stringBuilder.append(reader.readLine() + " ");
            }*/



            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            result = stringBuilder.toString().toLowerCase();


        } catch (IOException e) {
            e.printStackTrace();
            result=null;
        }
        return result;

    }

    //gets the result from doInBackground() and passes it on to MainActivity through the interface variable
    @Override
    protected void onPostExecute(String result){
        delegate.storeWordsInHMap(result);

    }

    //creats a interface and determines where we can get the output from this class in MainActivty
    public interface AsyncResponse {
        void storeWordsInHMap(String output);
    }


}


