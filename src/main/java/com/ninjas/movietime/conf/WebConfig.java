package com.ninjas.movietime.conf;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ninjas.movietime.MovieTimeConfig;
import com.ninjas.movietime.conf.vendor.jackson.FuzzyEnumModule;
import com.ninjas.movietime.conf.vendor.jackson.GuavaExtrasModule;
import com.ninjas.movietime.conf.vendor.jackson.MovieTimeJsonMapper;
import com.ninjas.movietime.conf.vendor.metrics.HttpClientMetricNameStrategies;
import com.ninjas.movietime.conf.vendor.metrics.InstrumentedHttpClientConnectionManager;
import com.ninjas.movietime.conf.vendor.metrics.InstrumentedHttpRequestExecutor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author ayassinov on 15/08/14.
 */
@Configuration
public class WebConfig {

    @Autowired
    private MovieTimeConfig config;

    @Autowired
    private MetricRegistry metricRegistry;

    private ObjectMapper objectMapper;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addErrorPages(new ErrorPage(org.springframework.http.HttpStatus.NOT_FOUND, "/index.html"),
                new ErrorPage(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "/index.html"));
        return factory;
    }

    /**
     * Create a REST template to call an external API
     *
     * @return Spring REST template
     */
    @Bean
    public RestTemplate restTemplate() {
        //get HttpFactory
        final HttpComponentsClientHttpRequestFactory factory =
                WebConfig.HttpClientFactory.INSTANCE.getRequestFactory(metricRegistry, config.getClient());

        return new RestTemplate(factory);
    }

    /**
     * Configure Jackson mapper with our preferences
     *
     * @return Jackson converter
     */
    @Bean
    public MappingJackson2HttpMessageConverter configureMapper() {
        final MovieTimeJsonMapper jsonMapper = new MovieTimeJsonMapper();
        jsonMapper.setObjectMapper(objectMapper());
        return jsonMapper;
    }

    /**
     * Return an Jackson Object Mapper to convert from/to Java Object to JsonNode types.
     *
     * @return Object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        if (objectMapper != null)
            return objectMapper;

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new FuzzyEnumModule());
        objectMapper.registerModule(new GuavaExtrasModule());
        objectMapper.registerModule(new GuavaModule());

        //objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return objectMapper;
    }


    @PostConstruct
    public void postConstruct() {
        if (config == null)
            throw new NullPointerException("MovieTimeConfig is mandatory. Check Spring autowiring");
    }


    private static class HttpClientFactory {
        public static final HttpClientFactory INSTANCE = new HttpClientFactory();
        private static final Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);
        private final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        private HttpClientFactory() {
        }

        public HttpComponentsClientHttpRequestFactory getRequestFactory(final MetricRegistry registry,
                                                                        final MovieTimeConfig.HttpClientConfig config) {
            final HttpClient httpClient = getHttpClient(registry, config);
            factory.setHttpClient(httpClient);
            factory.setReadTimeout(config.getReadTimeOut());
            factory.setConnectTimeout(config.getConnectTimeout());
            return factory;
        }

        private HttpClient getHttpClient(final MetricRegistry registry, final MovieTimeConfig.HttpClientConfig config) {
            //activate metrics
            final HttpClientBuilder clientBuilder = HttpClientBuilder.create();

            //instrumentation
            if (registry != null && config.isActivateMetrics()) {
                clientBuilder.setRequestExecutor(
                        new InstrumentedHttpRequestExecutor(registry, HttpClientMetricNameStrategies.HOST_AND_METHOD));
                clientBuilder.setConnectionManager(
                        new InstrumentedHttpClientConnectionManager(registry));
                LOG.info("HTTP Client created with instrumentation");
            }

            //user agent
            clientBuilder.setUserAgent(config.getUserAgent());

            //proxy configuration
            if (config.getProxy() != null) {
                setProxy(clientBuilder, config.getProxy());
                LOG.info("HTTP Client created with Proxy configuration");
            }

            return clientBuilder.build();
        }

        private void setProxy(HttpClientBuilder clientBuilder, MovieTimeConfig.ProxyConfig config) {
            CredentialsProvider credProvider = new BasicCredentialsProvider();
            credProvider.setCredentials(
                    new AuthScope(config.getServer(), config.getPort(), AuthScope.ANY_REALM, "ntlm"),
                    new NTCredentials(config.getUserName(), config.getPassword(), null, null)
            );
            clientBuilder.useSystemProperties();
            clientBuilder.setProxy(new HttpHost(config.getServer(), config.getPort()));
            clientBuilder.setDefaultCredentialsProvider(credProvider);
            clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
        }


    }

}
