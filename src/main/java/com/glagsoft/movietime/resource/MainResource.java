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

package com.glagsoft.movietime.resource;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * @author ayassinov on 11/07/14
 */
@RestController
@RequestMapping("/")
public class MainResource {

    @Autowired
    private MetricRegistry metrics;

    @RequestMapping(method = RequestMethod.GET)
    public String main() {
        metrics.meter(name("meter", "MainResource", "main", "request")).mark();
        return "YO ! MAN!";
    }

    @RequestMapping(method = RequestMethod.HEAD)
    /**
     * for new relic ping
     */
    public String ok() {
        return "ok";
    }
}
