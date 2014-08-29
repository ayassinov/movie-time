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

package com.ninjas.movietime.core.domain.movie;

import com.ninjas.movietime.core.domain.showtime.Showtime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 30/07/2014.
 */
@Getter
@ToString
@TypeAlias("movie")
@Document(collection = "movies")
@EqualsAndHashCode(of = "id")
public class Movie {

    @Id
    private final String id;

    private final String title;

    private final Date releaseDate;

    private final int runtime;

    private final Rating rating;

    private final List<Genre> genres;

    private final List<String> directors;

    private final List<String> actors;

    @Transient
    private List<Showtime> showtime = new ArrayList<>();

    //private final int productionYear;

    public Movie(String id) {
        this(id, null, null, 0, null
                , new ArrayList<Genre>()
                , new ArrayList<String>()
                , new ArrayList<String>());
    }

    @PersistenceConstructor
    public Movie(String id, String title, Date releaseDate, int runtime,
                 Rating rating, List<Genre> genres, List<String> directors, List<String> actors) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.rating = rating;
        this.genres = genres;
        this.directors = directors;
        this.actors = actors;
    }

    @Data
    public static class Genre {
        private String code;
        private String name;

        public Genre(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}
