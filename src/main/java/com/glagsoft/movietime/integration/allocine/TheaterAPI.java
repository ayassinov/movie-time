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

import com.glagsoft.movietime.core.domain.Location;
import com.glagsoft.movietime.core.util.StringUtils;
import com.glagsoft.movietime.integration.allocine.core.AlloCineRequest;
import com.glagsoft.movietime.integration.allocine.core.Builder;
import com.glagsoft.movietime.integration.allocine.core.Parameter;
import com.glagsoft.movietime.integration.allocine.core.SearchFilterEnum;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class TheaterAPI extends BaseAlloCineAPI {

    public String findById(String theaterId, int count) {
        final List<Parameter> parameters = Builder.create()
                .add("theater", theaterId)
                .add("count", String.valueOf(count))
                .build();
        final URI url = AlloCineRequest.create("theaterlist", parameters);
        return getForObject(url, String.class);
    }

    public String findByLocation(String location, int radius, int count) {
        final List<Parameter> parameters = Builder.create()
                .add("location", location)
                .build();
        return find(parameters, radius, count);
    }

    public String findByGeoLoc(Location location, int radius, int count) {
        final List<Parameter> parameters = Builder.create()
                .add("lat", location.getLatitude())
                .add("long", location.getLongitude())
                .build();
        return find(parameters, radius, count);
    }

    public String findByZip(String zip, int radius, int count) {
        final List<Parameter> parameters = Builder.create()
                .add("zip", zip)
                .build();
        return find(parameters, radius, count);
    }

    public String findByName(String term, int count) {
        Preconditions.checkNotNull(term);
        return search(StringUtils.encode(term), SearchFilterEnum.THEATER, count, String.class);
    }

    private String find(final List<Parameter> parameters, int radius, int count) {

        parameters.add(new Parameter("radius", String.valueOf(radius)));
        parameters.add(new Parameter("count", String.valueOf(count)));

        final URI url = AlloCineRequest.create("theaterlist", parameters);

        return getForObject(url, String.class);
    }
}
