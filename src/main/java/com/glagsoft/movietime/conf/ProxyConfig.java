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
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotNull;

/**
 * @author ayassinov
 */
@Getter
@Setter
@Profile("proxy")
@ToString(exclude = "password")
public class ProxyConfig {

    @NotEmpty(message = "Server cannot be null or empty")
    private String server;

    @NotNull(message = "The proxy server port number is mandatory")
    private int port;

    private String userName;

    private String password;
}
