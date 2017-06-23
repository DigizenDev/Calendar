package com.jeek.calendar.widget.calendar.schedule.event;

import android.util.SparseArray;

/**
 * author  dengyuhan
 * created 2017/6/23 19:09
 */
public class YearEvent {
    private SparseArray<MonthEvent> monthEvents;

    public YearEvent() {
    }

    public YearEvent(SparseArray<MonthEvent> monthEvents) {
        this.monthEvents = monthEvents;
    }

    public SparseArray<MonthEvent> getMonthEvents() {
        return monthEvents == null ? new SparseArray<MonthEvent>() : monthEvents;
    }

    public void setMonthEvents(SparseArray<MonthEvent> monthEvents) {
        this.monthEvents = monthEvents;
    }
}
