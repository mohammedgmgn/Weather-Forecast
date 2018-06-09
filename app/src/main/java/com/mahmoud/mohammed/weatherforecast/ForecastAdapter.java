package com.mahmoud.mohammed.weatherforecast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoud.mohammed.weatherforecast.model.WeatherModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by siko on 6/25/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    List<WeatherModel> WeatherList;
    Context context;
    private static RecyclerViewClickListener itemListener;

    public ForecastAdapter(List<WeatherModel> weatherdatalist, Context context, RecyclerViewClickListener listener) {
        this.WeatherList = weatherdatalist;
        this.context = context;
        this.itemListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TODAY) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast_today, parent, false);
            return new TodayWeatherholder(view);

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.forecast_list_item, parent, false);
            return new Weatherholder(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        WeatherModel ModelWeatherData = WeatherList.get(position);
        int high = (int) ModelWeatherData.getMax();
        int low = (int) ModelWeatherData.getMin();
        String description = ModelWeatherData.getDescription();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Calendar c = Calendar.getInstance();
        if (viewType == VIEW_TYPE_TODAY) {
            TodayWeatherholder todayWeatherholder = (TodayWeatherholder) (holder);
            String month = String.format(Locale.US, "%tB", c);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            todayWeatherholder.dateView.setText("Today" + "," + " " + month + " " + dayOfMonth);
            todayWeatherholder.art_clouds.setImageResource(R.drawable.art_clouds);
            todayWeatherholder.high_temperature.setText(String.valueOf(high));
            todayWeatherholder.low_temperature.setText(String.valueOf(low));
            todayWeatherholder.weather_description.setText(description);

        } else if (viewType == VIEW_TYPE_FUTURE_DAY) {
            Weatherholder normalholder = (Weatherholder) (holder);
            normalholder.descriptionView.setText(description);
            normalholder.highTempView.setText(String.valueOf(high));
            normalholder.lowTempView.setText(String.valueOf(low));
            normalholder.iconView.setImageResource(R.drawable.art_clouds);
            // for making  days sorted ascending
            for (int i = 1; i <= 6; i++) {
                if (position == i) {
                    c.add(Calendar.DAY_OF_YEAR, i);
                }
            }
            String nameofDay = sdf.format(c.getTime());
            String month = String.format(Locale.US, "%tB", c);
            normalholder.dateView.setText(nameofDay + "," + " " + month);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }


    @Override
    public int getItemCount() {
        if (null == WeatherList) return 0;
        return WeatherList.size();
    }

    public class Weatherholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconView;
        TextView dateView;
        TextView descriptionView;
        TextView highTempView;
        TextView lowTempView;

        public Weatherholder(View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getLayoutPosition());

        }
    }

    public class TodayWeatherholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateView;
        ImageView art_clouds;
        TextView high_temperature;
        TextView low_temperature;
        TextView weather_description;

        public TodayWeatherholder(View view) {
            super(view);
            dateView = (TextView) view.findViewById(R.id.datetoday);
            high_temperature = (TextView) view.findViewById(R.id.high_temperature);
            low_temperature = (TextView) view.findViewById(R.id.low_temperature);
            weather_description = (TextView) view.findViewById(R.id.weather_description);
            art_clouds = (ImageView) view.findViewById(R.id.weather_icon);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getLayoutPosition());

        }
    }


    public interface RecyclerViewClickListener {

        void recyclerViewListClicked(View v, int position);
    }

}
