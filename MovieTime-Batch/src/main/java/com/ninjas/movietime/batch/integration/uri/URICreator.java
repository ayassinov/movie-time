package com.ninjas.movietime.batch.integration.uri;

import com.ninjas.movietime.batch.integration.helpers.Parameter;

import java.net.URI;
import java.util.List;

/**
 * Interface to implement by external REST API to specify how to build URI
 * using path parameter and url parameters
 *
 * @author ayassinov on 31/08/14.
 */
public interface URICreator {

    /**
     * Create an Url using a Path and Url parameters
     *
     * @param path       the path of the url
     * @param parameters the list of url parameters
     * @return an URI using the given parameters
     */
    URI create(String path, List<Parameter> parameters);

}
