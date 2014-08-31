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

import com.ninjas.movietime.core.domain.Actor;
import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.util.DateUtils;
import lombok.*;
import org.springframework.data.annotation.Id;
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
@Setter
@ToString
@TypeAlias("movie")
@Document(collection = "movies")
@EqualsAndHashCode(of = "id")
public class Movie {

    @Id
    private String id;

    private String imdbTitle;

    private String title;

    private Date releaseDate;

    private int runtime;

    private Rating rating;

    private List<Genre> genres = new ArrayList<>();

    private List<String> directorsName = new ArrayList<>();

    private List<String> actorsName = new ArrayList<>();

    private List<People> directors = new ArrayList<>();

    private List<People> writers = new ArrayList<>();

    private List<Actor> actors = new ArrayList<>();

    private List<People> producers = new ArrayList<>();

    private String imdbCode;

    private String rottenTomatoesId;

    private String theMovieDbCode;

    private int year;

    private String trailerUrl;

    private String tagLine;

    private String overview;

    private String certification;

    private String posterUrl;

    private String fanArtUrl;

    private Date updateDate;

    private Date rottenUpdateDate;

    private Date theMovieDbUpdateDate;

    private Date traktDbUpdateDate;

    @Transient
    private List<Showtime> showtime = new ArrayList<>();

    public Movie() {
    }

    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, String title, Date releaseDate, int runtime,
                 Rating rating, List<Genre> genres, List<String> directorsName, List<String> actorsName) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.rating = rating;
        this.genres = genres;
        this.directorsName = directorsName;
        this.actorsName = actorsName;
        this.updateDate = DateUtils.now();
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
