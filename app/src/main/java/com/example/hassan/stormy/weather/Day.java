package com.example.hassan.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hassan on 8/27/15.
 */
public class Day implements Parcelable {
    private long mTime;
    private String mSummary;
    private double mTemperatureMax;
    private String mIcon;
    private  String  mTimezone;

    public Day()
    {

    }

    private Day(Parcel in)
    {
        mTime = in.readLong();
        mSummary= in.readString();
        mIcon= in.readString();
        mTimezone = in.readString();
        mTemperatureMax = in.readDouble();
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId()
    {
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek()
    {
        SimpleDateFormat f = new SimpleDateFormat("EEEE");
        f.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date date = new Date(mTime*1000);
        return f.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeString(mIcon);
        dest.writeString(mTimezone);
        dest.writeDouble(mTemperatureMax);
    }


    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };


}
