package com.sreeram.weather.info.repository;

import com.sreeram.weather.info.to.CoordinatesTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatesRepository extends JpaRepository<CoordinatesTO, Long> {
    CoordinatesTO findCoordinatesTOByPincode(Long pincode);

}
