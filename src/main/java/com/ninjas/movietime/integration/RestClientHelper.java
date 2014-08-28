package com.ninjas.movietime.integration;

import com.bugsnag.Client;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * @author ayassinov on 26/08/2014.
 */
@Component
public class RestClientHelper {

    private static final Logger LOG = LoggerFactory.getLogger(RestClientHelper.class);

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final MetricRegistry metricRegistry;

    private final Client bugSnagClient;

    private final String className = this.getClass().getCanonicalName();

    @Autowired
    public RestClientHelper(@NonNull RestTemplate restTemplate, @NonNull ObjectMapper objectMapper,
                            MetricRegistry metricRegistry, Client bugSnagClient) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.metricRegistry = metricRegistry;
        this.bugSnagClient = bugSnagClient;
    }


    public <T> T get(@NonNull final URI uri, Class<T> clazz) {
        final Optional<Timer.Context> timer = startTimer("get", uri.getPath());
        try {
            return restTemplate.getForObject(uri, clazz);
        } catch (RestClientException ex) {
            processException(ex);
            throw ex;
        } finally {
            processMetrics(timer);
        }
    }

    public JsonNode get(@NonNull final URI uri) {
        final Optional<Timer.Context> timer = startTimer("get", uri.getPath());
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            if (response.getStatusCode() != HttpStatus.OK)
                throw new RestClientException("HTTP Response NON OK. got = " + response.getStatusCode());

            return objectMapper.readTree(response.getBody());
        } catch (RestClientException | IOException ex) {
            processException(ex);
            throw new RestClientException("Json Response error.", ex);
        } finally {
            processMetrics(timer);
        }
    }

    private Optional<Timer.Context> startTimer(String methodName, String path) {
        if (metricRegistry != null)
            return Optional.of(metricRegistry.timer(className + methodName + path).time());
        return Optional.absent();
    }

    private void processException(Exception ex) {
        //notify bugSnag
        if (bugSnagClient != null) {
            bugSnagClient.notify(ex);
        }
        //log error
        LOG.error("HTTP GET request ended with error", ex);
    }

    private void processMetrics(Optional<Timer.Context> timer) {
        if (timer.isPresent()) {
            final long duration = timer.get().stop();
            LOG.trace("GET HTTP request executed in {} ms", duration);
        }
    }

}
