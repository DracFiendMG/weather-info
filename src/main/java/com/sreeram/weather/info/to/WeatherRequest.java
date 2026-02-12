package com.sreeram.weather.info.to;

import java.util.Date;

public class WeatherRequest {
    private String pincode;
    private Date forDate;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Date getForDate() {
        return forDate;
    }

    public void setForDate(Date forDate) {
        this.forDate = forDate;
    }
}
