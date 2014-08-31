package com.ninjas.movietime.integration.uri;

import com.ninjas.movietime.integration.helpers.Parameter;

import java.net.URI;
import java.util.List;

/**
 * @author ayassinov on 31/08/14.
 */
public interface URICreator {

    URI create(String path, List<Parameter> parameters);


}
