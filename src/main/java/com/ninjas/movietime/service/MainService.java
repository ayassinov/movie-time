package com.ninjas.movietime.service;

import com.google.common.base.Preconditions;
import com.ninjas.movietime.MovieTimeConfig;
import com.ninjas.movietime.core.domain.api.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 08/09/14.
 */
@Service
public class MainService {

    private final MovieTimeConfig config;
    private final EndpointHandlerMapping endpointHandlerMapping;

    @Autowired
    public MainService(MovieTimeConfig config,
                       @SuppressWarnings("SpringJavaAutowiringInspection") EndpointHandlerMapping endpointHandlerMapping) {
        this.config = config;
        this.endpointHandlerMapping = endpointHandlerMapping;
    }

    public Information getAppInformation() {
        final MovieTimeConfig.AppConfig configApp = config.getApp();
        Preconditions.checkNotNull(configApp, "Application configuration cannot be null");
        return new Information(configApp.getName(), configApp.getVersion(), configApp.getApiVersion());
    }

    public List<String> listManageEndPoints() {
        final List<String> endpoints = new ArrayList<>();
        for (MvcEndpoint endpoint : endpointHandlerMapping.getEndpoints())
            endpoints.add(endpoint.getPath());
        return endpoints;
    }
}
