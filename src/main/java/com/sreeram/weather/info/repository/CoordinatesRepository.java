package com.sreeram.weather.info.repository;

import com.sreeram.weather.info.to.CoordinatesPK;
import com.sreeram.weather.info.to.CoordinatesTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CoordinatesRepository extends JpaRepository<CoordinatesTO, CoordinatesPK> {
    CoordinatesTO findCoordinatesTOByPincodeAndForDate(String pincode, Date forDate);

}
