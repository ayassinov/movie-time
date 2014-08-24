package com.ninjas.movietime.conf;

import com.bugsnag.Client;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.ninjas.movietime.MovieTimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author ayassinov on 15/08/14.
 */
@Configuration
public class MetricsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsConfig.class);

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
        //set console reporter only on dev mode
        if (configuration.getApp().getMode().equals(MovieTimeConfig.RunModeEnum.DEV)) {
            final ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build();
            reporter.start(1, TimeUnit.MINUTES);
            LOG.info("Metrics console reporter bootstrapped");
        }

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
            LOG.info("Metrics graphite reporter bootstrapped");
        }
        return metricRegistry;
    }


}
