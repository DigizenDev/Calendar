package com.jeek.calendar.widget.calendar.month;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jeek.calendar.library.R;
import com.jeek.calendar.widget.calendar.CalendarUtils;
import com.jeek.calendar.widget.calendar.LunarCalendarUtils;
import com.jeek.calendar.widget.calendar.bean.Lunar;
import com.jeek.calendar.widget.calendar.bean.Solar;
import com.jeek.calendar.widget.calendar.schedule.event.DayEvent;
import com.jeek.calendar.widget.calendar.schedule.event.Event;
import com.jeek.calendar.widget.calendar.schedule.event.MonthEvent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/6 0006.
 */
public class MonthView extends View {

    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private static final int NUM_ROWS_MIN = 5;
    private static final int NOT_SELECTED_DAY = -1;//都不选中
    private static final int MAX_DRAW_EVENT_COUNT = 2;//最多花2个颜色的event
    private Paint mPaint;
    private Paint mLunarPaint;
    private int mNormalDayColor;
    private int mSelectDayColor;
    private int mSelectBGColor;
    private int mSelectBGTodayColor;
    private int mCurrentDayColor;
    private int mHintCircleColor;
    private int mLunarTextColor;
    private int mHolidayTextColor;
    private int mLastOrNextMonthTextColor;
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mCurrLunarYear, mCurrLunarMonth, mCurrLunarDay;
    private boolean mCurrLunarMonthLeap;//当前时间农历月是不是闰月
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize, mSelectCircleSize;
    private int mDaySize;
    private int mLunarTextSize;
    private int mWeekRow; // 当前月份第几周
    private int mCircleRadius = 6;
    private int[][] mDaysText;
    private int[] mHolidays;
    private String[][] mHolidayOrLunarText;
    private boolean mIsShowLunar;
    private boolean mIsShowHint;
    private boolean mIsShowHolidayHint;
    private DisplayMetrics mDisplayMetrics;
    private OnMonthClickListener mDateClickListener;
    private GestureDetector mGestureDetector;
    private MonthEvent mMonthEvent = new MonthEvent();
    private List<Integer> mTaskHintList;
    private Bitmap mRestBitmap, mWorkBitmap;

    private boolean mIsShowLast;//是否显示非这个月的

    public MonthView(Context context, int year, int month) {
        this(context, null, year, month);
    }

    public MonthView(Context context, TypedArray array, int year, int month) {
        this(context, array, null, year, month);
    }

    public MonthView(Context context, TypedArray array, AttributeSet attrs, int year, int month) {
        this(context, array, attrs, 0, year, month);
    }

