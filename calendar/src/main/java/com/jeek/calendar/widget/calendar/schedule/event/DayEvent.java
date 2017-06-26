package com.jeek.calendar.widget.calendar.schedule.event;

import java.util.ArrayList;
import java.util.List;

/**
 * author  dengyuhan
 * created 2017/6/23 19:09
 */
public class DayEvent {
    private List<Event> events;

    public DayEvent(List<Event> events) {
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
}
