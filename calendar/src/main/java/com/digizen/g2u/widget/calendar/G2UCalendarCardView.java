package com.digizen.g2u.widget.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeek.calendar.library.R;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.month.MonthCalendarView;
import com.jeek.calendar.widget.calendar.schedule.Event;

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
            }
        });
        mCalendarView.setOnCalendarClickListener(new OnCalendarClickListener() {
            @Override
            public void onClickDate(int year, int month, int day) {

            }

            @Override
            public void onPageChange(int year, int month, int day) {
                setCurrentDateTitle(year, month);
            }
        });
        Calendar calendar = Calendar.getInstance();
        setCurrentDateTitle(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }


    public void setCurrentDateTitle(int year, int month) {
        mTitleYear.setText(String.valueOf(year));
        mTitleMonth.setText(mFormatMonths[month]);
    }

    public void addEvent(Event event) {
        mCalendarView.addEvent(event);
    }

}
