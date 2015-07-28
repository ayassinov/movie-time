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

package com.ninjas.movietime.core.domain.theater;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.geo.Point;

/**
 * @author ayassinov on 16/07/14
 */
@Getter
@ToString
@EqualsAndHashCode(of = {"longitude", "latitude"})
public class GeoLocation {

    private final double longitude;

    private final double latitude;

    @JsonIgnore
    private final Point point;

    @JsonCreator
    public GeoLocation(@JsonProperty("lat") double latitude,
                       @JsonProperty("long") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.point = new Point(longitude, latitude);
    }
}
