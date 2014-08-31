package com.ninjas.movietime.integration.uri;

import com.google.common.base.Joiner;
import com.ninjas.movietime.integration.helpers.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
* @author ayassinov on 31/08/14.
*/
public class RottenTomatoesURICreator implements URICreator {

    private static final Logger LOG = LoggerFactory.getLogger(AlloCineURICreator.class);
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

        //http://api.rottentomatoes.com/api/public/v1.0/movie_alias.json?apikey=n6g8dqcjqb3bu5ze9xavztsx&type=imdb&id=2870708
        final String url = String.format("%s/%s.json?%s", BASE_URI, path, params);

        LOG.debug("RottenTomatoes API call: " + url);

        // return an URI so the url will not be encoded a second time.
        return URI.create(url);
    }
}
