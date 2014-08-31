package com.ninjas.movietime.integration.helpers;

import com.ninjas.movietime.integration.uri.URICreator;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class that help building an URI to call AlloCine REST API
 *
 * @author ayassinov on 24/08/14.
 */
public class RequestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBuilder.class);

    private final URICreator uriCreator;
    private final List<Parameter> parameters;
    private final String path;

    /**
     * Private constructor used only by the Builder
     *
     * @param uriCreator URICreator implementation
     * @param path       the path of the url
     */
    private RequestBuilder(@NonNull URICreator uriCreator, final @NonNull String path) {
        this.uriCreator = uriCreator;
        this.path = path;
        this.parameters = new ArrayList<>();
    }

    /**
     * The enter point to use the builder
     *
     * @param path the path parameter
     * @return the RequestBuilder
     */
    public static RequestBuilder create(final URICreator uriCreator, final String path) {
        final RequestBuilder requestBuilder = new RequestBuilder(uriCreator, path);
        LOG.trace("RequestBuilder was created with path={}", path);
        return requestBuilder;
    }

    /**
     * Add url parameter using int value
     *
     * @param name  the name of the parameter
     * @param value the int value
     * @return the RequestBuilder
     */
    public RequestBuilder add(@NonNull final String name, final int value) {
        final String StringValue = String.valueOf(value);
        add(name, StringValue);
        LOG.trace("URL parameter added name={} value={}", name, value);
        return this;
    }

    /**
     * Add url parameter using String value
     *
     * @param name  the name of the parameter
     * @param value the string value
     * @return the RequestBuilder
     */
    public RequestBuilder add(@NonNull final String name, @NonNull final String value) {
        parameters.add(new Parameter(name, value));
        return this;
    }

    /**
     * Build the url using the path and url parameters
     *
     * @return full URI signed to use for calling AlloCine REST API
     */
    public URI build() {
        final URI uri = uriCreator.create(this.path, this.parameters);
        LOG.trace("URI created {}", uri.toString());
        return uri;
    }

}
