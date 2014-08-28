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

package com.ninjas.movietime.core.domain.showtime;

import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.domain.theater.Theater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 31/07/2014.
 */
@Data
@TypeAlias("showtime")
@Document(collection = "showtimes")
@EqualsAndHashCode(of = "id")
public class Showtime {

    @Id
    private String id;

    @DBRef
    private Theater theater;

    @DBRef
    private Movie movie;

    private Date lastUpdate;

    private final List<Schedule> schedules = new ArrayList<>();

    public Showtime() {
    }

    public Showtime(String theaterId, String movieCode) {
        this.theater = new Theater(theaterId);
        this.movie = new Movie(movieCode);
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }
}
