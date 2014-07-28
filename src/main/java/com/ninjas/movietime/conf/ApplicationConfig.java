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

package com.ninjas.movietime.conf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    @NotNull(message = "Running Mode is one of (DEV,PROD,TEST)")
    private RunModeEnum mode;

    @NotNull(message = "Version cannot be null or empty")
    private String version;

    @NotNull(message = "Mongo url configuration should not be null or empty")
    private String mongoUrl;

    @NotNull(message = "http client configuration is mandatory")
    private HttpClientConfig client;

    private String bugSnag = null;

    private String esUrl = null;

    @PostConstruct
    public void postConstruct() {
        LOG.info("Movie Time Configuration loaded successfully with this parameters: [{}]", toString());
    }
}
