package com.ninjas.movietime.integration.uri;

import com.google.common.base.Preconditions;
import com.ninjas.movietime.integration.helpers.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Class to create the URI for TrackTv REST API
 *
 * @author ayassinov on 31/08/14.
 */
@Slf4j
@Component
public class TraktTvAPIURICreator implements URICreator {

    private final String KEY = "6d98ac69d9f35b06b303d570988c72ff";
    private final String BASE_URI = "http://api.trakt.tv";


    @Override
    public URI create(String path, List<Parameter> parameters) {
        Preconditions.checkElementIndex(0, parameters.size());

        final String url = String.format("%s/%s.json/%s/%s", BASE_URI, path, KEY, parameters.get(0).getValue());

        log.debug("TrackTV API call: {}", url);

        return URI.create(url);
    }
}
