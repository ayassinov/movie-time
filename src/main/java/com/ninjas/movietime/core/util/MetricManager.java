package com.ninjas.movietime.core.util;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * @author ayassinov on 02/09/2014.
 */
@Slf4j
@Component
public class MetricManager {

    private static MetricRegistry metricRegistry;

    @Autowired
    public MetricManager(MetricRegistry metric) {
        if (metricRegistry == null)
            metricRegistry = metric;
    }

    public static Optional<Timer.Context> startTimer(String className, String methodName) {
        if (metricRegistry != null)
            return Optional.of(metricRegistry.timer(String.format("%s.%s", className, methodName)).time());
        return Optional.absent();
    }

    public static void stopTimer(Optional<Timer.Context> timer) {
        if (timer.isPresent()) {
            final long duration = timer.get().stop();
            log.trace("Timer ended within {} ms", duration);
        }
    }

    public static void markResourceMeter(String... path) {
        metricRegistry.meter(name("resource", path)).mark();
    }
}
