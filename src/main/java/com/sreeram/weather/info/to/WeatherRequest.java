package com.sreeram.weather.info.to;

import java.util.Date;

public class WeatherRequest {
    private String pincode;
    private Date forDate;

    public String getPincode() {
        return pincode;
    }

    public Date getForDate() {
        return forDate;
    }
}
