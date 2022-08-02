package com.example.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.json.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity<inputLine> extends AppCompatActivity {
    EditText latitude,longitude;
    TextView city1name, city2name, city3name,city1temp,city2temp,city3temp,city1time,city2time,city3time,city1date,city2date,city3date,city1conditions,city2conditions,city3conditions;
    ImageView c1image,c2image,c3image;
    Button enter;
    String apikey = "f45c0c65f0b0fe826a92706f2167146f";
    String [] temp = new String [3];
    String [] name = new String [3];
    String [] time = new String [3];
    String [] Cdate = new String [3];
    String [] condition = new String [3];
    String [] icon = new String[3];
    public MainActivity() throws IOException {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        enter = findViewById(R.id.Enter);
        city1name = findViewById(R.id.city1name);
        city2name = findViewById(R.id.city2name);
        city3name = findViewById(R.id.city3name);

        city1temp = findViewById(R.id.city1temp);
        city2temp = findViewById(R.id.city2temp);
        city3temp = findViewById(R.id.city3temp);

        city1time = findViewById(R.id.city1time);
        city2time = findViewById(R.id.city2time);
        city3time = findViewById(R.id.city3time);

        city1date = findViewById(R.id.city1date);
        city2date = findViewById(R.id.city2date);
        city3date = findViewById(R.id.city3date);

        city1conditions = findViewById(R.id.city1conditions);
        city2conditions = findViewById(R.id.city2conditions);
        city3conditions = findViewById(R.id.city3conditions);


        c1image = findViewById(R.id.imageViewc1);
        c2image = findViewById(R.id.imageViewc2);
        c3image = findViewById(R.id.imageViewc3);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FE9505"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new JsonReader().execute();
            }
        });


    }

    private class JsonReader extends AsyncTask<URL, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected Void doInBackground(URL... urls) {
            String line = "";
            String lat = latitude.getText().toString();
            String log = longitude.getText().toString();
            Log.d("TAG_INFO", lat.toString());

            JSONObject object = new JSONObject();
            StringBuilder sb = new StringBuilder();
            //log =  "-74.535278";
            //lat = "40.382118";
            URL url = null;
            try {
                url = new URL("https://api.openweathermap.org/data/2.5/find?lat="+lat+"&lon="+log+"&cnt=3&units=imperial&appid="+apikey);
                Log.d("TAG_INFOurl",url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection con = null;
            try {
                assert url != null;
                con = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert con != null;
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                object = new JSONObject(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("TAG_INFO",object.toString());
            String[] dt = new String[3];
            for(int i=0; i<3; i++){
                try {
                    temp[i] = (object.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp"));
                    name[i] = (object.getJSONArray("list").getJSONObject(i).getString("name"));
                    icon[i] = (object.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                    condition[i] = (object.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dt[i] = object.getJSONArray("list").getJSONObject(0).getString("dt");
                    long units = Long.parseLong(dt[i]) * 1000;
                    Date date = new Date(units);
                    @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("HH:mm");
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                    dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

                    String tempor = sdf.format(date);
                    int sub3 = Integer.parseInt(tempor.substring(0,2));
                    if(sub3 < 12)
                        time[i] = sub3 + tempor.substring(2)+" AM";
                    if(sub3 > 12)
                        time[i] =  (sub3 - 12) + tempor.substring(2)+" PM";
                    else
                        time[i] = "12"+tempor.substring(2)+" AM";
                    //time[i] = sdf.format(date);
                    Cdate[i] = dateFormat.format(date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            city1name.setText(name[0]);
            city2name.setText(name[1]);
            city3name.setText(name[2]);

            city1time.setText(time[0]);
            city2time.setText(time[1]);
            city3time.setText(time[2]);

            city1temp.setText(temp[0]+"°");
            city2temp.setText(temp[1]+"°");
            city3temp.setText(temp[2]+"°");

            city1date.setText(Cdate[0]);
            city2date.setText(Cdate[1]);
            city3date.setText(Cdate[2]);

            city1conditions.setText(condition[0]);
            city2conditions.setText(condition[1]);
            city3conditions.setText(condition[2]);

            c1image.setImageResource(R.drawable.b13d);
            c2image.setImageResource(R.drawable.b01d);
            c3image.setImageResource(R.drawable.b10d);

            Log.d("TAG_INFO_ICON",icon[0]);
            switch (icon[0]){
                case "01d":
                case "01n":
                    c1image.setImageResource(R.drawable.b01d);
                    break;
                case "02d":
                case "02n":
                    c1image.setImageResource(R.drawable.b02d);
                    break;
                case "03d":
                case "03n":
                    c1image.setImageResource(R.drawable.b03d);
                    break;
                case "04d":
                case "04n":
                    c1image.setImageResource(R.drawable.b04d);
                    break;
                case "09d":
                case "09n":
                    c1image.setImageResource(R.drawable.b09d);
                    break;
                case "10d":
                case "10n":
                    c1image.setImageResource(R.drawable.b10d);
                    break;
                case "11d":
                case "11n":
                    c1image.setImageResource(R.drawable.b11d);
                    break;
                case "13d":
                case "13n":
                    c1image.setImageResource(R.drawable.b13d);
                    break;
                case "50d":
                case "50n":
                    c1image.setImageResource(R.drawable.b50d);
                    break;

            }
            switch (icon[1]){
                case "01d":
                case "01n":
                    c2image.setImageResource(R.drawable.b01d);
                    break;
                case "02d":
                case "02n":
                    c2image.setImageResource(R.drawable.b02d);
                    break;
                case "03d":
                case "03n":
                    c2image.setImageResource(R.drawable.b03d);
                    break;
                case "04d":
                case "04n":
                    c2image.setImageResource(R.drawable.b04d);
                    break;
                case "09d":
                case "09n":
                    c2image.setImageResource(R.drawable.b09d);
                    break;
                case "10d":
                case "10n":
                    c2image.setImageResource(R.drawable.b10d);
                    break;
                case "11d":
                case "11n":
                    c2image.setImageResource(R.drawable.b11d);
                    break;
                case "13d":
                case "13n":
                    c2image.setImageResource(R.drawable.b13d);
                    break;
                case "50d":
                case "50n":
                    c2image.setImageResource(R.drawable.b50d);
                    break;
            }
            switch (icon[2]){
                case "01d":
                case "01n":
                    c3image.setImageResource(R.drawable.b01d);
                    break;
                case "02d":
                case "02n":
                    c3image.setImageResource(R.drawable.b02d);
                    break;
                case "03d":
                case "03n":
                    c3image.setImageResource(R.drawable.b03d);
                    break;
                case "04d":
                case "04n":
                    c3image.setImageResource(R.drawable.b04d);
                    break;
                case "09d":
                case "09n":
                    c3image.setImageResource(R.drawable.b09d);
                    break;
                case "10d":
                case "10n":
                    c3image.setImageResource(R.drawable.b10d);
                    break;
                case "11d":
                case "11n":
                    c3image.setImageResource(R.drawable.b11d);
                    break;
                case "13d":
                case "13n":
                    c3image.setImageResource(R.drawable.b13d);
                    break;
                case "50d":
                case "50n":
                    c3image.setImageResource(R.drawable.b50d);
                    break;

            }



        }
    }



}

