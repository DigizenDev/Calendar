package com.jeek.calendar.widget.calendar.schedule.event;

import android.util.SparseArray;

/**
 * author  dengyuhan
 * created 2017/6/23 19:09
 */
public class YearEvent {
    private int year;
    private SparseArray<MonthEvent> monthEvents;

    public YearEvent() {
    }

    public YearEvent(int year, SparseArray<MonthEvent> monthEvents) {
        this.year = year;
        this.monthEvents = monthEvents;
    }

    public SparseArray<MonthEvent> getMonthEvents() {
        return monthEvents == null ? monthEvents = new SparseArray<MonthEvent>() : monthEvents;
    }

    public void setMonthEvents(SparseArray<MonthEvent> monthEvents) {
        this.monthEvents = monthEvents;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
