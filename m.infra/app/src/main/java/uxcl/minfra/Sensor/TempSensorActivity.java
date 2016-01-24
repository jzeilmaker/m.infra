package uxcl.minfra.Sensor;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import uxcl.minfra.MainActivity;
import uxcl.minfra.Model.Person;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uxcl.minfra.Sensor.Credential;

public class TempSensorActivity extends MainActivity implements SensorEventListener {
    private List<MyTask> myTasks;
    private ProgressBar progressBar;

    GPSTracker gps;
    Float temp;
    URL url;

    private TextView temperaturelabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperaturelabel = (TextView) findViewById(R.id.textView);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.
        }
        if (mTemperature == null) {
            temperaturelabel.setText(NOT_SUPPORTED_MESSAGE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mTemperature == null) {
                    temperaturelabel.setText(NOT_SUPPORTED_MESSAGE+" \n\n What did i tell you!");
                }else {


                    // GET GPS CORDS
                    gps = new GPSTracker(TempSensorActivity.this);

                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        float tmp = getTemp();



                        if (isOnline()) {
                            // Dit is stap 1. RequestData wordt aangeroepen. Je stuurt hier in de URL mee

                            Toast.makeText(getApplicationContext(), "\nLat: " + latitude + "\nLong: " + longitude + "\nTemp: " + tmp
                                    + "\nHash "
                                    , Toast.LENGTH_LONG).show();

                            attemptPost(url.RESULT, latitude, longitude, tmp);

                            Log.d("nw: ","wel netwerk");

                            //postData(url.RESULT); // In mijn geval is dat een testpagina
                        } else {
                           Log.d("nw: ","Geen netwerk verbinding");
                        }




                    } else {
                        gps.showSettingsAlert();
                    }
                }
            }
        });

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



   public void attemptPost(String uri, double latitude, double longtide, float temp){
       RequestPackage p = new RequestPackage();

       p.setMethod("POST");
       p.setUri(uri);

//       p.setParam("user_id", );
       p.setParam("temp", String.valueOf(temp));
       p.setParam("latitude", String.valueOf(latitude));
       p.setParam("longtide", String.valueOf(longtide));

       Log.e("getParam", p.getEncodedParams()); //logging

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
            String content = HttpManager.getData(params[0]);
            return content;
        }

        protected void onPostExecute(String result) {
            Log.d("nw:", "yes werkt " + result);
//            gebruiker = login_parser.parseFeed(result);
//
//            boolean authenticated = gebruiker.isAuthenticated();
//            if (authenticated) {
//                saveUser(gebruiker);
//            }
//
//            login(authenticated);
//
//            myTasks.remove(this);
//
//            if (myTasks.size() == 0) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
        }
    }


}

