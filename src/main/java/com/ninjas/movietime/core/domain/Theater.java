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

package com.ninjas.movietime.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ayassinov on 15/07/14
 */
@Getter
@Setter
@ToString
@TypeAlias("theater")
@Document(collection = "theaters")
@EqualsAndHashCode(of = "id")
public class Theater {

    @Id
    @JsonProperty("code")
    private String id;

    //@Indexed(name = "idx_name")
    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "geoloc", required = true)
    private GeoLocation geoLocation;

    @JsonUnwrapped
    private Address adr;

    @DBRef
    @JsonProperty(value = "cinemaChain", required = true)
    private TheaterChain theaterChain;

    @JsonProperty(required = false)
    private Shutdown shutdown;

    public Theater() {
    }

    public Theater(String name, GeoLocation geoLocation,
                   Address adr, TheaterChain theaterChain, Shutdown shutdown) {
        this(null, name, geoLocation, adr, theaterChain, shutdown);
    }

    public Theater(String id, String name, GeoLocation geoLocation,
                   Address adr, TheaterChain theaterChain, Shutdown shutdown) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLocation;
        this.adr = adr;
        this.theaterChain = theaterChain;
        this.shutdown = shutdown;
    }

}
