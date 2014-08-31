package com.ninjas.movietime.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.integration.helpers.RequestBuilder;
import com.ninjas.movietime.integration.helpers.RestClientHelper;
import com.ninjas.movietime.integration.uri.RottenTomatoesURICreator;
import com.ninjas.movietime.integration.uri.URICreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @author ayassinov on 31/08/14.
 */
@Service
public class RottenTomatoesAPI {

    private final RestClientHelper restClient;

    private final URICreator uriCreator;

    @Autowired
    public RottenTomatoesAPI(RestClientHelper restClient) {
        this.restClient = restClient;
        this.uriCreator = new RottenTomatoesURICreator();
    }

    public void updateMovieInformation(Movie movie, String imdbMovieCode) {
        final URI uri = RequestBuilder.create(uriCreator, "movie_alias")
                .add("type", "imdb")
                .add("id", imdbMovieCode)
                .build();
        final JsonNode node = restClient.get(uri);
        if (!node.path("id").isMissingNode()) {
            movie.setRottenTomatoesId(node.path("id").asText());
            movie.getRating().setRottenCriticsRating(node.path("ratings").path("critics_score").asDouble());
            movie.getRating().setRottenUserRating(node.path("ratings").path("audience_score").asDouble());
            movie.setRottenTomatoesLastUpdate(DateUtils.now());
        }
    }
}
