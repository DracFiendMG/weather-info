package com.sreeram.weather.info.util;

import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.to.WeatherTO;

public class WeatherUtils {
    public static WeatherTO transform(WeatherDetailsBO weatherDetailsBO) {
        WeatherTO weather = new WeatherTO();
        weather.setDescription(weatherDetailsBO.getWeather().getFirst().getDescription());
        weather.setHumidity(weatherDetailsBO.getMain().getHumidity());
        weather.setIcon(weatherDetailsBO.getWeather().getFirst().getIcon());
        weather.setId(weatherDetailsBO.getWeather().getFirst().getId());
        weather.setMain(weatherDetailsBO.getWeather().getFirst().getMain());
        weather.setPressure(weatherDetailsBO.getMain().getPressure());
        weather.setSeaLevel(weatherDetailsBO.getMain().getSeaLevel());
        weather.setTemp(weatherDetailsBO.getMain().getTemp());
        weather.setTempMin(weatherDetailsBO.getMain().getTempMin());
        weather.setTempMax(weatherDetailsBO.getMain().getTempMax());

        return weather;
    }
}
