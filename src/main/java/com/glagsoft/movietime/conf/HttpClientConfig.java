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
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author ayassinov
 */
@Getter
//@Component
//@Profile(value = {"dev", "prod"})
//@ConfigurationProperties(prefix = "app.client")
public class HttpClientConfig {

    private int readTimeOut;

    private int connectTimeout;

    @NotEmpty
    private String userAgent;

    private ProxyConfig proxy;
}
