/*
 * Copyright 2014 Parisian Ninjas
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

package com.ninjas.movietime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * @author ayassinov on 18/07/14
 */
@Getter
@Setter
@Component
@ToString
@ConfigurationProperties(prefix = "movietime")
public class MovieTimeConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MovieTimeConfig.class);

    @NotNull
    private AppConfig app;

    @NotNull(message = "http client configuration is mandatory")
    private HttpClientConfig client;

    @NotNull(message = "Mongo configuration is mandatory")
    private MongoConfig mongo;

    private GraphiteConfig graphite;

    public MovieTimeConfig(){}


    @PostConstruct
    public void postConstruct() {
        LOG.info("Movie Time Configuration loaded successfully with this parameters: [{}]", toString());
    }

    public static enum RunModeEnum {

        DEV, PROD, TEST;

        public String toString() {

            switch (this) {
                case DEV:
                    return "development";
                case PROD:
                    return "production";
                case TEST:
                    return "test";
                default:
                    throw new EnumConstantNotPresentException(RunModeEnum.class, this.name());
            }
        }
    }


    @Getter
    @Setter
    @ToString
    public static class GraphiteConfig {

        @NotEmpty(message = "ApiKey is mandatory")
        private String apiKey;

        @NotEmpty(message = "Host is mandatory")
        private String host;

        @NotEmpty(message = "Port is mandatory")
        private int port;

        @NotNull(message = "Activate should have a value of true or false")
        private boolean activate;
    }

    @Getter
    @Setter
    @ToString
    public static class MongoConfig {

        @NotNull(message = "Mongo url configuration should not be null or empty")
        private String url;

        @NotNull(message = "WriteResultChecking should not be null, and need to be one of NONE, LOG, EXCEPTION")
        private WriteResultChecking writeResultChecking;
    }

    @Getter
    @Setter
    @ToString
    public static class HttpClientConfig {

        @NotNull(message = "Read timeout cannot be null")
        private int readTimeOut;

        @NotNull(message = "Connection timeout cannot be null")
        private int connectTimeout;

        @NotEmpty(message = "User agent cannot be null or empty")
        private String userAgent;

        @NotNull(message = "Activate metrics should be equals to true or false")
        private boolean activateMetrics = true;

        private ProxyConfig proxy;
    }

    @Getter
    @Setter
    @Profile("proxy")
    @ToString(exclude = "password")
    public static class ProxyConfig {

        @NotEmpty(message = "Server cannot be null or empty")
        private String server;

        @NotNull(message = "The proxy server port number is mandatory")
        private int port;

        private String userName;

        private String password;
    }

    /**
    * @author ayassinov on 15/08/14.
    */
    @Getter
    @Setter
    public static class AppConfig {
        @NotNull(message = "Running Mode is one of (DEV,PROD,TEST)")
        private RunModeEnum mode;

        @NotNull(message = "Version cannot be null or empty")
        private String version;


        private String bugSnag = null;

        private String esUrl = null;

        public AppConfig(){

        }
    }
}
