package com.jeek.calendar.widget.calendar.schedule.event;

import java.util.Date;
import java.util.UUID;

/**
 * author  dengyuhan
 * created 2017/6/15 11:07
 */
public class Event {
    private String id;
    private Date date;
    private int color;

    public Event(String id, Date date, int color) {
        this.id = id;
        this.date = date;
        this.color = color;
    }

    public Event(Date date, int color) {
        this(generateEventId(), date, color);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public static String generateEventId() {
        return UUID.randomUUID().toString();
    }
}
