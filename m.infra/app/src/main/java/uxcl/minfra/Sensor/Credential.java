package uxcl.minfra.Sensor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by JZeilmaker on 17/12/15.
 */
public class Credential extends AppCompatActivity {
    String imeiDevice=null;
    String imsiDevice=null;


//    getPhoneImei();
//    String hashImei = sha256(getPhoneImei());
//    Log.e("phoneID:", "sha(imei) " + hashImei);

    public String getPhoneImei(){
        TelephonyManager telephonyManager;
        telephonyManager  = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imeiDevice = telephonyManager.getDeviceId();
//        imsiDevice = telephonyManager.getSubscriberId();

        Log.e("phoneID:", "imei: " + imeiDevice);

        return  imeiDevice;

    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
