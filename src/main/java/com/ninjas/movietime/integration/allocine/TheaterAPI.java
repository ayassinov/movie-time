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

package com.ninjas.movietime.integration.allocine;

import com.fasterxml.jackson.databind.JsonNode;
import com.ninjas.movietime.core.domain.*;
import com.ninjas.movietime.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 17/07/14
 */
@Service
public class TheaterAPI {

    private final static String THEATER_PATH = "theaterlist";

    private final AlloCineAPIHelper restClient;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TheaterAPI(AlloCineAPIHelper restClient, MongoTemplate mongoTemplate) {
        this.restClient = restClient;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Theater> findAllByRegion(int zip, int radius, int count) {
        final URI uri = RequestBuilder
                .create(THEATER_PATH)
                .add("zip", zip)
                        //.add("page", 2)
                .add("count", count)
                .add("radius", radius)
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

    public void save() {
        final List<Theater> allByRegion = findAllByRegion(75000, 50, 400);
        for (final Theater theater : allByRegion) {
            mongoTemplate.save(theater.getTheaterChain());
            mongoTemplate.save(theater);
        }
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
