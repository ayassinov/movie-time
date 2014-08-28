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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 30/07/2014.
 */
@Data
@TypeAlias("movie")
@Document(collection = "movies")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @Id
    private String id;

    private Date releaseDate;

    private GenreEnum genreEnum;

    private Rating rating;

    //private final int productionYear;

    private List<Showtime> showtime = new ArrayList<>();

    public Movie() {
    }

    public Movie(String id) {
        this.id = id;
    }
}
