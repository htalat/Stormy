package com.example.hassan.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hassan on 8/27/15.
 */
public class Hour implements Parcelable{
    private  long mTime;
    private  String mSummary;
    private  double mTemperature;
    private String mIcon;
    private String mTimezone;

    public  Hour()
    {

    }
    private Hour(Parcel p)
    {
        mTime = p.readLong();
        mTemperature = p.readDouble();
        mSummary = p.readString();
        mTimezone = p.readString();
        mIcon = p.readString();
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

    public int getTemperature() {
        return (int)Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }
    public int getIconId()
    {
        return Forecast.getIconId(mIcon);
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

    public  String getHour()
    {
        SimpleDateFormat f = new SimpleDateFormat("h a ");
        Date date = new Date(mTime*1000);
        return f.format(date);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeString(mSummary);
        dest.writeString(mTimezone);
        dest.writeString(mIcon);
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
