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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.ninjas.movietime.core.domain.GeoLocation;
import com.ninjas.movietime.core.domain.Theater;
import com.ninjas.movietime.core.util.StringUtils;
import com.ninjas.movietime.integration.allocine.request.Builder;
import com.ninjas.movietime.integration.allocine.request.Parameter;
import com.ninjas.movietime.integration.allocine.request.SearchFilterEnum;
import com.ninjas.movietime.integration.allocine.response.FeedResponse;
import com.ninjas.movietime.integration.allocine.response.RootResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class TheaterAPI extends BaseAlloCineAPI {

    public TheaterAPI() {
        super("theaterlist");
    }

    public Optional<Theater> findById(String theaterId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(theaterId));

        final Builder paramBuilder = Builder.create();
        paramBuilder.add("code", theaterId);

        final String path = "theater"; //path to one resource

        final Theater theater = get(path, paramBuilder.build()).getTheater();
        return Optional.fromNullable(theater);
    }

    public List<Theater> findByLocation(String location, int radius, int count) {
        final Builder paramBuilder = Builder.create();
        paramBuilder.add("location", location);
        paramBuilder.add("count", String.valueOf(count));
        paramBuilder.add("radius", String.valueOf(radius));

        return listTheater(paramBuilder.build());
    }

    public List<Theater> findByGeoLocation(GeoLocation geoLocation, int radius, int count) {
        final Builder paramBuilder = Builder.create();
        paramBuilder.add("lat", geoLocation.getLatitude());
        paramBuilder.add("long", geoLocation.getLongitude());
        paramBuilder.add("count", String.valueOf(count));
        paramBuilder.add("radius", String.valueOf(radius));

        return listTheater(paramBuilder.build());
    }

    public List<Theater> findByZip(int zip, int radius, int count) {
        final Builder paramBuilder = Builder.create();
        paramBuilder.add("zip", String.valueOf(zip));
        paramBuilder.add("count", String.valueOf(count));
        paramBuilder.add("radius", String.valueOf(radius));

        return listTheater(paramBuilder.build());
    }

    public String findByName(String term, int count) {
        Preconditions.checkNotNull(term);
        return search(StringUtils.encode(term), SearchFilterEnum.THEATER, count, String.class);
    }

    private List<Theater> listTheater(List<Parameter> parameters) {
        final RootResponse rootResponse = get(parameters);
        final FeedResponse feedResponse = rootResponse.getFeedResponse();
        Preconditions.checkNotNull(feedResponse, "feedResponse should never be null.");
        return feedResponse.getTheater();
    }
}
