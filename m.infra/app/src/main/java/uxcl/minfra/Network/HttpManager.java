package uxcl.minfra.Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rene on 22-11-2015.
 */

public class HttpManager {

    public static String getData(RequestPackage rp) {

        BufferedReader reader = null;
        String uri = rp.getUri();
        if (rp.getMethod().equals("GET")) {
            uri += "?" + rp.getEncodedParams();
        }

        try {
            URL url = new URL (uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(rp.getMethod());

            if (rp.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(rp.getEncodedParams());
                Log.e("nwH", rp.getEncodedParams()+"");
                writer.flush();
            }
           //return rp.getEncodedParams();



            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.e("nwH2",  sb.toString());
            return sb.toString();

        } catch(Exception e){
            Log.e("nwH", "noGo");
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("nwH", "noGo1");
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
