package uxcl.minfra.Sensor;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import uxcl.minfra.JSON_model_parser.Result_parser;
import uxcl.minfra.MainActivity;
import uxcl.minfra.Model.Person;
import uxcl.minfra.Model.Result;
import uxcl.minfra.Network.HttpManager;
import uxcl.minfra.Network.RequestPackage;
import uxcl.minfra.Network.URL;
import uxcl.minfra.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uxcl.minfra.Network.RequestPackage;


public class TempSensorActivity extends MainActivity implements SensorEventListener {
    private List<MyTask> myTasks;
    private ProgressBar progressBar;
    private List<Result> results;

    GPSTracker gps;
    Float temp;
    URL url;

    private ListView listView;
    private Context ctx;

    private TextView temperaturelabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** get data from server ---------------- */
        if (isOnline()) {
            Log.d("nw ", "Server is online ");

            // Dit is stap 1. RequestData wordt aangeroepen. Je stuurt hier in de URL mee
            this.requestData(getSha1Hex(getPhoneImei()),getSha1Hex(getPhoneImei()));
        } else {
            Log.d("nw", "Server is offline ");
        }

        Log.d("nw5", String.valueOf(results));

//        List listCity = new ArrayList();
//        listCity.add(new Result("21","44","55"));
//        listCity.add(new Result("18","66","77"));

        listView = ( ListView ) findViewById(R.id.listview);
        listView.setAdapter(new ActivityAdapter(this, R.layout.content_main, results));


//        temperaturelabel = (TextView) findViewById(R.id.textView);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.
        }
//        if (mTemperature == null) {
//            listView.setText(NOT_SUPPORTED_MESSAGE);
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // GET GPS CORDS
                gps = new GPSTracker(TempSensorActivity.this);

                if (gps.canGetLocation()) {

                    double latitude   = gps.getLatitude();
                    double longitude  = gps.getLongitude();
                    float tmp         = 24;
                    String phone_imei = getPhoneImei();
                    String sha1Hex    = getSha1Hex(phone_imei);

                    if (isOnline())
                    {
                        if (mTemperature != null)
                        {
                            tmp  = getTemp();
                        }

                        attemptPost(url.RESULT, latitude, longitude, tmp, sha1Hex, phone_imei);

                        Toast.makeText(getApplicationContext(),
                                "\nLat: " + latitude + "\nLong: " + longitude + "\nTemp: " + tmp
                                + "\nImei: " + phone_imei + "\nHash: " + sha1Hex
                                , Toast.LENGTH_LONG).show();

                        Log.d("nw: ", "wel netwerk");
                    } else
                    {
                       Log.d("nw: ","Geen netwerk verbinding");
                    }
                } else
                {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    public static String getSha1Hex(String clearString)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes)
            {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
            return null;
        }
    }

    public String getPhoneImei(){
        TelephonyManager telephonyManager;
        telephonyManager  = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imeiDevice = telephonyManager.getDeviceId();

        Log.e("phoneID:", "imei: " + imeiDevice);

        return  imeiDevice;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        url = new URL();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getUser() {
        // Check if person exist if nog create in db else true for attemptPost()
        return false;
    }



   public void attemptPost(String uri, double latitude, double longtide, float temp, String sha1hex, String phone_imei){
       RequestPackage p = new RequestPackage();

       p.setUri("http://rkodde.nl/infra");
       p.setMethod("GET");
       p.setParam("method", "post");
       p.setParam("mime", String.valueOf(sha1hex));
       p.setParam("hash", String.valueOf(sha1hex));
       p.setParam("temp", String.valueOf(temp));
       p.setParam("lat", String.valueOf(latitude));
       p.setParam("long", String.valueOf(longtide));

       Log.e("nw", p.getEncodedParams()); //logging
       Log.e("nw", p+"");
       MyTask mytask = new MyTask();
       mytask.execute(p);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float ambient_temperature = event.values[0];
        setTemp(ambient_temperature);
        //temperaturelabel.setText("Ambient Temperature:\n " + String.valueOf(ambient_temperature));
    }

    public void setTemp(Float temp){
        this.temp = temp;
    }

    public float getTemp(){
        return this.temp;
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    private class MyTask extends AsyncTask<RequestPackage, String, String>
    {
        protected void onPreExecute() {
//            if (myTasks.size() == 0) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//            myTasks.add(this);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            Log.d("nw2",params+"");
            String content = HttpManager.getData(params[0]);
            Log.d("nw2",content+"");
            return content;
        }

        protected void onPostExecute(String result) {
            Log.d("nw:", "yes werkt " + result);
            // Vang je result op, en voer het aan je parser die het omzet naar een java model
            results = Result_parser.parseFeed(result);
            Log.d("nw5", result);
            refreshDisplay();
        }
    }

    public void refreshDisplay() {


    }

    private void requestData(String sha1hex, String phone_imei) {
        RequestPackage p = new RequestPackage();

        p.setMethod("GET");
        p.setUri("http://rkodde.nl/infra");
        p.setParam("method", "get");
        p.setParam("mime", String.valueOf(sha1hex));
        p.setParam("hash", String.valueOf(sha1hex));

        Log.d("getParam", p.getEncodedParams()); //logging

        MyTask mytask = new MyTask();
        mytask.execute(p);
    }

}

