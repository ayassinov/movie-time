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

import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.GeoLocation;
import com.ninjas.movietime.core.util.StringUtils;
import com.ninjas.movietime.integration.allocine.core.Builder;
import com.ninjas.movietime.integration.allocine.core.Parameter;
import com.ninjas.movietime.integration.allocine.core.SearchFilterEnum;
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

    public String findById(String theaterId, int count) {
        final List<Parameter> parameters = buildParameters(count, "theater", theaterId);
        return get(parameters, String.class);
    }

    public String findByLocation(String location, int radius, int count) {
        final List<Parameter> parameters = buildParameters(radius, count, "location", location);
        return get(parameters, String.class);
    }

    public String findByGeoLocation(GeoLocation geoLocation, int radius, int count) {
        final List<Parameter> parameters = buildParameters(radius, count,
                "lat", geoLocation.getLatitude(),
                "long", geoLocation.getLongitude());

        return get(parameters, String.class);
    }

    public String findByZip(int zip, int radius, int count) {
        final List<Parameter> parameters = buildParameters(radius, count, "zip", String.valueOf(zip));
        return get(parameters, String.class);
    }

    public String findByName(String term, int count) {
        Preconditions.checkNotNull(term);
        return search(StringUtils.encode(term), SearchFilterEnum.THEATER, count, String.class);
    }

    private List<Parameter> buildParameters(int count, String... params) {
        return buildParameters(-1, count, params);
    }

    private List<Parameter> buildParameters(int radius, int count, String... params) {
        //check if there is a pair of params
        Preconditions.checkArgument(params.length % 2 == 0, "Params should be in pairs");

        final Builder parameterBuilder = Builder.create();
        for (int i = 0; i < params.length; i = i + 2) {
            parameterBuilder.add(params[i], params[i + 1]);
        }

        //default parameters for all calls :)
        parameterBuilder.add("count", String.valueOf(count));
        if (radius >= 0) {
            parameterBuilder.add("radius", String.valueOf(radius));
        }

        return parameterBuilder.build();
    }
}
