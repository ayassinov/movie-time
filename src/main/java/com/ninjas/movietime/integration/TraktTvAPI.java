package com.ninjas.movietime.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.exception.CannotFindTrackTvInformationException;
import com.ninjas.movietime.core.domain.movie.Image;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.integration.helpers.RequestBuilder;
import com.ninjas.movietime.integration.helpers.RestClientHelper;
import com.ninjas.movietime.integration.uri.TraktTvAPIURICreator;
import com.ninjas.movietime.integration.uri.URICreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @author ayassinov on 31/08/14.
 */
@Slf4j
@Service
public class TraktTvAPI {

    private final RestClientHelper restClient;

    private final URICreator uriCreator;

    @Autowired
    public TraktTvAPI(RestClientHelper restClient) {
        this.restClient = restClient;
        this.uriCreator = new TraktTvAPIURICreator();
    }

    public void updateMovieInformation(Movie movie) {
        final URI uri = RequestBuilder
                .create(uriCreator, "movie/summary")
                .add("imdb", movie.getTimdbId())
                .build();

        final JsonNode node = restClient.get(uri);
        if (!node.path("title").isMissingNode()) {
            movie.setImdbId(node.path("imdb_id").asText().replace("tt", ""));
            movie.setTrailerUrl(node.path("trailer").asText());
            movie.getImages().add(new Image(node.path("images").path("poster").asText(), Image.ImageTypeEnum.ORIGINAL_POSTER));
            movie.getImages().add(new Image(node.path("images").path("fanart").asText(), Image.ImageTypeEnum.FAN_ART));
            movie.getRating().setTrackTvRating(node.path("ratings").path("percentage").asDouble());
            movie.getRating().setTrackTvVoteCount(node.path("ratings").path("votes").asDouble());
            movie.getMovieUpdateStatus().setLastTrackTvUpdate(DateUtils.nowServerDateTime());

            for (JsonNode directorNode : node.path("people").path("directors")) {
                movie.getStaff().getDirectors().add(createPeople(directorNode, People.JobEnum.DIRECTOR));
            }

            for (JsonNode writersNode : node.path("people").path("writers")) {
                movie.getStaff().getWriters().add(createPeople(writersNode, People.JobEnum.WRITER));
            }

            for (JsonNode producerNode : node.path("people").path("producers")) {
                movie.getStaff().getProducers().add(createPeople(producerNode, People.JobEnum.PRODUCER));
            }

            for (JsonNode actorNode : node.path("people").path("actors")) {
                movie.getStaff().getActors().add(new People(actorNode.path("name").asText(),
                                People.JobEnum.ACTOR,
                                actorNode.path("images").path("headshot").asText(),
                                actorNode.path("character").asText())
                );
            }

            log.debug("Information from trackTv found for the movie {}", movie.getTitle());

        } else {
            throw new CannotFindTrackTvInformationException("Movie not found on TrackTV",
                    movie.getId(), movie.getTimdbId(), movie.getTitle());
        }
    }

    private People createPeople(JsonNode node, People.JobEnum job) {
        return new People(node.path("name").asText(), job, node.path("images").path("headshot").asText());
    }
}
