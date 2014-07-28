package com.ninjas.movietime.core.metrics;

import org.apache.http.HttpRequest;

/**
 * @author ayassinov on 28/07/2014.
 */
public interface HttpClientMetricNameStrategy {
    String getNameFor(String name, HttpRequest request);
}
