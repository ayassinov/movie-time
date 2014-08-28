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

import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 15/07/14
 */
@Data
@TypeAlias("theater")
@Document(collection = "theaters")
@EqualsAndHashCode(of = "id")
public class Theater {

    @Id
    private String id;

    //@Indexed(name = "idx_name", unique = false)
    private String name;

    private GeoLocation geoLocation;

    private Address address;

    @DBRef
    private TheaterChain theaterChain;

    private ShutDownStatus shutDownStatus;

    private boolean isOpen;

    private Date lastUpdate;

    private List<Showtime> showtime = new ArrayList<>();

    public Theater() {
    }

    public Theater(String id) {
        this.id = id;
    }

    public Theater(String id, String name, GeoLocation geoLocation,
                   Address address, TheaterChain theaterChain,
                   ShutDownStatus shutDownStatus) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLocation;
        this.address = address;
        this.theaterChain = theaterChain;
        this.shutDownStatus = shutDownStatus;
        this.isOpen = checkIfTheaterIsOpen();
        this.lastUpdate = DateUtils.now();
    }

    private boolean checkIfTheaterIsOpen() {
        return this.shutDownStatus == null || DateUtils.isBeforeNow(this.shutDownStatus.getDateEnd());
    }
}
