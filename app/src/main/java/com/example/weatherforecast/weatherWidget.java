package com.example.weatherforecast;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.weatherforecast.adapter.DailyWeatherAdapter;
import com.example.weatherforecast.adapter.WeatherForecastAdapter;
import com.example.weatherforecast.common.Common;
import com.example.weatherforecast.databasehelper.DBAccess;
import com.example.weatherforecast.model.Coord;
import com.example.weatherforecast.model.Sys;
import com.example.weatherforecast.model.WeatherForecastResponse;
import com.example.weatherforecast.model.WeatherResponse;
import com.example.weatherforecast.retrofitclient.RetrofitClient;
import com.example.weatherforecast.retrofitclient.WeatherService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.util.Date;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class weatherWidget extends AppWidgetProvider {

    String temperatureString,  cityName, coord;
    String lat = Common.latitude, lon = Common.longitude;

    private void getWeatherInfo(String lat, String lon) {
        Retrofit retrofit = RetrofitClient.getInstance();
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = weatherService.getWeatherByLatLon(lat, lon, Common.API_KEY_ID, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    int temp = (int) Math.round(weatherResponse.getMain().getTemp());
                    temperatureString = temp + "°C";
                    cityName = weatherResponse.getName();
                }
            }
            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, Throwable t) {
                cityName = "Khong co";
            }
        });
    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//Retrieve the current time//

        String timeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.id_value, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.update_value,
                context.getResources().getString(
                        R.string.time, timeString));

//Create an Intent with the AppWidgetManager.ACTION_APPWIDGET_UPDATE action//

        Intent intentUpdate = new Intent(context, NewAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

//Update the current widget instance only, by creating an array that contains the widget’s unique ID//

        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

//Wrap the intent as a PendingIntent, using PendingIntent.getBroadcast()//

        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);

//Send the pending intent in response to the user tapping the ‘Update’ TextView//

        views.setOnClickPendingIntent(R.id.update, pendingUpdate);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://code.tutsplus.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.launch_url, pendingIntent);

//Request that the AppWidgetManager updates the application widget//

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            getWeatherInfo(lat,lon);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.weather_icon_widget, pendingIntent);
            views.setTextViewText(R.id.tv_temp_widget,temperatureString);
            views.setTextViewText(R.id.txt_city_widget,cityName);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}