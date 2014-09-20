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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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

    private String timdbId;

    private String trackTvId; //new

    private String rottenTomatoesId;

    private String title;

    private String imdbTitle;

    private String originalTitle; //new

    private String keyword;

    private String synopsis; //new

    private String synopsisShort;

    private int runtime;

    @Indexed
    private DateTime releaseDate;

    private String trailerUrl; //not always exists !

    private String certification;

    private Rating rating;

    private Staff staff = new Staff();

    @DBRef
    private MovieType movieType;

    @DBRef
    private List<Genre> genres = new ArrayList<>();

    private List<Image> images = new ArrayList<>();

    private List<Nationality> nationality = new ArrayList<>();

    @Transient
    private List<Showtime> showtime = new ArrayList<>();

    @JsonIgnore
    private MovieUpdateStatus movieUpdateStatus = new MovieUpdateStatus();

    public Movie() {

    }

    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, String title, DateTime releaseDate, int runtime, Rating rating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.rating = rating;
    }

    @JsonProperty
    public String releaseDateText(){
        return this.releaseDate.toString();
    }
}
