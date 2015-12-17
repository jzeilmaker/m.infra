package uxcl.minfra.Model;

/**
 * Created by JZeilmaker on 17/12/15.
 */
public class Result {

    private int     person_id;
    private String  temp;
    private String  location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
