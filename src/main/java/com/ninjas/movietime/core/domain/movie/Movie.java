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

import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.util.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    private String imdbId;

    private String rottenTomatoesId;

    private String timdbId;

    private String imdbTitle;

    private String title;

    @Indexed
    private int year;

    @Indexed
    private Date releaseDate;

    private int runtime;

    private String trailerUrl;

    private String tagLine;

    private String overview;

    private String certification;

    private String posterUrl;

    private String fanArtUrl;

    private String imageUrl;

    private Rating rating;

    private List<Nationality> nationality = new ArrayList<>();

    @DBRef
    private MovieType movieType;

    @DBRef
    private List<Genre> genres = new ArrayList<>();

    @DBRef
    private List<People> directors = new ArrayList<>();

    @DBRef
    private List<People> writers = new ArrayList<>();

    @DBRef
    private List<People> actors = new ArrayList<>();

    @DBRef
    private List<People> producers = new ArrayList<>();

    @Indexed
    private Date lastUpdate;

    @Indexed
    private Date rottenTomatoesLastUpdate;

    @Indexed
    private Date timdbLastUpdate;

    @Indexed
    private Date traktLastUpdate;

    @Transient
    private List<Showtime> showtime = new ArrayList<>();
    private String synopsis;

    public Movie() {
        this.lastUpdate = DateUtils.now();
    }

    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, String title, Date releaseDate, int runtime, Rating rating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.rating = rating;
    }
}
