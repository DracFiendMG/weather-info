package com.sreeram.weather.info.to;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CoordinatesPK implements Serializable {
    private String pincode;
    private Date forDate;

    public CoordinatesPK() {
    }

    public CoordinatesPK(String pincode, Date forDate) {
        this.pincode = pincode;
        this.forDate = forDate;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinatesPK that = (CoordinatesPK) o;
        return Objects.equals(pincode, that.pincode) && Objects.equals(forDate, that.forDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pincode, forDate);
    }
}

