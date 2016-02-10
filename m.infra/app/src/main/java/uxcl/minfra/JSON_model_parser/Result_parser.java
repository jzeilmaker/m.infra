package uxcl.minfra.JSON_model_parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uxcl.minfra.Model.Result;

/**
 * Created by JZeilmaker on 17/12/15.
 */
public class Result_parser {

    public static List<Result> parseFeed(String content) {
        try {
            JSONObject json = new JSONObject(content);
            List<Result> result_parser_list = new ArrayList<>();

            JSONArray data = json.getJSONArray("result");

            for (int i = 0; i < data.length(); i++) {

                JSONObject c = data.getJSONObject(i);

                Result result_model = new Result();

                result_model.setId(c.getInt("id"));
                result_model.setPerson_id(c.getInt("user_id"));
                result_model.setTemp(c.getString("temp"));
                result_model.setLng(c.getString("long"));
                result_model.setLat(c.getString("lat"));
                result_model.setDate(c.getString("date"));

//                result_model.setMine(c.getString("mine"));
//
//                result_model.setHash(c.getString("hash"));

                result_parser_list.add(result_model);
            }


            return result_parser_list;

        } catch (JSONException ex) {
            Log.e("Fout?", "Ja");
            Log.e("Exception", ex.toString());
            ex.printStackTrace();
            return null;
        }
    }


}
