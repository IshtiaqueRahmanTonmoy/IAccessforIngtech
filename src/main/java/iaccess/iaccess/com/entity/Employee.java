package iaccess.iaccess.com.entity;

/**
 * Created by TONMOYPC on 2/12/2018.
 */

public class Employee {
    String id,day,month,name,fromtotime,location;

    public Employee(String name, String month, String day, String fromtotime, String location) {
        this.name = name;
        this.month = month;
        this.day = day;
        this.fromtotime = fromtotime;
        this.location = location;
    }

    public Employee(String id, String name, String month, String day, String fromtotime, String location) {
        this.id = id;
        this.name = name;
        this.month = month;
        this.day = day;
        this.fromtotime = fromtotime;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String date) {
        this.day = day;
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

    public String getFromtotime() {
        return fromtotime;
    }

    public void setFromtotime(String fromtotime) {
        this.fromtotime = fromtotime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
