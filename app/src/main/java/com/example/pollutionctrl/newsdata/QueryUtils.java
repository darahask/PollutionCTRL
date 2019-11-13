package com.example.pollutionctrl.newsdata;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL createUrl(String string){

        URL url = null;
        try{
            url = new URL(string);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url",e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";
        if (url == null)
            return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try
        {
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(100000);
            httpURLConnection.setConnectTimeout(150000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream){

        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    private static AirData extractAirData(String JSON_RESPONSE) {
        String name;
        int aqi;// index
        int tp;// tmp
        int pr;// pressure
        int hm;//humidity
        int ws;//wind speed
        int wd;//wind direction
        String main;//main pollutant

        try {
            JSONObject mJsonObject = new JSONObject(JSON_RESPONSE);
            JSONObject dJsonObject = mJsonObject.getJSONObject("data");
            JSONObject cJsonObject = dJsonObject.getJSONObject("current");
            JSONObject wJsonObject = cJsonObject.getJSONObject("weather");
            JSONObject pJsonObject = cJsonObject.getJSONObject("pollution");

            name = dJsonObject.getString("city");
            aqi = pJsonObject.getInt("aqius");
            tp = wJsonObject.getInt("tp");
            pr = wJsonObject.getInt("pr");
            hm = wJsonObject.getInt("hu");
            ws = wJsonObject.getInt("ws");
            wd = wJsonObject.getInt("wd");
            main = pJsonObject.getString("mainus");

            return new AirData(name,aqi,tp,pr,hm,ws,wd,main);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return null;
    }

    public static AirData fetchAirData(String requestUrl){

        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try
        {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractAirData(jsonResponse);
    }

}
