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

import com.ninjas.movietime.integration.allocine.core.AlloCineRequest;
import com.ninjas.movietime.integration.allocine.core.Builder;
import com.ninjas.movietime.integration.allocine.core.Parameter;
import com.ninjas.movietime.integration.allocine.core.SearchFilterEnum;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class MovieAPI extends BaseAlloCineAPI {


    /**
     * Get a movie information using the allo cine code.
     *
     * @param movieId Code of the film in allo cine.
     * @return movie details in the json text format
     * throws IllegalArgumentException if movieId is null or empty
     */
    public String findById(String movieId) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(movieId));

        final List<Parameter> parameters = Builder
                .create("code", movieId)
                .build();

        final URI url = AlloCineRequest.create("movie", parameters);

        return getForObject(url, String.class);
    }

    public String findByName(String name, int count) {
        return search(name, SearchFilterEnum.MOVIE, count, String.class);
    }
}