    public MonthView(Context context, TypedArray array, AttributeSet attrs, int defStyleAttr, int year, int month) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
        initAttrs(array, year, month);
        initPaint();
        initMonth();
        initGestureDetector();
        initTaskHint();
    }

    private void initTaskHint() {
        if (mIsShowHint) {
            // 从数据库中获取圆点提示数据
            //ScheduleDao dao = ScheduleDao.getInstance(getContext());
            //mTaskHintList = dao.getTaskHintByMonth(mSelYear, mSelMonth);
        }
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    private void initAttrs(TypedArray array, int year, int month) {
        if (array != null) {
            mSelectDayColor = array.getColor(R.styleable.MonthCalendarView_month_selected_text_color, Color.parseColor("#FFFFFF"));
            mSelectBGColor = array.getColor(R.styleable.MonthCalendarView_month_selected_circle_color, Color.parseColor("#E8E8E8"));
            mSelectBGTodayColor = array.getColor(R.styleable.MonthCalendarView_month_selected_circle_today_color, Color.parseColor("#FF8594"));
            mNormalDayColor = array.getColor(R.styleable.MonthCalendarView_month_normal_text_color, Color.parseColor("#575471"));
            mCurrentDayColor = array.getColor(R.styleable.MonthCalendarView_month_today_text_color, Color.parseColor("#FF8594"));
            mHintCircleColor = array.getColor(R.styleable.MonthCalendarView_month_hint_circle_color, Color.parseColor("#FE8595"));
            mLastOrNextMonthTextColor = array.getColor(R.styleable.MonthCalendarView_month_last_or_next_month_text_color, Color.parseColor("#ACA9BC"));
            mLunarTextColor = array.getColor(R.styleable.MonthCalendarView_month_lunar_text_color, Color.parseColor("#ACA9BC"));
            mHolidayTextColor = array.getColor(R.styleable.MonthCalendarView_month_holiday_color, Color.parseColor("#A68BFF"));
            mDaySize = array.getInteger(R.styleable.MonthCalendarView_month_day_text_size, 13);
            mLunarTextSize = array.getInteger(R.styleable.MonthCalendarView_month_day_lunar_text_size, 8);
            mIsShowHint = array.getBoolean(R.styleable.MonthCalendarView_month_show_task_hint, true);
            mIsShowLunar = array.getBoolean(R.styleable.MonthCalendarView_month_show_lunar, true);
            mIsShowHolidayHint = array.getBoolean(R.styleable.MonthCalendarView_month_show_holiday_hint, true);
            mIsShowLast = array.getBoolean(R.styleable.MonthCalendarView_month_show_last, true);
        } else {
            mSelectDayColor = Color.parseColor("#FFFFFF");
            mSelectBGColor = Color.parseColor("#E8E8E8");
            mSelectBGTodayColor = Color.parseColor("#FF8594");
            mNormalDayColor = Color.parseColor("#575471");
            mCurrentDayColor = Color.parseColor("#FF8594");
            mHintCircleColor = Color.parseColor("#FE8595");
            mLastOrNextMonthTextColor = Color.parseColor("#ACA9BC");
            mHolidayTextColor = Color.parseColor("#A68BFF");
            mDaySize = 13;
            mLunarTextSize = 8;
            mIsShowHint = true;
            mIsShowLunar = true;
            mIsShowHolidayHint = true;
        }
        mSelYear = year;
        mSelMonth = month;
        mRestBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rest_day);
        mWorkBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_work_day);
        mHolidays = CalendarUtils.getInstance(getContext()).getHolidays(mSelYear, mSelMonth + 1);
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);

        mLunarPaint = new Paint();
        mLunarPaint.setAntiAlias(true);
        mLunarPaint.setTextSize(mLunarTextSize * mDisplayMetrics.scaledDensity);
        mLunarPaint.setColor(mLunarTextColor);
    }

    private void initMonth() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);

        Lunar lunar = LunarCalendarUtils.solarToLunar(new Solar(mCurrYear, mCurrMonth, mCurrDay));
        mCurrLunarYear = lunar.lunarYear;
        mCurrLunarMonth = lunar.lunarMonth;
        mCurrLunarDay = lunar.lunarDay;
        mCurrLunarMonthLeap = lunar.isLeap;

        /*
        if (mSelYear == mCurrYear && mSelMonth == mCurrMonth) {
            setSelectYearMonth(mSelYear, mSelMonth, mCurrDay);
        } else {
            setSelectYearMonth(mSelYear, mSelMonth, 1);
        }
        */
        //TODO 不默认选中当天
        setSelectYearMonth(mSelYear, mSelMonth, NOT_SELECTED_DAY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        clearData();
        drawLastMonth(canvas);
        int selected[] = drawThisMonth(canvas);
        drawNextMonth(canvas);
        drawLunarText(canvas, selected);
        drawHoliday(canvas);
    }

    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        //mRowSize = getHeight() / NUM_ROWS;
        mRowSize = getHeight() / getSelectNumberRows();
        //mSelectCircleSize = (int) (mColumnSize / 3.2);
        mSelectCircleSize = (int) (mColumnSize / 2.6);
        while (mSelectCircleSize > mRowSize / 2) {
            mSelectCircleSize = (int) (mSelectCircleSize / 1.3);
        }
    }

    private int getSelectNumberRows() {
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        int rows = weekNumber - 1 + getSelectDaysOfMonth() > NUM_ROWS_MIN * NUM_COLUMNS ? NUM_ROWS : NUM_ROWS_MIN;
        return rows;
    }


    private void clearData() {
        mDaysText = new int[6][7];
        mHolidayOrLunarText = new String[6][7];
    }

    private void drawLastMonth(Canvas canvas) {
        int lastYear, lastMonth;
        if (mSelMonth == 0) {
            lastYear = mSelYear - 1;
            lastMonth = 11;
        } else {
            lastYear = mSelYear;
            lastMonth = mSelMonth - 1;
        }
        mPaint.setColor(mLastOrNextMonthTextColor);
        int monthDays = CalendarUtils.getMonthDays(lastYear, lastMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < weekNumber - 1; day++) {
            mDaysText[0][day] = monthDays - weekNumber + day + 2;
            if (mIsShowLast) {
                String dayString = String.valueOf(mDaysText[0][day]);
                int startX = (int) (mColumnSize * day + (mColumnSize - mPaint.measureText(dayString)) / 2);
                int startY = (int) (mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
                canvas.drawText(dayString, startX, startY, mPaint);
            }
            mHolidayOrLunarText[0][day] = CalendarUtils.getHolidayFromSolar(lastYear, lastMonth, mDaysText[0][day]);
        }
    }

    private int[] drawThisMonth(Canvas canvas) {
        String dayString;
        int selectedPoint[] = new int[2];
        int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < monthDays; day++) {
            dayString = String.valueOf(day + 1);
            int col = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            mDaysText[row][col] = day + 1;
            int startX = (int) (mColumnSize * col + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            if (dayString.equals(String.valueOf(getSelectDay()))) {
                //TODO 如果需要显示选中 才画圈
                //Log.d("---------->", mSelDay + "--" + getSelectDay());
                if (mSelDay != NOT_SELECTED_DAY) {
                    int startRecX = mColumnSize * col;
                    int startRecY = mRowSize * row;
                    int endRecX = startRecX + mColumnSize;
                    int endRecY = startRecY + mRowSize;
                    if (mSelYear == mCurrYear && mCurrMonth == mSelMonth && day + 1 == mCurrDay) {
                        mPaint.setColor(mSelectBGTodayColor);
                    } else {
                        mPaint.setColor(mSelectBGColor);
                    }
                    //TODO 空心
                    Paint circle = new Paint(mPaint);
                    circle.setStyle(Paint.Style.STROKE);
                    circle.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.g2u_day_stroke_width));
                    int lunarMargin = getResources().getDimensionPixelSize(R.dimen.g2u_lunar_margin);
                    int circleOffset = getResources().getDimensionPixelSize(R.dimen.g2u_circle_offset);
                    canvas.drawCircle((startRecX + endRecX) / 2, (startRecY + endRecY) / 2 + lunarMargin + circleOffset, mSelectCircleSize, circle);
                }
                mWeekRow = row + 1;
            }
            drawHintCircle(row, col, day + 1, canvas);
            if (dayString.equals(String.valueOf(getSelectDay()))) {
                selectedPoint[0] = row;
                selectedPoint[1] = col;
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(String.valueOf(mCurrDay)) && mCurrDay != getSelectDay() && mCurrMonth == mSelMonth && mCurrYear == mSelYear) {
                mPaint.setColor(mCurrentDayColor);
            } else {
                mPaint.setColor(mNormalDayColor);
            }

            canvas.drawText(dayString, startX, startY, mPaint);

            mHolidayOrLunarText[row][col] = CalendarUtils.getHolidayFromSolar(mSelYear, mSelMonth, mDaysText[row][col]);
        }
        return selectedPoint;
    }

    private void drawNextMonth(Canvas canvas) {
        mPaint.setColor(mLastOrNextMonthTextColor);
        int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        int nextMonthDays = 42 - monthDays - weekNumber + 1;
        int nextMonth = mSelMonth + 1;
        int nextYear = mSelYear;
        if (nextMonth == 12) {
            nextMonth = 0;
            nextYear += 1;
        }
        for (int day = 0; day < nextMonthDays; day++) {
            int column = (monthDays + weekNumber - 1 + day) % 7;
            int row = 5 - (nextMonthDays - day - 1) / 7;
            try {
                mDaysText[row][column] = day + 1;
                mHolidayOrLunarText[row][column] = CalendarUtils.getHolidayFromSolar(nextYear, nextMonth, mDaysText[row][column]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mIsShowLast) {
                String dayString = String.valueOf(mDaysText[row][column]);
                int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
                int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
                canvas.drawText(dayString, startX, startY, mPaint);
            }
        }
    }

    /**
     * 绘制农历
     *
     * @param canvas
     * @param selected
     */
    private void drawLunarText(Canvas canvas, int[] selected) {
        if (mIsShowLunar) {
            int firstYear, firstMonth, firstDay;
            int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
            if (weekNumber == 1) {
                firstYear = mSelYear;
                firstMonth = mSelMonth + 1;
                firstDay = 1;
            } else {
                int monthDays;
                if (mSelMonth == 0) {
                    firstYear = mSelYear - 1;
                    firstMonth = 11;
                    monthDays = CalendarUtils.getMonthDays(firstYear, firstMonth);
                    firstMonth = 12;
                } else {
                    firstYear = mSelYear;
                    firstMonth = mSelMonth - 1;
                    monthDays = CalendarUtils.getMonthDays(firstYear, firstMonth);
                    firstMonth = mSelMonth;
                }
                firstDay = monthDays - weekNumber + 2;
            }
            Lunar lunar = LunarCalendarUtils.solarToLunar(new Solar(firstYear, firstMonth, firstDay));
            int days;
            int day = lunar.lunarDay;
            int leapMonth = LunarCalendarUtils.leapMonth(lunar.lunarYear);
            days = LunarCalendarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);

            for (int i = 0; i < 42; i++) {
                int column = i % 7;
                int row = i / 7;
                if (day > days) {
                    day = 1;
                    boolean isLast = lunar.lunarMonth == 12;
                    if (isLast) {
                        lunar.lunarMonth = 1;
                        lunar.lunarYear = lunar.lunarYear + 1;
                    }
                    if (lunar.lunarMonth == leapMonth && !lunar.isLeap) {
                        lunar.isLeap = true;
                        days = LunarCalendarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
                    } else if (!isLast) {
                        lunar.lunarMonth++;
                        days = LunarCalendarUtils.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
                    }
                }
                if (row == 0 && mDaysText[row][column] >= 23 || row >= 4 && mDaysText[row][column] <= 14) {
                    mLunarPaint.setColor(mLunarTextColor);
                } else {
                    mLunarPaint.setColor(mHolidayTextColor);
                }
                String dayString = mHolidayOrLunarText[row][column];
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getLunarHoliday(lunar.lunarYear, lunar.lunarMonth, day);
                }
                //TODO 节气
                Solar solar = LunarCalendarUtils.lunarToSolar(new Lunar(lunar.lunarYear, lunar.lunarMonth, day, lunar.isLeap));
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getSolarTerm(solar.solarYear, solar.solarMonth, solar.solarDay);
                }
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getLunarDayString(day);
                }

                boolean isDrawLunar = mIsShowLast;
                //TODO 如果是这个月的
                //Log.d("--------->", "公历 " + solar.solarYear + "-" + solar.solarMonth + "-" + solar.solarDay + " , 农历 " + lunar.lunarYear + "-" + lunar.lunarMonth + "-" + day + "-" + lunar.isLeap + " , 当前选择公历月-" + (mSelMonth + 1) + " , 当前年闰月-" + leapMonth + " " + days + "天");
                if (solar.solarMonth == mSelMonth + 1) {
                    mLunarPaint.setColor(mLunarTextColor);
                    if (!isDrawLunar) {
                        isDrawLunar = true;
                    }
                } else {
                    mLunarPaint.setColor(mLastOrNextMonthTextColor);
                }

                if (selected[0] == row && selected[1] == column) {
                    mLunarPaint.setColor(mSelectDayColor);
                }

                if (isDrawLunar) {
                    //TODO 间距
                    int lunarMargin = getResources().getDimensionPixelSize(R.dimen.g2u_lunar_margin);
                    float measureText = mLunarPaint.measureText(dayString);
                    int startX = (int) (mColumnSize * column + (mColumnSize - measureText) / 2);
                    int startY = (int) (mRowSize * row + mRowSize * 0.72 - (mLunarPaint.ascent() + mLunarPaint.descent()) / 2);
                    canvas.drawText(dayString, startX, startY + lunarMargin, mLunarPaint);

                    //TODO 下划线
                    //Log.d("-------->", lunar.lunarYear + "-" + lunar.lunarMonth + "-" + day + "," + mCurrLunarYear + "-" + (mCurrLunarMonth + 1) + "-" + (mCurrLunarDay+1));
                    if (mSelDay != mCurrDay && solar.solarYear == mCurrYear && (solar.solarMonth == mCurrMonth + 1 && solar.solarMonth == mSelMonth + 1) && solar.solarDay == mCurrDay) {
                        Paint underlinePaint = new Paint();
                        underlinePaint.setColor(mSelectBGColor);
                        int underlineMargin = getResources().getDimensionPixelSize(R.dimen.g2u_current_day_underline);
                        int underlineSize = getResources().getDimensionPixelSize(R.dimen.g2u_current_day_underline_size);
                        int underlineStartY = startY + underlineMargin + mLunarTextSize;
                        canvas.drawRect(startX, underlineStartY, startX + measureText, underlineStartY + underlineSize, underlinePaint);
                    }
                }

                day++;
            }
        }
    }

    private void drawHoliday(Canvas canvas) {
        if (mIsShowHolidayHint) {
            Rect rect = new Rect(0, 0, mRestBitmap.getWidth(), mRestBitmap.getHeight());
            Rect rectF = new Rect();
            int distance = (int) (mSelectCircleSize / 2.5);
            for (int i = 0; i < mHolidays.length; i++) {
                int column = i % 7;
                int row = i / 7;
                rectF.set(mColumnSize * (column + 1) - mRestBitmap.getWidth() - distance, mRowSize * row + distance, mColumnSize * (column + 1) - distance, mRowSize * row + mRestBitmap.getHeight() + distance);
                if (mHolidays[i] == 1) {
                    canvas.drawBitmap(mRestBitmap, rect, rectF, null);
                } else if (mHolidays[i] == 2) {
                    canvas.drawBitmap(mWorkBitmap, rect, rectF, null);
                }
            }
        }
    }

    /**
     * 绘制圆点提示
     *
     * @param column
     * @param day
     * @param canvas
     */
    private void drawHintCircle(int row, int column, int day, Canvas canvas) {
        if (day == mSelDay) {
            return;
        }
        SparseArray<DayEvent> dayEvents = mMonthEvent.getDayEvents();
        if (mIsShowHint && dayEvents != null && dayEvents.size() > 0) {
            //TODO 圆点数据如果没有这天
            if (dayEvents.indexOfKey(day) == -1) return;

            //当天的多个event
            DayEvent dayEvent = dayEvents.get(day);
            if (dayEvent == null) {
                return;
            }
            List<Event> events = dayEvent.getEvents();
            int maxDrawCount = getMaxDrawEventCount(events);
            int tmpColor = 0;
            int drawCount = 0;
            for (int i = 0; i < events.size(); i++) {
                if (drawCount >= maxDrawCount) {
                    break;
                }
                Event event = events.get(i);
                if (tmpColor == event.getColor()) {
                    continue;
                }
                tmpColor = event.getColor();
                mPaint.setColor(event.getColor());
                int circleMargin = getResources().getDimensionPixelSize(R.dimen.g2u_circle_margin);
                //float circleX = (float) (mColumnSize * column + mColumnSize * 0.5);
                //中心位置
                float circleX = (float) (mColumnSize * column + mColumnSize * 0.5);
                //float circleY = (float) (mRowSize * row + mRowSize * 0.75);
                float circleY = (float) (mRowSize * row + mRowSize * 0.25f);

                /*float centerX = circleX;
                if (drawCount > 1) {
                    if (drawCount % 2 == 0) {
                        int centerIndex = drawCount / 2 - 1;
                        Log.d("------->",(centerIndex - i + 1)+"");
                        int offset = (mCircleRadius * 2 + circleMargin) * (centerIndex - i + 1);
                        Log.d("-------->", "=====>" + centerIndex + " " + i);
                        circleX = i <= centerIndex ? circleX - offset-circleMargin/2 : circleX + offset;
                        *//*if (i % 2 == 0) {
//                            circleX = circleX - mCircleRadius * i - circleMargin;
                            int offset = mCircleRadius * 2 * x;
                            if (i == 0 | i == 1) offset /= 1.5;
                            circleX = centerX - offset;
                        } else {
                            int offset = mCircleRadius * 2 * x;
                            if (i == 0 | i == 1) offset /= 1.5;
                            circleX = centerX + offset;
//                            Log.i("-->", "drawHintCircle: " + mCircleRadius + "," + centerX + "," + circleMargin + "," + x);
                            x++;
                        }*//*

                        Log.i("-->", "drawHintCircle: " + mCircleRadius + "," + centerX + "," + circleX + "," + (circleX - centerX));
                    } else {
                        circleX = circleX - (mCircleRadius + circleMargin) * (i - 1);
                    }
                }*/

                if (maxDrawCount > 1) {
                    if (drawCount == 0) {
                        //往左偏移event个数的X值
                        //circleX = circleX - (circleMargin * (drawCount - 1) + mCircleRadius * 2);
                        circleX = circleX - mCircleRadius - circleMargin;
                    } else {
                        circleX = circleX + mCircleRadius + circleMargin;
                    }
                }

                //circleX = circleX + circleMargin * i + mCircleRadius * 2;
                canvas.drawCircle(circleX, circleY, mCircleRadius, mPaint);
                drawCount++;
            }
        }
    }

    private int getMaxDrawEventCount(List<Event> events) {
        int tmpColor = 0;
        int maxDrawCount = 0;
        for (Event e : events) {
            if (tmpColor == e.getColor()) {
                continue;
            }
            tmpColor = e.getColor();
            maxDrawCount++;
            if (maxDrawCount >= MAX_DRAW_EVENT_COUNT) {
                break;
            }
        }
        return maxDrawCount;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }


    private void doClickAction(int x, int y) {
        if (y > getHeight())
            return;
        int row = y / mRowSize;
        int column = x / mColumnSize;
        column = Math.min(column, 6);
        int clickYear = mSelYear, clickMonth = mSelMonth;
        if (row == 0) {
            if (mDaysText[row][column] >= 23) {
                if (mSelMonth == 0) {
                    clickYear = mSelYear - 1;
                    clickMonth = 11;
                } else {
                    clickYear = mSelYear;
                    clickMonth = mSelMonth - 1;
                }
                if (mDateClickListener != null && mIsShowLast) {
                    mDateClickListener.onClickLastMonth(clickYear, clickMonth, mDaysText[row][column]);
                }
            } else {
                clickThisMonth(clickYear, clickMonth, mDaysText[row][column]);
            }
        } else {
            int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
            int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
            int nextMonthDays = 42 - monthDays - weekNumber + 1;
            if (mDaysText[row][column] <= nextMonthDays && row >= 4) {
                if (mSelMonth == 11) {
                    clickYear = mSelYear + 1;
                    clickMonth = 0;
                } else {
                    clickYear = mSelYear;
                    clickMonth = mSelMonth + 1;
                }
                //TODO 如果不显示上一月就不能点击
                if (mDateClickListener != null && mIsShowLast) {
                    mDateClickListener.onClickNextMonth(clickYear, clickMonth, mDaysText[row][column]);
                }
            } else {
                clickThisMonth(clickYear, clickMonth, mDaysText[row][column]);
            }
        }
    }

    /**
     * 跳转到某日期
     *
     * @param year
     * @param month
     * @param day
     */
    public void clickThisMonth(int year, int month, int day) {
        setSelectYearMonth(year, month, day);
        invalidate();
        if (mDateClickListener != null) {
            mDateClickListener.onClickThisMonth(year, month, getSelectDay());
        }
    }

    /**
     * 获取当前选择年
     *
     * @return
     */
    public int getSelectYear() {
        return mSelYear;
    }

    /**
     * 获取当前选择月
     *
     * @return
     */
    public int getSelectMonth() {
        return mSelMonth;
    }

    /**
     * 获取当前选择日
     *
     * @return 最少1号
     */
    public int getSelectDay() {
        return Math.max(this.mSelDay, 1);
    }

    /**
     * 获取真实选择日
     *
     * @return 有可能为-1,-1则不圈任何天
     */
    public int getRealSelectDay() {
        return this.mSelDay;
    }

    public int getRowSize() {
        return mRowSize;
    }

    public int getWeekRow() {
        return mWeekRow;
    }

    /**
     * 设置圆点提示的集合
     *
     * @param taskHintList
     */
    public void setTaskHintList(List<Integer> taskHintList) {
        mTaskHintList = taskHintList;
        invalidate();
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    public void addTaskHint(Integer day) {
        if (mTaskHintList != null) {
            if (!mTaskHintList.contains(day)) {
                mTaskHintList.add(day);
                invalidate();
            }
        }
    }

    /**
     * 删除一个圆点提示
     *
     * @param day
     */
    public void removeTaskHint(Integer day) {
        if (mTaskHintList != null) {
            if (mTaskHintList.remove(day)) {
                invalidate();
            }
        }
    }

    public void setEventList(MonthEvent monthEvent) {
        this.mMonthEvent = monthEvent == null ? new MonthEvent() : monthEvent;
        invalidate();
    }

    /**
     * 设置点击日期监听
     *
     * @param dateClickListener
     */
    public void setOnDateClickListener(OnMonthClickListener dateClickListener) {
        this.mDateClickListener = dateClickListener;
    }


    /**
     * 当前月有多少天
     *
     * @return
     */
    public int getSelectDaysOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mSelYear);
        calendar.set(Calendar.MONTH, mSelMonth);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}

