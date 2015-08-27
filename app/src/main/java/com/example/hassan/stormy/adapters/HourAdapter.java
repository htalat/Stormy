package com.example.hassan.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.stormy.R;
import com.example.hassan.stormy.weather.Hour;

/**
 * Created by hassan on 8/27/15.
 */
public class HourAdapter extends RecyclerView.Adapter <HourAdapter.HourViewHolder>{

    private  Hour[] mHours;
    private Context mContext;

    public HourAdapter( Context c,Hour[] h)
    {
        mHours = h;
        mContext = c;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hourly_list_item,viewGroup,false);
        HourViewHolder hvh = new HourViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i)
    {
        hourViewHolder.bindHour(mHours[i]);

    }

    @Override
    public int getItemCount()
    {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;


        public HourViewHolder(View itemView)
        {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour)
        {
            mTimeLabel.setText(hour.getHour());
            mSummaryLabel.setText(hour.getSummary());
            mTemperatureLabel.setText(hour.getTemperature() + "");
            mIconImageView.setImageResource(hour.getIconId());

        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String msg = String.format("On %s the high will be %s and it will be %s",time,temperature,summary);
            Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
        }
    }
}
