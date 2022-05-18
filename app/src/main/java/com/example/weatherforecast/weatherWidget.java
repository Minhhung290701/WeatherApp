package com.example.weatherforecast;


import android.app.PendingIntent;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.widget.RemoteViews;
import android.content.ComponentName;

import androidx.annotation.NonNull;

import com.example.weatherforecast.common.Common;
import com.example.weatherforecast.model.WeatherResponse;
import com.example.weatherforecast.retrofitclient.RetrofitClient;
import com.example.weatherforecast.retrofitclient.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class weatherWidget extends AppWidgetProvider {

    String temperatureString,  cityName, tempDay, tempNight;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            Retrofit retrofit = RetrofitClient.getInstance();
            WeatherService weatherService = retrofit.create(WeatherService.class);
            Call<WeatherResponse> call = weatherService.getWeatherByLatLon(Common.latitude, Common.longitude, Common.API_KEY_ID, "metric");
            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                    if (response.code() == 200) {
                        WeatherResponse weatherResponse = response.body();
                        assert weatherResponse != null;
                        int temp = (int) Math.round(weatherResponse.getMain().getTemp());
                        temperatureString = temp + "°C";
                        //tempDay = Math.round(weatherResponse.getDaily().get(0).getTemp().getDay()) + "°C";
                        //tempNight = Math.round(weatherResponse.getDaily().get(0).getTemp().getNight()) + "°C";
                        cityName = weatherResponse.getName();

                        update(context,temperatureString, tempNight, tempDay,cityName,appWidgetManager,appWidgetIds);
                        System.out.println(cityName);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<WeatherResponse> call, Throwable t) {
                }
            });
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.weather_icon_widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void update(Context context,String tv_temp, String night_temp, String day_temp, String cityName, AppWidgetManager appWidgetManager, int[] ids) {


        for (int id : ids) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

            updateRemoteViews(remoteViews, tv_temp, night_temp, day_temp,cityName);

            PendingIntent pendingIntent = getPendingIntent(context, ids);
            remoteViews.setOnClickPendingIntent(R.id.reloadwidget, pendingIntent);
            appWidgetManager.updateAppWidget(new ComponentName(context, weatherWidget.class), remoteViews);
        }

    }

    private void updateRemoteViews(RemoteViews remoteViews, String tv_temp,String night_temp, String day_temp, String cityName) {

        remoteViews.setTextViewText(R.id.tv_temp_widget, tv_temp);
        remoteViews.setTextViewText(R.id.night_temp_widget, night_temp);
        remoteViews.setTextViewText(R.id.day_temp_widget, day_temp);
        remoteViews.setTextViewText(R.id.txt_city_widget,cityName);
    }

    private PendingIntent getPendingIntent(Context context, int[] ids) {
        Intent intent = new Intent(context, weatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}