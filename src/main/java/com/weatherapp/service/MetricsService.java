package com.weatherapp.service;

import com.weatherapp.dto.MetricsResponse;
import com.weatherapp.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsService {

    private final WeatherDataRepository weatherDataRepository;
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public Mono<MetricsResponse> getApplicationMetricsReactive() {
        return Mono.fromCallable(() -> {
                    LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

                    // Get metrics from database
                    Long totalRequests = weatherDataRepository.countByCreatedAtAfter(oneHourAgo);
                    Double averageResponseTime = weatherDataRepository.findAverageResponseTimeSince(oneHourAgo);

                    // Get system metrics
                    long activeThreads = threadMXBean.getThreadCount();
                    long usedMemory = memoryMXBean.getHeapMemoryUsage().getUsed();
                    double memoryUsageMB = usedMemory / (1024.0 * 1024.0);

                    return MetricsResponse.builder()
                            .totalRequests(totalRequests != null ? totalRequests : 0L)
                            .averageResponseTime(averageResponseTime != null ? averageResponseTime : 0.0)
                            .activeThreads(activeThreads)
                            .memoryUsageMB(memoryUsageMB)
                            .build();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}