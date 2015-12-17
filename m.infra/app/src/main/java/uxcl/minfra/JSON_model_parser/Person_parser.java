package uxcl.minfra.JSON_model_parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uxcl.minfra.Model.Person;
import uxcl.minfra.Model.Result;

/**
 * Created by JZeilmaker on 17/12/15.
 */
public class Person_parser {


    public static List<Person> parseFeed(String content) {
        try {
            JSONObject json = new JSONObject(content);
            List<Person> result_parser_list = new ArrayList<>();

            JSONArray data = json.getJSONArray("result");

            for (int i = 0; i < data.length(); i++) {

                JSONObject c = data.getJSONObject(i);

                Person person_model = new Person();

                person_model.setPerson_id(c.getInt("id"));
                person_model.setHash(c.getString("hash"));
                person_model.setMime(c.getString("mime"));
                person_model.setUsername(c.getString("username"));

                result_parser_list.add(person_model);
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
