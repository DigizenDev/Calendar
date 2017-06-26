package com.jeek.calendar.widget.calendar.schedule.event;

import android.util.SparseArray;

/**
 * author  dengyuhan
 * created 2017/6/23 19:09
 */
public class MonthEvent {
    private int month;
    private SparseArray<DayEvent> dayEvents;

    public MonthEvent(int month, SparseArray<DayEvent> dayEvents) {
        this.month = month;
        this.dayEvents = dayEvents;
    }

    public MonthEvent() {
    }

    public SparseArray<DayEvent> getDayEvents() {
        return dayEvents == null ? dayEvents = new SparseArray<DayEvent>() : dayEvents;
    }

    public void setDayEvents(SparseArray<DayEvent> dayEvents) {
        this.dayEvents = dayEvents;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
