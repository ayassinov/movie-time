package com.ninjas.movietime.integration.uri;

import com.google.common.base.Joiner;
import com.ninjas.movietime.integration.helpers.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Class to create an URI for TheMovieDb.org REST API
 *
 * @author ayassinov on 31/08/14.
 */
@Slf4j
@Component
public class ImdbURICreator implements URICreator {

    private final String KEY = "api_key=e478c264afe3a1f9fb058c8059cedd78";
    private final String BASE_URI = "https://api.themoviedb.org/3";


    @Override
    public URI create(String path, List<Parameter> parameters) {
        final String params;
        if (parameters.size() > 0) {
            params = "&" + Joiner.on("&").join(parameters);
        } else {
            params = "";
        }

        final String url = String.format("%s/%s?%s%s", BASE_URI, path, KEY, params);

        log.debug("TheMovieDB API call: {}", url);

        return URI.create(url);
    }
}
