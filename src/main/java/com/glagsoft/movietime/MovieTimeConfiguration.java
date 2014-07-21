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

import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * @author ayassinov on 18/07/14
 */
@Getter
@Component
@ConfigurationProperties
public class MovieTimeConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MovieTimeConfiguration.class);

    @NotNull
    @Value("${app.mode}")
    private String mode;

    @NotNull
    @Value("${app.version}")
    private String version;

    @NotNull(message = "Mongo url configuration should not be null or empty")
    @Value("${mongo.url}")
    private String mongoUrl;


    @Range(min = 1000, max = 30000)
    @Value("${httpClient.readTimeOut}")
    private int readTimeOut = 2000;


    @Range(min = 1000, max = 30000)
    @Value("${httpClient.connectTimeout}")
    private int connectTimeout = 2000;

    @Value("${app.bugSnag}")
    private String bugSnag = null;

    @PostConstruct
    public void postConstruct() {
        LOG.info("Configuration Loaded in mode [{}] and version [{}]", getMode(), getVersion());
    }
}
