package com.digizen.g2u.widget.calendar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeek.calendar.library.R;
import com.jeek.calendar.widget.calendar.OnCalendarChangeListener;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.month.MonthCalendarView;
import com.jeek.calendar.widget.calendar.month.MonthView;
import com.jeek.calendar.widget.calendar.schedule.event.Event;
import com.jeek.calendar.widget.calendar.schedule.event.MonthEvent;
import com.jeek.calendar.widget.calendar.schedule.event.YearEvent;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * author  dengyuhan
 * created 2017/6/14 18:05
 */
public class G2UCalendarCardView extends RelativeLayout {

    private TextView mTitleMonth;
    private TextView mTitleYear;

    private String[] mFormatMonths = new DateFormatSymbols(Locale.getDefault()).getMonths();

    private MonthCalendarView mCalendarView;
    private OnCalendarChangeListener mOnCalendarChangeListener;
    private final int INITIAL_OFFSCREEN_PAGE_LIMIT = 1;//最少1
    private int mOffscreenPageLimit = INITIAL_OFFSCREEN_PAGE_LIMIT;

    public G2UCalendarCardView(Context context) {
        this(context, null);
    }

    public G2UCalendarCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public G2UCalendarCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        View.inflate(getContext(), R.layout.layout_g2u_calendar, this);
        mCalendarView = (MonthCalendarView) findViewById(R.id.mcvCalendar);

        mTitleMonth = (TextView) findViewById(R.id.tv_calendar_title_month);
        mTitleYear = (TextView) findViewById(R.id.tv_calendar_title_year);
        findViewById(R.id.tv_calendar_title_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setCurrentItem(mCalendarView.getCurrentItem() - 1);
            }
        });
        findViewById(R.id.tv_calendar_title_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setCurrentItem(mCalendarView.getCurrentItem() + 1);
            }
        });
        findViewById(R.id.tv_calendar_today).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setTodayToView();
                mOffscreenPageLimit = INITIAL_OFFSCREEN_PAGE_LIMIT;
            }
        });
        //setDynamicOffscreenPageLimit();
        mCalendarView.setOnCalendarClickListener(new OnCalendarClickListener() {
            @Override
            public void onClickDate(int year, int month, int day) {
                if (mOnCalendarChangeListener != null) {
                    mOnCalendarChangeListener.onClickDate(getCalendar(year, month, day), year, month, day);
                }
            }

            @Override
            public void onPageChange(int year, int month, int day) {
                setCurrentDateTitle(year, month);
                if (mOnCalendarChangeListener != null) {
                    mOnCalendarChangeListener.onPageChange(getCalendar(year, month, day), year, month, day);
                }
            }

            private Calendar getCalendar(int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                return calendar;
            }
        });
        Calendar calendar = Calendar.getInstance();
        setCurrentDateTitle(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }

    private void setDynamicOffscreenPageLimit() {
        mCalendarView.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int currentPosition = mCalendarView.getCurrentItem();

            @Override
            public void onPageSelected(int position) {
                //Log.e("direction", "position-->"+position);
                if (position > currentPosition) {
                    //右滑
                    //Log.e("direction", "right-->"+position);
                    currentPosition = position;
                    mOffscreenPageLimit++;
                } else if (position < currentPosition) {
                    //左滑
                    //Log.e("direction", "left-->"+position);
                    currentPosition = position;
                    mOffscreenPageLimit--;
                }
                //Log.d("onPageSelected", "mOffscreenPageLimit-->" + mOffscreenPageLimit);
                mCalendarView.setOffscreenPageLimit(Math.abs(mOffscreenPageLimit) + INITIAL_OFFSCREEN_PAGE_LIMIT);
            }
        });
    }


    public void setOnCalendarChangeListener(OnCalendarChangeListener listener) {
        this.mOnCalendarChangeListener = listener;
    }

    public void setCurrentDateTitle(int year, int month) {
        mTitleYear.setText(String.valueOf(year));
        mTitleMonth.setText(mFormatMonths[month]);
    }


    public boolean addEvent(int year, int month, int day, @ColorInt int color) {
        return mCalendarView.addEvent(year, month, day, color);
    }

    public boolean addEvent(int year, int month, int day, String eventId, @ColorInt int color) {
        return mCalendarView.addEvent(year, month, day, eventId, color);
    }

    public boolean addEvent(int year, int month, int day, Event event) {
        return mCalendarView.addEvent(year, month, day, event);
    }

    public boolean addEvent(Event event) {
        return mCalendarView.addEvent(event);
    }


    /**
     * 杀出整年的event
     *
     * @param year
     * @return
     */
    public boolean removeYearEvent(int year) {
        return mCalendarView.removeYearEvent(year);
    }


    /**
     * 删除整月的event
     *
     * @param year
     * @param month 从0开始
     */
    public boolean removeMonthEvent(int year, int month) {
        return mCalendarView.removeMonthEvent(year, month);
    }

    /**
     * 删除整天的event
     *
     * @param year
     * @param month 从0开始
     * @param day
     */
    public boolean removeDayEvent(int year, int month, int day) {
        return mCalendarView.removeDayEvent(year, month, day);
    }

    /**
     * 删除单个event
     *
     * @param eventId
     */
    public boolean removeEvent(int year, int month, int day, String eventId) {
        return mCalendarView.removeEvent(year, month, day, eventId);
    }


    public boolean removeEvent(Event event) {
        return mCalendarView.removeEvent(event);
    }


    public void setYearEvent(YearEvent yearEvent) {
        mCalendarView.setYearEvent(yearEvent);
    }

    public void setMonthEvent(int year, MonthEvent monthEvent) {
        mCalendarView.setMonthEvent(year, monthEvent);
    }

    /**
     * 清除所有event
     */
    public void clearAllEvent() {
        mCalendarView.clearAllEvent();
    }


    public int getRowSize() {
        return getCurrentMonthView().getRowSize();
    }


    public Calendar getSelectDate() {
        return mCalendarView.getSelectDate();
    }

    public int getSelectDay() {
        return mCalendarView.getSelectDay();
    }

    public int getSelectMonth() {
        return mCalendarView.getSelectMonth();
    }

    public int getSelectYear() {
        return mCalendarView.getSelectYear();
    }

    public int getRealSelectDay() {
        return mCalendarView.getRealSelectDay();
    }

    public MonthView getCurrentMonthView() {
        return mCalendarView.getCurrentMonthView();
    }


    public SparseArray<YearEvent> getEvents() {
        return mCalendarView.getEvents();
    }


    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mCalendarView.addOnPageChangeListener(listener);
    }

    public MonthCalendarView getCalendarView() {
        return mCalendarView;
    }
}
