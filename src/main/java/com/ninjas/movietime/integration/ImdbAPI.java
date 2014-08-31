package com.ninjas.movietime.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.core.util.StringUtils;
import com.ninjas.movietime.integration.helpers.RequestBuilder;
import com.ninjas.movietime.integration.helpers.RestClientHelper;
import com.ninjas.movietime.integration.uri.ImdbURICreator;
import com.ninjas.movietime.integration.uri.URICreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @author ayassinov on 31/08/14.
 */
@Service
public class ImdbAPI {
    private final RestClientHelper restClient;

    private final URICreator uriCreator;

    @Autowired
    public ImdbAPI(RestClientHelper restClient) {
        this.restClient = restClient;
        this.uriCreator = new ImdbURICreator();
    }

    public void updateMovieInformation(Movie movie, String movieName, int movieYear) {
        //        //https://api.themoviedb.org/3/search/movie?api_key=e478c264afe3a1f9fb058c8059cedd78&query=Qu%27est-ce%20qu%27on%20a%20fait%20au%20Bon%20Dieu&year=2014
        final URI uri = RequestBuilder.create(uriCreator, "search/movie").add("query", StringUtils.encode(movieName)).add("year", movieYear).build();
        final JsonNode node = restClient.get(uri);

        if (node.path("total_results").asInt() > 0) {
            movie.setTheMovieDbCode(node.path("results").get(0).path("id").asText());
            movie.setImdbTitle(node.path("results").get(0).path("title").asText()); //second Title
            movie.getRating().setImdbRating(node.path("results").get(0).path("vote_average").asDouble());
            movie.getRating().setImdbVoteCount(node.path("results").get(0).path("vote_count").asInt());
            movie.setTheMovieDbUpdateDate(DateUtils.now());
        }

    }
}
