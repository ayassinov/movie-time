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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.glagsoft.movietime.core.jackson.FuzzyEnumModule;
import com.glagsoft.movietime.core.jackson.GuavaExtrasModule;
import com.glagsoft.movietime.core.jackson.MovieTimeJsonMapper;
import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

/**
 * @author ayassinov on 11/07/14
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MovieTimeApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MovieTimeApplication.class);

    @Autowired
    private MovieTimeConfiguration configuration;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MovieTimeApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        //load bugsnag
        if (!Strings.isNullOrEmpty(configuration.getBugSnag())) {
            Client bugsnag = new Client(configuration.getBugSnag());
            LOG.info("BugSnag loaded");
        }
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
        //set timeout configuration
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(configuration.getReadTimeOut());
        factory.setConnectTimeout(configuration.getConnectTimeout());
        //factory.setHttpClient(getHttpClient());
        final RestTemplate restTemplate = new RestTemplate(factory);
        return setInterceptor(restTemplate);
    }

    private CloseableHttpClient getHttpClient() {
        //set proxy configuration
        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(
                new AuthScope("server", 9999, AuthScope.ANY_REALM, "ntlm"),
                new NTCredentials("userName", "password", null, null)
        );

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost("server", 9999));
        clientBuilder.setDefaultCredentialsProvider(credProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
        return clientBuilder.build();
    }

    private RestTemplate setInterceptor(RestTemplate restTemplate) {
        restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                HttpHeaders headers = request.getHeaders();
                //headers.add("User-Agent", "AlloCine/2.9.5 CFNetwork/548.1.4 Darwin/11.0.0");
                //headers.add("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.2.2; Nexus 4 Build/JDQ39E)");
                //headers.add("User-Agent", "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
                headers.add("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5");
                //headers.add("User-Agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10");
                return execution.execute(request, body);
            }
        }));
        return restTemplate;
    }


}

