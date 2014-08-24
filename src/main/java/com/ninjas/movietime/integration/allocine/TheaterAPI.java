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

package com.ninjas.movietime.integration.allocine;

import com.ninjas.movietime.core.domain.Theater;
import com.ninjas.movietime.integration.allocine.request.RequestBuilder;
import com.ninjas.movietime.integration.allocine.response.FeedResponse;
import com.ninjas.movietime.integration.allocine.response.RootResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class TheaterAPI {

    private final static String THEATER_PATH = "theaterlist";
    @Autowired
    private RestTemplate restTemplate;

    public List<Theater> findAllByRegion(int zip, int radius, int count) {
        final FeedResponse feedResponse = RequestBuilder
                .create(THEATER_PATH)
                .add("zip", "7500")
                .add("count", "400")
                .add("radius", "50")
                .execute(restTemplate, RootResponse.class)
                .getFeedResponse();
        return new ArrayList<>();
    }
}
