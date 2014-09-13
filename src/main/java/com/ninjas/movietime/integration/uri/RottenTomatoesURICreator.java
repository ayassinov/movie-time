package com.ninjas.movietime.integration.uri;

import com.google.common.base.Joiner;
import com.ninjas.movietime.integration.helpers.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Class to create an URI for the rotten Tomatoes REST API
 *
 * @author ayassinov on 31/08/14.
 */
@Slf4j
@Component
public class RottenTomatoesURICreator implements URICreator {

    private final String KEY = "apikey=n6g8dqcjqb3bu5ze9xavztsx";
    private final String BASE_URI = "http://api.rottentomatoes.com/api/public/v1.0";

    @Override
    public URI create(String path, List<Parameter> parameters) {

        final String params;
        if (parameters.size() > 0) {
            params = Joiner.on("&").join(parameters) + "&" + KEY;
        } else {
            params = KEY;
        }


        final String url = String.format("%s/%s.json?%s", BASE_URI, path, params);

        log.debug("RottenTomatoes API call: {}", url);

        // return an URI so the url will not be encoded a second time.
        return URI.create(url);
    }
}
