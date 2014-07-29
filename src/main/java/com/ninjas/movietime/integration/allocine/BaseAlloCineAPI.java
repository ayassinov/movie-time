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

package com.ninjas.movietime.integration.allocine;

import com.bugsnag.Client;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.ninjas.movietime.integration.allocine.core.AlloCineRequest;
import com.ninjas.movietime.integration.allocine.core.Builder;
import com.ninjas.movietime.integration.allocine.core.Parameter;
import com.ninjas.movietime.integration.allocine.core.SearchFilterEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * @author ayassinov on 11/07/14
 */
public abstract class BaseAlloCineAPI {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseAlloCineAPI.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MetricRegistry registry;

    @Autowired
    private Client bugSnag;

    private final String path;

    protected BaseAlloCineAPI(String path) {
        //set path
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "Path cannot be null");
        this.path = path;
    }

    /**
     * Execute an HTTP GET request using Allo Cine configuration providing a path variable and a result type
     *
     * @param parameters list of URL parameters
     * @param clazz      The result class type
     * @param <T>        The type of the result
     * @return return an object in the same type as provided, resulting from the execution of HTTP GET request
     */
    protected <T> T get(List<Parameter> parameters, Class<T> clazz) {
        //construct the url
        final URI url = AlloCineRequest.create(path, parameters);
        //metrics
        final Timer.Context timerContext = timer("get").time();
        try {
            return restTemplate.<T>getForObject(url, clazz);
        } catch (RestClientException restEx) {
            //notify bugSnag
            bugSnag.notify(restEx);
            //log error
            LOG.error("HTTP GET request ended with error", restEx);
            //rethrow
            throw restEx;
        } finally {
            //get duration
            final long duration = timerContext.stop();
            LOG.trace("GET HTTP request executed in {} ms", duration);
        }
    }

    protected <T> T search(String term, SearchFilterEnum searchFilterEnum, int count, Class<T> clazz) {
        final List<Parameter> parameters = Builder.create()
                .add("q", term)
                .add("filter", searchFilterEnum.toString())
                .add("count", String.valueOf(count))
                .build();

        final URI url = AlloCineRequest.create("search", parameters);

        //todo migrate to search Repo
        return restTemplate.getForObject(url, clazz);
    }

    private Timer timer(String methodName) {
        return registry.timer(this.getClass().getCanonicalName() + methodName + "(" + path + ")");
    }
}



