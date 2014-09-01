package com.ninjas.movietime.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.integration.helpers.RequestBuilder;
import com.ninjas.movietime.integration.helpers.RestClientHelper;
import com.ninjas.movietime.integration.uri.TraktTvAPIURICreator;
import com.ninjas.movietime.integration.uri.URICreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * @author ayassinov on 31/08/14.
 */
@Service
public class TraktTvAPI {

    private final RestClientHelper restClient;

    private final URICreator uriCreator;

    @Autowired
    public TraktTvAPI(RestClientHelper restClient) {
        this.restClient = restClient;
        this.uriCreator = new TraktTvAPIURICreator();
    }

    public JsonNode getMovieDetail(String imdbCode) {
        //http://api.trakt.tv/movie/summary.json/6d98ac69d9f35b06b303d570988c72ff/262391
        final URI uri = RequestBuilder.create(uriCreator, "movie/summary").add("imdb", imdbCode).build();
        return restClient.get(uri);
    }

    public void getMoviesDetail(List<String> imdbCodes) {
        Preconditions.checkArgument(imdbCodes.size() > 0);
        final URI uri = RequestBuilder.create(uriCreator, "movie/summaries").add("imdb", Joiner.on(",").join(imdbCodes)).build();
        final JsonNode jsonNode = restClient.get(uri);
    }

    public void updateMovieInformation(Movie movie, String theMovieDbCode) {
        final JsonNode node = getMovieDetail(theMovieDbCode);
        if (!node.path("title").isMissingNode()) {
            movie.setTraktLastUpdate(DateUtils.now());
            movie.setImdbId(node.path("imdb_id").asText().replace("tt", ""));
            movie.setTrailerUrl(node.path("trailer").asText());
            movie.setTagLine(node.path("tagline").asText());
            movie.setOverview(node.path("overview").asText());
            movie.setCertification(node.path("certification").asText());
            movie.setPosterUrl(node.path("images").path("poster").asText());
            movie.setFanArtUrl(node.path("images").path("fanart").asText());
            movie.getRating().setTrackTvRating(node.path("ratings").path("percentage").asDouble());
            movie.getRating().setTrackTvVoteCount(node.path("ratings").path("votes").asDouble());

            for (JsonNode directorNode : node.path("people").path("directors")) {
                movie.getDirectors().add(createPeople(directorNode, People.JobEnum.DIRECTOR));
            }

            for (JsonNode writersNode : node.path("people").path("writers")) {
                movie.getWriters().add(createPeople(writersNode, People.JobEnum.WRITER));
            }

            for (JsonNode producerNode : node.path("people").path("producers")) {
                movie.getProducers().add(createPeople(producerNode, People.JobEnum.PRODUCER));
            }

            for (JsonNode actorNode : node.path("people").path("actors")) {
                movie.getActors().add(new People(actorNode.path("name").asText(),
                                People.JobEnum.ACTOR,
                                actorNode.path("images").path("headshot").asText(),
                                actorNode.path("character").asText())
                );
            }
        }
    }

    private People createPeople(JsonNode node, People.JobEnum job) {
        return new People(node.path("name").asText(), job, node.path("images").path("headshot").asText());
    }
}
