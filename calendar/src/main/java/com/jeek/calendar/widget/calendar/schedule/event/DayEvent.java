package com.jeek.calendar.widget.calendar.schedule.event;

import java.util.ArrayList;
import java.util.List;

/**
 * author  dengyuhan
 * created 2017/6/23 19:09
 */
public class DayEvent {
    private int day;
    private List<Event> events;

    public DayEvent(int day, List<Event> events) {
        this.day = day;
        this.events = events;
    }

    public DayEvent() {
    }

    public List<Event> getEvents() {
        return events == null ? events = new ArrayList<Event>() : events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
