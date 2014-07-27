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

package com.glagsoft.movietime.conf;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * @author ayassinov on 18/07/14
 */
@Getter
@Component
@Profile(value = {"dev", "prod"})
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    //@NotNull
    // private RunModeEnum mode;

    @NotNull
    private String version;

    @NotNull(message = "Mongo url configuration should not be null or empty")
    private String mongoUrl;

    //private HttpClientConfig client;

    private String bugSnag = null;

    private String esUrl = null;

    @PostConstruct
    public void postConstruct() {
        LOG.info("Configuration Loaded in mode [{}] and version [{}]", null, getVersion());
    }
}
