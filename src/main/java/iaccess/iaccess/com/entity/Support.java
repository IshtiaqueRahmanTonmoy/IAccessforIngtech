package iaccess.iaccess.com.entity;

/**
 * Created by TONMOYPC on 2/24/2018.
 */

public class Support {
    String id;
    String date;
    String month;
    String name;
    String fromto;
    String address;

    public Support(String id, String name, String month, String date, String fromto, String address) {
        this.id = id;
        this.name = name;
        this.month = month;
        this.date = date;
        this.fromto = fromto;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromto() {
        return fromto;
    }

    public void setFromto(String fromto) {
        this.fromto = fromto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
