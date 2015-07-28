package com.ninjas.movietime.service;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.ninjas.movietime.MovieTimeConfig;
import com.ninjas.movietime.core.domain.api.Information;
import com.ninjas.movietime.core.util.MetricManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 08/09/14.
 */
@Service
public class MainService {

    private final String className = this.getClass().getCanonicalName();

    private final MovieTimeConfig config;

    @Autowired
    public MainService(MovieTimeConfig config) {
        this.config = config;
    }

    public Information getAppInformation() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "getAppInformation");
        try {
            final MovieTimeConfig.AppConfig configApp = config.getApp();
            Preconditions.checkNotNull(configApp, "Application configuration cannot be null");
            return new Information(configApp.getName(), configApp.getVersion(), configApp.getApiVersion());
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    public List<String> listManageEndPoints() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listManageEndPoints");
        try {
            final List<String> endpoints = new ArrayList<>();
            //todo list all manage endPoints
            endpoints.add("/task");
            endpoints.add("/status");
            return endpoints;
        } finally {
            MetricManager.stopTimer(timer);
        }
    }
}
