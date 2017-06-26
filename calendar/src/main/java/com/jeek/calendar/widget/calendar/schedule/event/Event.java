package com.jeek.calendar.widget.calendar.schedule.event;

import java.util.Calendar;
import java.util.UUID;

/**
 * author  dengyuhan
 * created 2017/6/15 11:07
 */
public class Event {
    private String id;
    private Calendar date;
    private int color;

    public Event(String id, Calendar date, int color) {
        this.id = id;
        this.date = date;
        this.color = color;
    }

    public Event(Calendar date, int color) {
        this(generateEventId(), date, color);
    }

    public Event(String id, int color) {
        this(id, null, color);
    }


    public Event(int color) {
        this(generateEventId(), null, color);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getMonth() {
        return date.get(Calendar.MONTH);
    }

    public int getDay() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public int getYear() {
        return date.get(Calendar.YEAR);
    }


    public static String generateEventId() {
        return UUID.randomUUID().toString();
    }
}
