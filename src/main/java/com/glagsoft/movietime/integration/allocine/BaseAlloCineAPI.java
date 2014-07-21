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

package com.glagsoft.movietime.integration.allocine;

import com.glagsoft.movietime.integration.allocine.core.AlloCineRequest;
import com.glagsoft.movietime.integration.allocine.core.Builder;
import com.glagsoft.movietime.integration.allocine.core.Parameter;
import com.glagsoft.movietime.integration.allocine.core.SearchFilterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ayassinov on 11/07/14
 */
public abstract class BaseAlloCineAPI {

    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5";

    @Autowired
    private RestTemplate restTemplate;

    protected <T> T getForObject(URI uri, Class<T> clazz) {
        return restTemplate.getForObject(uri, clazz);
    }

    private <T> T getForObjectWithHeaders(URI uri, Map<String, String> headers, Class<T> clazz) {
        final HttpHeaders httpHeaders = getJsonDefaultHeader();
        httpHeaders.setAll(headers);
        final ResponseEntity<T> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity(httpHeaders), clazz);
        return exchange.getBody();
    }

    private HttpHeaders getJsonDefaultHeader() {
        //accept
        final List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        //create headers
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(acceptableMediaTypes);
        httpHeaders.add("User-Agent", USER_AGENT_VALUE);
        return httpHeaders;
    }

    protected <T> T search(String term, SearchFilterEnum searchFilterEnum, int count, Class<T> clazz) {
        final List<Parameter> parameters = Builder.create()
                .add("q", term)
                .add("filter", searchFilterEnum.toString())
                .add("count", String.valueOf(count))
                .build();

        final URI url = AlloCineRequest.create("search", parameters);

        return getForObject(url, clazz);
    }
}



