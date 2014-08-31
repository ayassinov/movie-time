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
public class ImdbURICreator implements URICreator {
    private static final Logger LOG = LoggerFactory.getLogger(ImdbURICreator.class);
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
        return URI.create(url);
    }
}
