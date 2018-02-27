package iaccess.iaccess.com.entity;

/**
 * Created by TONMOYPC on 2/25/2018.
 */

public class Event {
    String id,name,title,time;

    public Event(String id, String time, String title, String name) {
        this.id = id;
        this.time = time;
        this.title = title;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
