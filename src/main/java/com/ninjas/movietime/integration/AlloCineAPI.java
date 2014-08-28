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
import com.ninjas.movietime.core.domain.showtime.Schedule;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.*;
import com.ninjas.movietime.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class AlloCineAPI {

    private final RestClientHelper restClient;

    @Autowired
    public AlloCineAPI(RestClientHelper restClient) {
        this.restClient = restClient;
    }

    public List<Theater> findAllInParis() {
        final URI uri = RequestBuilder
                .create("theaterlist")
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

    public List<Showtime> findShowtime(List<Theater> theaterChains) {
        //build theater codes
        List<String> ids = new ArrayList<>();
        for (Theater theaterChain : theaterChains) {
            ids.add(theaterChain.getId());
        }
        final String theatersCode = Joiner.on(",").join(ids);

        //build uri
        final URI uri = RequestBuilder
                .create("showtimelist")
                .add("theaters", theatersCode)
                        //.add("movie", "code")
                        //.add("date", "YYYY-MM-DD")
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

    private Schedule createSchedule(final JsonNode node) {
        final Schedule schedule = new Schedule();
        schedule.setVO(node.path("version").path("original").asBoolean());
        schedule.setLanguage(new Schedule.Language(
                node.path("version").path("lang").asText(),
                node.path("version").path("$").asText()));
        schedule.setScreenFormat(new Schedule.ScreenFormat(
                node.path("screenFormat").path("code").asText(),
                node.path("screenFormat").path("$").asText()));

        for (JsonNode nodeDateTime : node.path("scr")) {
            for (JsonNode nodeTime : nodeDateTime.path("t")) {
                schedule.addDateTime(DateUtils.parse(nodeDateTime.path("d").asText() + " " + nodeTime.path("$").asText(), "yyyy-MM-dd HH:mm"));
            }
        }

        return schedule;
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
                showtime = new Showtime(codeTheater, codeMovie);
                showtime.addSchedule(createSchedule(nodeShowtime));
                showtimeMap.put(codeMovie, showtime);
            }
        }
        return  showtimeMap.values();
    }

    private Theater createTheater(final JsonNode node) {
        final TheaterChain theaterChain = new TheaterChain(
                node.path("cinemaChain").path("code").asText(),
                node.path("cinemaChain").path("$").asText()
        );

        final GeoLocation geoLocation = new GeoLocation(
                node.path("geoloc").path("lat").asDouble(),
                node.path("geoloc").path("long").asDouble());

        final Address address = new Address(
                node.path("address").asText(),
                node.path("city").asText(),
                node.path("postalCode").asText());

        final ShutDownStatus shutDownStatus;
        if (!node.path("shutdown").isMissingNode()) {
            shutDownStatus = new ShutDownStatus(
                    DateUtils.parse(node.path("shutdown").path("dateStart").asText()),
                    DateUtils.parse(node.path("shutdown").path("dateEnd").asText()));
        } else {
            shutDownStatus = null;
        }

        return new Theater(node.path("code").asText(), node.path("name").asText(),
                geoLocation, address, theaterChain, shutDownStatus);
    }
}