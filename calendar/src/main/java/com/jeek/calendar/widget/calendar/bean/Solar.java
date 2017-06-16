package com.jeek.calendar.widget.calendar.bean;

/**
 * author  dengyuhan
 * created 2017/6/16 11:42
 */
public class Solar {
    public int solarDay;
    public int solarMonth;
    public int solarYear;

    public Solar() {

    }

    public Solar(int solarYear, int solarMonth, int solarDay) {
        this.solarYear = solarYear;
        this.solarMonth = solarMonth;
        this.solarDay = solarDay;
    }
}