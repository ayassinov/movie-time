/*
 * Copyright 2014 GlagSoftware
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glagsoft.movietime;

import com.bugsnag.Client;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.glagsoft.movietime.conf.ApplicationConfig;
import com.glagsoft.movietime.conf.HttpClientFactory;
import com.glagsoft.movietime.core.jackson.FuzzyEnumModule;
import com.glagsoft.movietime.core.jackson.GuavaExtrasModule;
import com.glagsoft.movietime.core.jackson.MovieTimeJsonMapper;
import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

/**
 * @author ayassinov on 11/07/14
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
//@ActiveProfiles(profiles = "dev")
public class MovieTimeApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MovieTimeApplication.class);

    @Autowired
    private ApplicationConfig configuration;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MovieTimeApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        //load bugsnag
        if (!Strings.isNullOrEmpty(configuration.getBugSnag())) {
            Client bugsnag = new Client(configuration.getBugSnag());
            bugsnag.notify(new RuntimeException("Non-fatal"));
            LOG.info("BugSnag loaded");
        }
    }


    @Bean
    public MetricRegistry getMetrics() {
        final MetricRegistry metrics = new MetricRegistry();

        return metrics;
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

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new FuzzyEnumModule());
        objectMapper.registerModule(new GuavaExtrasModule());
        objectMapper.registerModule(new GuavaModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return objectMapper;
    }

    @Bean
    @Scope(value = "singleton")
    public MongoDbFactory mongoDbFactory() throws Exception {
        //parse the uri
        final MongoClientURI mongoClientURI = new MongoClientURI(configuration.getMongoUrl());
        //create mongodb client
        final MongoClient mongoClient = new MongoClient(mongoClientURI);
        //create spring mongodbFactory
        return new SimpleMongoDbFactory(mongoClient, mongoClientURI.getDatabase());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public RestTemplate restTemplate() {
        //get HttpFactory
        final HttpComponentsClientHttpRequestFactory factory =
                HttpClientFactory.INSTANCE.getRequestFactory(/*configuration.getClient()*/ null);

        return new RestTemplate(factory);
    }

}

