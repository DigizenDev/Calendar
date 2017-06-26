package com.jeek.calendar.widget.calendar;

import java.util.Calendar;


public interface OnCalendarChangeListener {
    void onClickDate(Calendar date, int year, int month, int day);

    void onPageChange(Calendar date, int year, int month, int day);
}
