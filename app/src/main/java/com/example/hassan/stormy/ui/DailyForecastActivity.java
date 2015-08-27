package com.example.hassan.stormy.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.stormy.R;
import com.example.hassan.stormy.adapters.DayAdapter;
import com.example.hassan.stormy.weather.Day;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DailyForecastActivity extends Activity {

    private Day[] mDays;
    @Bind(android.R.id.list) ListView mListView;
    @Bind(android.R.id.empty) TextView mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables,parcelables.length,Day[].class);
        DayAdapter dayAdapter = new DayAdapter(this,mDays);
        mListView.setAdapter(dayAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String day = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String tmp = mDays[position].getTemperatureMax()+"";
                String msg = String.format("On %s the high will be %s and it will be %s",day,tmp,conditions);
                Toast.makeText(DailyForecastActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
       }


}
