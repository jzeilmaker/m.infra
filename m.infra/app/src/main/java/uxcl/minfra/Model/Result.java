package uxcl.minfra.Model;

/**
 * Created by JZeilmaker on 17/12/15.
 */
public class Result {

    private int     person_id;
    private String  temp;
    private String  lat;
    private String  lng;
    private String  mine;

    public Result(String temp, String lat, String lng) {
        super();
        this.temp = temp;
        this.lat = lat;
        this.lng = lng;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMine() {
        return mine;
    }

    public void setMine(String mine) {
        this.mine = mine;
    }

    private String  hash;


}
