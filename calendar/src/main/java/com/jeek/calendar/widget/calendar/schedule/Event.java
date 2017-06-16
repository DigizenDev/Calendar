package com.jeek.calendar.widget.calendar.schedule;

import java.util.Date;

/**
 * author  dengyuhan
 * created 2017/6/15 11:07
 */
public class Event {
    private Date date;
    private int color;

    public Event(Date date, int color) {
        this.date = date;
        this.color = color;
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
}
