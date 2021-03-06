package com.jeek.calendar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.digizen.g2u.widget.calendar.G2UCalendarCardView;
import com.jeek.calendar.R;
import com.jeek.calendar.widget.calendar.CalendarUtils;
import com.jeek.calendar.widget.calendar.LunarCalendarUtils;
import com.jeek.calendar.widget.calendar.OnCalendarChangeListener;
import com.jeek.calendar.widget.calendar.bean.Lunar;
import com.jeek.calendar.widget.calendar.bean.Solar;
import com.jeek.calendar.widget.calendar.schedule.event.DayEvent;
import com.jeek.calendar.widget.calendar.schedule.event.Event;
import com.jeek.calendar.widget.calendar.schedule.event.MonthEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * author  dengyuhan
 * created 2017/6/14 18:02
 */
public class G2UActivity extends AppCompatActivity {
    RecyclerView rv;
    EditText ed_delete_year;
    EditText ed_delete_month_year;
    EditText ed_delete_month;
    EditText ed_delete_day_year;
    EditText ed_delete_day_month;
    EditText ed_delete_day;
    EditText ed_delete_single_year;
    EditText ed_delete_single_month;
    EditText ed_delete_single_day;
    G2UCalendarCardView calendar;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g2u);
        textView = (TextView) findViewById(R.id.tv_ganzhi);
        initView();
        //load();
        calendar.setOnCalendarChangeListener(new OnCalendarChangeListener() {
            @Override
            public void onClickDate(Calendar date, int year, int month, int day) {
                Lunar lunar = LunarCalendarUtils.solarToLunar(new Solar(year, month, day));
                String ganZhi = LunarCalendarUtils.lunarYearToGanZhi(lunar.lunarYear);
                Log.d(G2UCalendarCardView.class.getSimpleName(), "onClickDate--->" + year + "-" + month + "-" + day + "----格式化时间-->" + new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()) + "---真实--->" + calendar.getRealSelectDay());
                textView.setText(ganZhi);
            }

            @Override
            public void onPageChange(Calendar date, int year, int month, int day) {
                Log.d(G2UCalendarCardView.class.getSimpleName(), "onPageChange--->" + year + "-" + month + "-" + day + "----格式化时间-->" + new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()) + "---真实--->" + calendar.getRealSelectDay());
            }

        });
        calendar.setCalendarToday();
        //calendar.clearEvent();

    }

    private void initView() {
        calendar = (G2UCalendarCardView) findViewById(R.id.calendar);
        ed_delete_year = (EditText) findViewById(R.id.ed_delete_year);
        ed_delete_month_year = (EditText) findViewById(R.id.ed_delete_month_year);
        ed_delete_month = (EditText) findViewById(R.id.ed_delete_month);
        ed_delete_day_year = (EditText) findViewById(R.id.ed_delete_day_year);
        ed_delete_day_month = (EditText) findViewById(R.id.ed_delete_day_month);
        ed_delete_day = (EditText) findViewById(R.id.ed_delete_day);
        ed_delete_single_year = (EditText) findViewById(R.id.ed_delete_single_year);
        ed_delete_single_month = (EditText) findViewById(R.id.ed_delete_single_month);
        ed_delete_single_day = (EditText) findViewById(R.id.ed_delete_single_day);
        //
        Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH) + 1;
        int day = current.get(Calendar.DAY_OF_MONTH);

        ed_delete_year.setHint(String.valueOf(year));

        ed_delete_month_year.setHint(String.valueOf(year));
        ed_delete_month.setHint(String.valueOf(month));

        ed_delete_day_year.setHint(String.valueOf(year));
        ed_delete_day_month.setHint(String.valueOf(month));
        ed_delete_day.setHint(String.valueOf(day));

        ed_delete_single_year.setHint(String.valueOf(year));
        ed_delete_single_month.setHint(String.valueOf(month));
        ed_delete_single_day.setHint(String.valueOf(day));

    }

    public void clickRandomAddEvent(View v) {
        Random random = new Random();
        int year = calendar.getSelectYear();
        int month = calendar.getSelectMonth();
        int day = random.nextInt(30) + 1;
        int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        calendar.addEvent(year, month, day, color);
    }

    public void clickDeleteYearEvent(View v) {
        String yearText = ed_delete_year.getText().toString();
        int year = Integer.parseInt(TextUtils.isEmpty(yearText) ? ed_delete_year.getHint().toString() : yearText);
        calendar.removeYearEvent(year);
    }

    public void clickDeleteMonthEvent(View v) {
        String yearText = ed_delete_month_year.getText().toString();
        int year = Integer.parseInt(TextUtils.isEmpty(yearText) ? ed_delete_month_year.getHint().toString() : yearText);
        String monthText = ed_delete_month.getText().toString();
        int month = Integer.parseInt(TextUtils.isEmpty(monthText) ? ed_delete_month.getHint().toString() : monthText);
        calendar.removeMonthEvent(year, month - 1);
    }

    public void clickDeleteDayEvent(View v) {
        String yearText = ed_delete_day_year.getText().toString();
        int year = Integer.parseInt(TextUtils.isEmpty(yearText) ? ed_delete_day_year.getHint().toString() : yearText);
        String monthText = ed_delete_day_month.getText().toString();
        int month = Integer.parseInt(TextUtils.isEmpty(monthText) ? ed_delete_day_month.getHint().toString() : monthText);
        String dayText = ed_delete_day.getText().toString();
        int day = Integer.parseInt(TextUtils.isEmpty(dayText) ? ed_delete_day.getHint().toString() : dayText);
        calendar.removeDayEvent(year, month - 1, day);
    }

    public void clickDeleteSingleEvent(View v) {
        try {
            String yearText = ed_delete_single_year.getText().toString();
            int year = Integer.parseInt(TextUtils.isEmpty(yearText) ? ed_delete_single_year.getHint().toString() : yearText);
            String monthText = ed_delete_single_month.getText().toString();
            int month = Integer.parseInt(TextUtils.isEmpty(monthText) ? ed_delete_single_month.getHint().toString() : monthText);
            String dayText = ed_delete_single_day.getText().toString();
            int day = Integer.parseInt(TextUtils.isEmpty(dayText) ? ed_delete_single_day.getHint().toString() : dayText);

            DayEvent event = calendar.getEvents().get(year).getMonthEvents().get(month - 1).getDayEvents().get(day);
            calendar.removeEvent(event.getEvents().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickClearEvent(View v) {
        calendar.clearAllEvent();
    }

    public void clickAddMonthEvent(View v) {
        MonthEvent monthEvent = new MonthEvent();
        SparseArray<DayEvent> dayEvents = new SparseArray<>();
        int year = calendar.getSelectYear();
        int month = calendar.getSelectMonth();
        int days = CalendarUtils.getMonthDays(year, month);
        Random random=new Random();
        for (int day = 1; day <= days; day++) {
            DayEvent dayEvent = dayEvents.get(day);
            if (dayEvent == null) {
                dayEvent = new DayEvent(day, new ArrayList<Event>());
                dayEvents.put(dayEvent.getDay(), dayEvent);
            }
            Calendar date = Calendar.getInstance();
            date.set(Calendar.YEAR,year);
            date.set(Calendar.MONTH,month);
            date.set(Calendar.DAY_OF_MONTH,day);
            for (int i = 0; i < random.nextInt(5)+1; i++) {
                dayEvent.getEvents().add(new Event(date, Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255))));
            }
        }
        monthEvent.setMonth(month);
        monthEvent.setDayEvents(dayEvents);
        calendar.setMonthEvent(year, monthEvent);
    }

    public void clickAddRedEvent(View v) {
        int year = calendar.getSelectYear();
        int month = calendar.getSelectMonth();
        Calendar current = Calendar.getInstance();
        int day = current.get(Calendar.DAY_OF_MONTH);
        calendar.addEvent(year, month, day, Color.RED);
    }

    public void clickAddGreenEvent(View v) {
        int year = calendar.getSelectYear();
        int month = calendar.getSelectMonth();
        Calendar current = Calendar.getInstance();
        int day = current.get(Calendar.DAY_OF_MONTH);
        calendar.addEvent(year, month, day, Color.GREEN);
    }

    public void clickAddBlueEvent(View v) {
        int year = calendar.getSelectYear();
        int month = calendar.getSelectMonth();
        Calendar current = Calendar.getInstance();
        int day = current.get(Calendar.DAY_OF_MONTH);
        calendar.addEvent(year, month, day, Color.BLUE);
    }


    private void load() {
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("item " + i);
        }
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView tv = (TextView) holder.itemView.findViewById(android.R.id.text1);
                tv.setText(data.get(position));
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });
    }
}
