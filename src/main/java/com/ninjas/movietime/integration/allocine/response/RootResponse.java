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

package com.ninjas.movietime.integration.allocine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ninjas.movietime.core.domain.Movie;
import com.ninjas.movietime.core.domain.Theater;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ayassinov on 31/07/2014.
 */
@Getter
@Setter
public class RootResponse {

    @JsonProperty(value = "feed")
    private FeedResponse feedResponse;

    private Movie movie;

    private Theater theater;
}
