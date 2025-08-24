package com.weatherapp.repository;

import com.weatherapp.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    Long countByCreatedAtAfter(LocalDateTime since);

    @Query("SELECT AVG(w.apiResponseTimeMs) FROM WeatherData w WHERE w.createdAt >= :since")
    Double findAverageResponseTimeSince(@Param("since") LocalDateTime since);
}