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

package com.ninjas.movietime.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.movie.*;
import com.ninjas.movietime.core.domain.showtime.DateAndTime;
import com.ninjas.movietime.core.domain.showtime.Schedule;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.*;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.integration.helpers.RequestBuilder;
import com.ninjas.movietime.integration.helpers.RestClientHelper;
import com.ninjas.movietime.integration.uri.URICreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class AlloCineAPI {

    private final RestClientHelper restClient;

    private final URICreator uriCreator;

    @Autowired
    public AlloCineAPI(RestClientHelper restClient, @Qualifier("alloCineURICreator") URICreator uriCreator) {
        this.restClient = restClient;
        this.uriCreator = uriCreator;
    }

    public List<Theater> findAllInParis() {
        final URI uri = RequestBuilder
                .create(uriCreator, "theaterlist")
                .add("zip", 75000) //paris
                .add("count", 400)
                .add("radius", 50)
                .build();

        final List<Theater> theaters = new ArrayList<>();
        final JsonNode jsonNode = restClient.get(uri);
        if (!jsonNode.path("feed").path("theater").isMissingNode()) {
            for (JsonNode node : jsonNode.path("feed").path("theater")) {
                theaters.add(createTheater(node));
            }
        }
        return theaters;
    }

    public List<Showtime> findShowtime(List<Theater> theaters) {
        //build theater codes
        List<String> ids = new ArrayList<>();
        for (Theater theater : theaters) {
            ids.add(theater.getId());
        }
        final String theatersCode = Joiner.on(",").join(ids);

        //build uri
        final URI uri = RequestBuilder
                .create(uriCreator, "showtimelist")
                .add("theaters", theatersCode)
                .build();
        final JsonNode root = restClient.get(uri);
        final List<Showtime> showtimes = new ArrayList<>();
        if (!root.path("feed").path("theaterShowtimes").isMissingNode()) {
            for (JsonNode node : root.path("feed").path("theaterShowtimes")) {
                showtimes.addAll(createShowtime(node));
            }
        }
        return showtimes;
    }

    public List<Movie> findComingSoon() {
        //build uri
        final URI uri = RequestBuilder
                .create(uriCreator, "movielist")
                .add("filter", "comingsoon")
                .add("order", "dateasc")
                .add("count", 50)
                        //.add("page", 1)
                .build();

        final JsonNode root = restClient.get(uri);
        List<Movie> movies = new ArrayList<>();
        if (!root.path("feed").path("movie").isMissingNode()) {
            for (JsonNode node : root.path("feed").path("movie")) {
                movies.add(createMovie(node));
            }
        }

        return movies;
    }

    public Optional<Movie> getMovie(String movieId) {
        final URI uri = RequestBuilder
                .create(uriCreator, "movie")
                .add("code", movieId)
                .add("filter", "movie")
                .add("striptags", "synopsis,synopsisshort")
                .build();

        final JsonNode root = restClient.get(uri);

        final JsonNode movieNode = root.path("movie");
        if (movieNode.isMissingNode())
            return Optional.absent();


        final Movie movie = createMovie(movieNode);
        movie.getMovieUpdateStatus().setAlloCineFullUpdated(true);
        return Optional.of(movie);
    }

    public void updateFullMovieInformation(Movie movieToUpdate) {
        final Movie movie = this.getMovie(movieToUpdate.getId()).orNull();
        if (movie == null)
            return;
        movieToUpdate.setRating(movie.getRating());
        movieToUpdate.setOriginalTitle(movie.getOriginalTitle());
        movieToUpdate.setKeyword(movie.getKeyword());
        movieToUpdate.setSynopsis(movie.getSynopsis());
        movieToUpdate.setSynopsisShort(movie.getSynopsisShort());
        movieToUpdate.setMovieType(movie.getMovieType());
        movieToUpdate.setNationality(movie.getNationality());
        movieToUpdate.getMovieUpdateStatus().setAlloCineFullUpdated(true);
        movieToUpdate.getMovieUpdateStatus().setLastAlloCineUpdate(DateUtils.nowServerDateTime());
    }

    private Schedule createSchedule(final JsonNode node) {
        final Schedule schedule = new Schedule();
        schedule.setVO(node.path("version").path("original").asBoolean());
        schedule.setLanguage(new Language(
                node.path("version").path("lang").asText(),
                node.path("version").path("$").asText()));
        schedule.setScreenFormat(new Schedule.ScreenFormat(
                node.path("screenFormat").path("code").asText(),
                node.path("screenFormat").path("$").asText()));

        final String dateFormat = "yyyy-MM-dd";
        final String timeFormat = "HH:mm";
        final Map<Date, DateAndTime> dateAndTimeMap = new HashMap<>();
        for (JsonNode nodeDateTime : node.path("scr")) {
            Date currentDate = DateUtils.parse(nodeDateTime.path("d").asText(), dateFormat);
            DateAndTime dateAndTime;
            for (JsonNode nodeTime : nodeDateTime.path("t")) {
                if (dateAndTimeMap.containsKey(currentDate)) {
                    dateAndTimeMap.get(currentDate).addTime(nodeTime.path("$").asText(), timeFormat);
                } else {
                    dateAndTime = new DateAndTime(currentDate);
                    dateAndTime.addTime(nodeTime.path("$").asText(), timeFormat);
                    dateAndTimeMap.put(currentDate, dateAndTime);
                }
            }
        }
        schedule.addDateAndTime(dateAndTimeMap.values());
        return schedule;
    }

    private Movie createMovie(final JsonNode node) {
        final Rating rating = new Rating(
                node.path("statistics").path("pressRating").asDouble(),
                node.path("statistics").path("pressReviewCount").asInt(),
                node.path("statistics").path("userRating").asDouble(),
                node.path("statistics").path("userRatingCount").asInt()
        );

        final Movie movie = new Movie(
                node.path("code").asText(),
                node.path("title").asText(),
                DateUtils.parseDateTime(node.path("release").path("releaseDate").asText(), "yyyy-MM-dd"),
                node.path("runtime").asInt(),
                rating
        );

        movie.setOriginalTitle(node.path("originalTitle").asText());
        movie.setKeyword(node.path("keywords").asText());
        movie.getImages().add(new Image(node.path("poster").path("href").asText(), Image.ImageTypeEnum.FRENCH_POSTER));
        movie.setSynopsis(node.path("synopsis").asText());
        movie.setSynopsisShort(node.path("synopsisShort").asText());
        movie.getMovieUpdateStatus().setLastAlloCineUpdate(DateUtils.nowServerDateTime());

        if (!node.path("movieCertificate").isMissingNode()) {
            movie.setCertification(node.path("movieCertificate").path("certificate").path("$").asText());
        }

        if (!node.path("movieType").isMissingNode()) {
            movie.setMovieType(new MovieType(
                    node.path("movieType").path("code").asText(),
                    node.path("movieType").path("$").asText()
            ));
        }

        final String[] directorsNames = node.path("castingShort").path("directors").asText().split(",");
        for (String director : directorsNames) {
            movie.getStaff().getDirectors().add(new People(director.trim(), People.JobEnum.DIRECTOR, null));
        }

        final String[] actorNames = node.path("castingShort").path("actors").asText().split(",");
        for (String actor : actorNames) {
            movie.getStaff().getActors().add(new People(actor.trim(), People.JobEnum.ACTOR, null, null));
        }

        for (JsonNode genreNode : node.path("genre")) {
            movie.getGenres().add(new Genre(
                    genreNode.path("code").asText(),
                    genreNode.path("$").asText()
            ));
        }

        for (JsonNode nationality : node.path("nationality")) {
            movie.getNationality().add(new Nationality(
                    nationality.path("code").asText(),
                    nationality.path("$").asText()
            ));
        }

        return movie;
    }

    private Collection<Showtime> createShowtime(final JsonNode node) {
        String codeMovie;
        Showtime showtime;
        final String codeTheater = node.path("place").path("theater").path("code").asText();
        final Map<String, Showtime> showtimeMap = new HashMap<>();
        for (JsonNode nodeShowtime : node.path("movieShowtimes")) {
            codeMovie = nodeShowtime.path("onShow").path("movie").path("code").asText();
            if (showtimeMap.containsKey(codeMovie)) {
                showtimeMap.get(codeMovie).addSchedule(createSchedule(nodeShowtime));
            } else {
                showtime = new Showtime(new Theater(codeTheater), createMovie(nodeShowtime.path("onShow").path("movie")));
                showtime.addSchedule(createSchedule(nodeShowtime));
                showtimeMap.put(codeMovie, showtime);
            }
        }
        return showtimeMap.values();
    }

    private Theater createTheater(final JsonNode node) {
        final TheaterChain theaterChain = new TheaterChain(
                node.path("cinemaChain").path("code").asText(),
                node.path("cinemaChain").path("$").asText()
        );

        final Address address = new Address(
                node.path("address").asText(),
                node.path("city").asText(),
                node.path("postalCode").asText());

        final ShutDownStatus shutDownStatus;
        if (!node.path("shutdown").isMissingNode()) {
            shutDownStatus = new ShutDownStatus(
                    DateUtils.parseDateTime(node.path("shutdown").path("dateStart").asText()),
                    DateUtils.parseDateTime(node.path("shutdown").path("dateEnd").asText()));
        } else {
            shutDownStatus = null;
        }

        return new Theater(node.path("code").asText(), node.path("name").asText(),
                node.path("geoloc").path("lat").asDouble(), node.path("geoloc").path("long").asDouble(), address, theaterChain, shutDownStatus);
    }
}
