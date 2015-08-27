package com.example.hassan.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.stormy.R;
import com.example.hassan.stormy.weather.Current;
import com.example.hassan.stormy.weather.Day;
import com.example.hassan.stormy.weather.Forecast;
import com.example.hassan.stormy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Forecast mForecast;


    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refreshImageView)ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    public static final String TAG = MainActivity.class.getSimpleName();
    public static String DAILY_FORECAST="DAILY_FORECAST";
    public static String HOURLY_FORECAST="HOURLY_FORECAST";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        final double latitude = 37.8267;
        final double longitude = -122.423;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });

        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude,double longitude) {
        String apiKey = "3b70cbc6f2b9a698e601b6c0dcbe9532";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if(isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();

            final Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            toggleRefresh();
                        }
                    });
                    try {

                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {

                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });


                        } else {
                            alertUserAboutError();
                        }


                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception found: ", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG,"Exception found",e);
                    }
                }
            });
        }else
        {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay()
    {
        Current current = mForecast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature()+ "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        mIconImageView.setImageResource(current.getIconID());
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException
    {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));



        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException
    {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        Day[] dailyData = new Day[data.length()];

        for (int i=0;i<dailyData.length;i++)
        {
            JSONObject jo = data.getJSONObject(i);
            Day d = new Day();
            d.setTime(jo.getLong("time"));
            d.setSummary(jo.getString("summary"));
            d.setTemperatureMax(jo.getDouble("temperatureMax"));
            d.setIcon(jo.getString("icon"));
            d.setTimezone(timezone);

            dailyData[i] = d;
        }

        return dailyData;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException
    {

        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");
        Hour[] hourlyData = new Hour[data.length()];
        JSONObject jo = new JSONObject();
        for (int i = 0 ;i < data.length() ; i++)
        {
            Hour h = new Hour();
            jo = data.getJSONObject(i);
            h.setTime(jo.getLong("time"));
            h.setTemperature(jo.getDouble("temperature"));
            h.setSummary(jo.getString("summary"));
            h.setIcon(jo.getString("icon"));
            h.setTimezone(timezone);

            hourlyData[i] = h;
        }

        return hourlyData;
    }


    private Current getCurrentDetails(String jsonData) throws JSONException
    {

        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTimeZone(forecast.getString("timezone"));

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected())
        {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError()
    {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View v)
    {
        Intent intent = new Intent(this,DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick (R.id.hourlyButton)
    public void startHourly(View v)
    {
        Intent intent = new Intent(this,HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST,mForecast.getHourlyForecast());
        startActivity(intent);
    }
}
