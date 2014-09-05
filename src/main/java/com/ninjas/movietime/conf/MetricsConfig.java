package com.ninjas.movietime.conf;

import com.bugsnag.Client;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.ninjas.movietime.MovieTimeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author ayassinov on 15/08/14.
 */
@Slf4j
@Configuration
public class MetricsConfig {

    @Autowired
    private MovieTimeConfig configuration;

    private MetricRegistry metricRegistry;

    /**
     * Create a BugSnag Client, we rely on the configuration URL to create this object
     *
     * @return BugSnag Client
     */
    @Bean
    @Scope(value = "singleton")
    public Client getBugSnagClient() {
        Client bugSnag = new Client(configuration.getApp().getBugSnag());
        //set the current version
        bugSnag.setAppVersion(configuration.getApp().getVersion());
        //set the current release stage
        bugSnag.setReleaseStage(configuration.getApp().getMode().toString());
        //notify about exception only in production mode;
        bugSnag.setNotifyReleaseStages(MovieTimeConfig.RunModeEnum.PROD.toString());
        log.info("BugSnag bootstrapped with application version {}", configuration.getApp().getVersion());
        return bugSnag;
    }

    @Bean
    @Scope(value = "singleton")
    public MetricRegistry getRegistry() {
        if (metricRegistry != null)
            return metricRegistry;

        return buildMetricRegistry();
    }

    private MetricRegistry buildMetricRegistry() {
        metricRegistry = new MetricRegistry();
        //set graphite reporter
        final MovieTimeConfig.GraphiteConfig graphiteConfig = configuration.getGraphite();
        if (graphiteConfig.isActivate()) {
            final Graphite graphite = new Graphite(new InetSocketAddress(graphiteConfig.getHost(), graphiteConfig.getPort()));
            final GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                    .prefixedWith(graphiteConfig.getApiKey())
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(graphite);
            graphiteReporter.start(1, TimeUnit.MINUTES);
            log.info("Metrics graphite reporter bootstrapped");
        } else {
            log.info("Metrics reporter not running");
        }
        return metricRegistry;
    }
}
