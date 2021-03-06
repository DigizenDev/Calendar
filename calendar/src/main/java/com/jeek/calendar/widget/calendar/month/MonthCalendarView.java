package com.jeek.calendar.widget.calendar.month;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.jeek.calendar.library.R;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.schedule.event.DayEvent;
import com.jeek.calendar.widget.calendar.schedule.event.Event;
import com.jeek.calendar.widget.calendar.schedule.event.MonthEvent;
import com.jeek.calendar.widget.calendar.schedule.event.YearEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/6 0006.
 */
public class MonthCalendarView extends ViewPager implements OnMonthClickListener {

    private MonthAdapter mMonthAdapter;
    private OnCalendarClickListener mOnCalendarClickListener;

    //<year,<month,<day,List<event>>
    private SparseArray<YearEvent> mEvents = new SparseArray<>();

    private boolean mAnimateEnd = true;//是否动画结束再回调

    public MonthCalendarView(Context context) {
        this(context, null);
    }

    public MonthCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        addOnPageChangeListener(mOnPageChangeListener);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        initMonthAdapter(context, context.obtainStyledAttributes(attrs, R.styleable.MonthCalendarView));
    }

    private void initMonthAdapter(Context context, TypedArray array) {
        mMonthAdapter = new MonthAdapter(context, array, this);
        setAdapter(mMonthAdapter);
        setCurrentItem(mMonthAdapter.getMonthCount() / 2, false);
    }

    @Override
    public void onClickThisMonth(int year, int month, int day) {
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(year, month, day);
        }
    }

    @Override
    public void onClickLastMonth(int year, int month, int day) {
        MonthView monthDateView = mMonthAdapter.getViews().get(getCurrentItem() - 1);
        if (monthDateView != null) {
            monthDateView.setSelectYearMonth(year, month, day);
        }
        setCurrentItem(getCurrentItem() - 1, true);
    }

    @Override
    public void onClickNextMonth(int year, int month, int day) {
        MonthView monthDateView = mMonthAdapter.getViews().get(getCurrentItem() + 1);
        if (monthDateView != null) {
            monthDateView.setSelectYearMonth(year, month, day);
            monthDateView.invalidate();
        }
        onClickThisMonth(year, month, day);
        setCurrentItem(getCurrentItem() + 1, true);
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        private int mPosition;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            this.mPosition = position;
            if (!mAnimateEnd) {
                callPageSelected(this.mPosition);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAnimateEnd && state == ViewPager.SCROLL_STATE_IDLE) {
                callPageSelected(this.mPosition);
            }
        }


        private void callPageSelected(final int position) {
            MonthView monthView = mMonthAdapter.getViews().get(getCurrentItem());
            if (monthView != null) {
                int realSelectDay = monthView.getRealSelectDay();
                int selectDay = monthView.getSelectDay();
                //Log.d("----------------->",monthView.getSelectMonth()+" "+monthView.getRealSelectDay()+" "+realSelectDay);
                int selectYear = monthView.getSelectYear();
                int selectMonth = monthView.getSelectMonth();
                if (mOnCalendarClickListener != null) {
                    mOnCalendarClickListener.onPageChange(selectYear, selectMonth, selectDay);
                }
                monthView.clickThisMonth(monthView.getSelectYear(), monthView.getSelectMonth(), realSelectDay);
            } else {
                MonthCalendarView.this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onPageSelected(position);
                    }
                }, 50);
            }
        }
    };

    /**
     * 跳转到今天
     */
    public void setTodayToView() {
        setCurrentItem(mMonthAdapter.getMonthCount() / 2);
        MonthView monthView = mMonthAdapter.getViews().get(mMonthAdapter.getMonthCount() / 2);
        if (monthView != null) {
            Calendar calendar = Calendar.getInstance();
            monthView.clickThisMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        }
    }

    public boolean addEvent(int year, int month, int day, @ColorInt int color) {
        return addEvent(year, month, day, new Event(color));
    }

    public boolean addEvent(int year, int month, int day, String eventId, @ColorInt int color) {
        return addEvent(year, month, day, new Event(eventId, color));
    }

    public boolean addEvent(int year, int month, int day, Event event) {
        YearEvent yearEvents = mEvents.get(year);
        if (yearEvents == null) {
            yearEvents = new YearEvent(year, new SparseArray<MonthEvent>());
            mEvents.put(year, yearEvents);
        }
        MonthEvent monthEvents = yearEvents.getMonthEvents().get(month);
        if (monthEvents == null) {
            monthEvents = new MonthEvent(month, new SparseArray<DayEvent>());
            yearEvents.getMonthEvents().put(month, monthEvents);
        }
        DayEvent dayEvents = monthEvents.getDayEvents().get(day);
        if (dayEvents == null) {
            dayEvents = new DayEvent(day, new ArrayList<Event>());
            monthEvents.getDayEvents().put(day, dayEvents);
        }
        if (event.getDate() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            event.setDate(calendar);
        }
        boolean add = dayEvents.getEvents().add(event);
        mMonthAdapter.notifyDataSetChanged();
        return add;
    }

    public boolean addEvent(Event event) {
        return addEvent(event.getYear(), event.getMonth(), event.getDay(), event);
    }


    public void setYearEvent(YearEvent yearEvent) {
        mEvents.put(yearEvent.getYear(), yearEvent);
        mMonthAdapter.notifyDataSetChanged();
    }

    public void setMonthEvent(int year, MonthEvent monthEvent) {
        YearEvent yearEvent = mEvents.get(year);
        if (yearEvent == null) {
            yearEvent = new YearEvent(year, new SparseArray<MonthEvent>());
            mEvents.put(yearEvent.getYear(), yearEvent);
        }
        yearEvent.getMonthEvents().put(monthEvent.getMonth(), monthEvent);
        mMonthAdapter.notifyDataSetChanged();
    }


    public void setDayEvent(int year, int month, DayEvent dayEvent) {
        YearEvent yearEvent = mEvents.get(year);
        if (yearEvent == null) {
            yearEvent = new YearEvent(year, new SparseArray<MonthEvent>());
            mEvents.put(yearEvent.getYear(), yearEvent);
        }
        MonthEvent monthEvent = yearEvent.getMonthEvents().get(month);
        if (monthEvent == null) {
            monthEvent = new MonthEvent(month, new SparseArray<DayEvent>());
            yearEvent.getMonthEvents().put(monthEvent.getMonth(), monthEvent);
        }
        monthEvent.getDayEvents().put(dayEvent.getDay(), dayEvent);
        mMonthAdapter.notifyDataSetChanged();
    }


    /**
     * 杀出整年的event
     *
     * @param year
     * @return
     */
    public boolean removeYearEvent(int year) {
        try {
            mEvents.get(year).getMonthEvents().clear();
            mMonthAdapter.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除整月的event
     *
     * @param year
     * @param month 从0开始
     */
    public boolean removeMonthEvent(int year, int month) {
        try {
            mEvents.get(year).getMonthEvents().get(month).getDayEvents().clear();
            mMonthAdapter.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除整天的event
     *
     * @param year
     * @param month 从0开始
     * @param day
     */
    public boolean removeDayEvent(int year, int month, int day) {
        try {
            mEvents.get(year).getMonthEvents().get(month).getDayEvents().get(day).getEvents().clear();
            mMonthAdapter.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除单个event
     *
     * @param eventId
     */
    public boolean removeEvent(int year, int month, int day, String eventId) {
        try {
            List<Event> events = mEvents.get(year).getMonthEvents().get(month).getDayEvents().get(day).getEvents();
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                if (eventId.equals(event.getId())) {
                    events.remove(event);
                }
            }
            mMonthAdapter.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeEvent(Event event) {
        try {
            Calendar calendar = event.getDate();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            List<Event> events = mEvents.get(year).getMonthEvents().get(month).getDayEvents().get(day).getEvents();
            boolean remove = events.remove(event);
            mMonthAdapter.notifyDataSetChanged();
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 清除所有event
     */
    public void clearAllEvent() {
        mEvents.clear();
        mMonthAdapter.notifyDataSetChanged();
    }

    /**
     * 设置点击日期监听
     *
     * @param onCalendarClickListener
     */
    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    public SparseArray<MonthView> getMonthViews() {
        return mMonthAdapter.getViews();
    }

    public MonthView getCurrentMonthView() {
        return getMonthViews().get(getCurrentItem());
    }

    public SparseArray<YearEvent> getEvents() {
        return mEvents;
    }

    public YearEvent getYearEventNotNull(int year) {
        YearEvent yearEvent = mEvents.get(year);
        if (yearEvent == null) {
            yearEvent = new YearEvent(year, new SparseArray<MonthEvent>());
            mEvents.put(year, yearEvent);
        }
        return yearEvent;
    }


    public MonthEvent getMonthEventNotNull(int year, int month) {
        YearEvent yearEvent = getYearEventNotNull(year);
        MonthEvent monthEvent = yearEvent.getMonthEvents().get(month);
        if (monthEvent == null) {
            monthEvent = new MonthEvent(month, new SparseArray<DayEvent>());
            yearEvent.getMonthEvents().put(month, monthEvent);
        }
        return monthEvent;
    }

    public DayEvent getDayEventNotNull(int year, int month, int day) {
        MonthEvent monthEvent = getMonthEventNotNull(year, month);
        DayEvent dayEvent = monthEvent.getDayEvents().get(day);
        if (dayEvent == null) {
            dayEvent = new DayEvent(day, new ArrayList<Event>());
            monthEvent.getDayEvents().put(day, dayEvent);
        }
        return dayEvent;
    }

    public Calendar getSelectDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getSelectYear());
        calendar.set(Calendar.MONTH, getSelectMonth());
        calendar.set(Calendar.DAY_OF_MONTH, getSelectDay());
        return calendar;
    }

    public int getSelectDay() {
        return getCurrentMonthView().getSelectDay();
    }

    public int getSelectMonth() {
        return getCurrentMonthView().getSelectMonth();
    }

    public int getSelectYear() {
        return getCurrentMonthView().getSelectYear();
    }

    public int getRealSelectDay() {
        return getCurrentMonthView().getRealSelectDay();
    }

    public void setAnimateEnd(boolean animateEnd) {
        this.mAnimateEnd = animateEnd;
    }

}
