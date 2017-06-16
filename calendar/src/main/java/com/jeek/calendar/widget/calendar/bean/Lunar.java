package com.jeek.calendar.widget.calendar.bean;

/**
 * author  dengyuhan
 * created 2017/6/16 11:42
 */
public class Lunar {
    public int lunarYear;
    public int lunarDay;
    public int lunarMonth;
    public boolean isLeap;


    public Lunar() {

    }

    public Lunar(int lunarYear, int lunarMonth, int lunarDay, boolean isLeap) {
        this.lunarYear = lunarYear;
        this.lunarMonth = lunarMonth;
        this.lunarDay = lunarDay;
        this.isLeap = isLeap;
    }
}