package com.ninjas.movietime.integration.uri;

import com.google.common.base.Preconditions;
import com.ninjas.movietime.integration.helpers.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * @author ayassinov on 31/08/14.
 */
public class TraktTvAPIURICreator implements URICreator {
    private static final Logger LOG = LoggerFactory.getLogger(TraktTvAPIURICreator.class);
    private final String KEY = "6d98ac69d9f35b06b303d570988c72ff";
    private final String BASE_URI = "http://api.trakt.tv";


    @Override
    public URI create(String path, List<Parameter> parameters) {
        Preconditions.checkElementIndex(0, parameters.size());
        //http://api.trakt.tv/movie/summary.json/6d98ac69d9f35b06b303d570988c72ff/262391
        final String url = String.format("%s/%s.json/%s/%s", BASE_URI, path, KEY, parameters.get(0).getValue());
        return URI.create(url);
    }
}
