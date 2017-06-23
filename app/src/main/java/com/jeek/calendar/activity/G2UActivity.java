package com.jeek.calendar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digizen.g2u.widget.calendar.G2UCalendarCardView;
import com.jeek.calendar.R;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.schedule.event.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * author  dengyuhan
 * created 2017/6/14 18:02
 */
public class G2UActivity extends AppCompatActivity {
    RecyclerView rv;
    G2UCalendarCardView calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g2u);

        calendar = (G2UCalendarCardView) findViewById(R.id.calendar);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.addEvent(new Event(dateFormat.parse("2017-06-06"), Color.RED));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-06"), Color.GRAY));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-06"), Color.BLACK));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-06"), Color.BLUE));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-06"), Color.BLACK));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-06"), Color.BLUE));

            calendar.addEvent(new Event(dateFormat.parse("2017-06-07"), Color.BLACK));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-07"), Color.RED));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-07"), Color.RED));

            calendar.addEvent(new Event(dateFormat.parse("2017-06-08"), Color.BLUE));
            calendar.addEvent(new Event(dateFormat.parse("2017-06-08"), Color.BLACK));

            calendar.addEvent(new Event(dateFormat.parse("2017-06-09"), Color.GREEN));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv = (RecyclerView) findViewById(R.id.rvScheduleList);
        load();
        calendar.setOnCalendarClickListener(new OnCalendarClickListener() {
            @Override
            public void onClickDate(int year, int month, int day) {
                Log.d(G2UCalendarCardView.class.getSimpleName(), "onClickDate--->"+year + "-" + month + "-" + day + "----格式化时间-->" + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getSelectDate().getTime())+"---真实--->"+calendar.getRealSelectDay());
            }

            @Override
            public void onPageChange(int year, int month, int day) {
                Log.d(G2UCalendarCardView.class.getSimpleName(), "onPageChange--->"+year + "-" + month + "-" + day + "----格式化时间-->" + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getSelectDate().getTime())+"---真实--->"+calendar.getRealSelectDay());
            }
        });
        //calendar.clearEvent();

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
