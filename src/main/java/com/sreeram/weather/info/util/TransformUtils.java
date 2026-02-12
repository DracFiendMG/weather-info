package com.sreeram.weather.info.util;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.to.CoordinatesTO;
import com.sreeram.weather.info.to.WeatherTO;

import java.util.Date;

public class TransformUtils {
    public static WeatherTO transform(String pincode, Date forDate, WeatherDetailsBO weatherDetailsBO) {
        WeatherTO weather = new WeatherTO();
        weather.setPincode(pincode);
        weather.setForDate(forDate);
        weather.setDescription(weatherDetailsBO.getWeather().getFirst().getDescription());
        weather.setHumidity(weatherDetailsBO.getMain().getHumidity());
        weather.setIcon(weatherDetailsBO.getWeather().getFirst().getIcon());
        weather.setMain(weatherDetailsBO.getWeather().getFirst().getMain());
        weather.setPressure(weatherDetailsBO.getMain().getPressure());
        weather.setSeaLevel(weatherDetailsBO.getMain().getSeaLevel());
        weather.setTemp(weatherDetailsBO.getMain().getTemp());
        weather.setTempMin(weatherDetailsBO.getMain().getTempMin());
        weather.setTempMax(weatherDetailsBO.getMain().getTempMax());

        return weather;
    }

    public static CoordinatesTO transform(String pincode, Date forDate, CoordinatesBO coordinatesBO) {
        CoordinatesTO coordinates = new CoordinatesTO();
        coordinates.setLatitude(coordinatesBO.getLat());
        coordinates.setLongitude(coordinatesBO.getLon());
        coordinates.setPincode(pincode);
        coordinates.setForDate(forDate);

        return coordinates;
    }
}
