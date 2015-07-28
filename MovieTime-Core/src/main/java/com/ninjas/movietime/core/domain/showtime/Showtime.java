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
import com.ninjas.movietime.core.util.DateUtils;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 31/07/2014.
 */
@Getter
@ToString
@TypeAlias("showtime")
@Document(collection = "showtimes")
@EqualsAndHashCode(of = "id")
public class Showtime {

    @Id
    private String id;

    @DBRef
    @Indexed
    private Theater theater;

    @DBRef
    @Indexed
    private Movie movie;

    private Date lastUpdate;

    @Setter(AccessLevel.NONE)
    private List<Schedule> schedules = new ArrayList<>();

    public Showtime(String theaterId, String movieCode) {
        this(new Theater(theaterId), new Movie(movieCode));
    }

    @PersistenceConstructor
    public Showtime(Theater theater, Movie movie) {
        this.id = String.format("%s_%s", theater.getId(), movie.getId());
        this.theater = theater;
        this.movie = movie;
        this.lastUpdate = DateUtils.nowServerDate();
        this.getTheater().getShowtime().add(this);
        this.getMovie().getShowtime().add(this);
    }


    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }
}
