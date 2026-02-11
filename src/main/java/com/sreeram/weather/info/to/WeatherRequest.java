package com.sreeram.weather.info.to;

import java.util.Date;

public class WeatherRequest {
    private Long pincode;
    private Date forDate;

    public Long getPincode() {
        return pincode;
    }

    public Date getForDate() {
        return forDate;
    }
}
