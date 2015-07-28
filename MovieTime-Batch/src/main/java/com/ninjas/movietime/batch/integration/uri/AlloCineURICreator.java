package com.ninjas.movietime.batch.integration.uri;

import com.google.common.base.Joiner;
import com.ninjas.movietime.batch.integration.helpers.Parameter;
import com.ninjas.movietime.batch.integration.helpers.UrlSigner;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Class to create and sign URI to use for calling AlloCine REST API
 */
@Slf4j
@Component
public class AlloCineURICreator implements URICreator {

    private static final String BASE_URI = "http://api.allocine.fr/rest/v3";
    private static final String SECRET = "29d185d98c984a359e6e6f26a0474269";
    private static final String DEFAULT_PARAMS = "format=json&partner=100043982026&profile=medium";

    @Setter
    private UrlSigner urlSigner = new UrlSigner(SECRET);

    /**
     * Build url parameters, generate a string from <code>List<Parameter></code>
     * and add default url parameters.  <br/><br/>
     * <i>exp: code=143067&format=json&partner=V2luZG93czg&profile=large</i>
     *
     * @param parameters list of parameters to add to the URL.
     * @return structured parameters to add to the URL
     */
    private String buildParams(List<Parameter> parameters) {
        if (parameters.size() > 0) {
            return Joiner.on("&").join(parameters) + "&" + DEFAULT_PARAMS;
        }

        //no params we return the default url parameters.
        return DEFAULT_PARAMS;
    }

    /**
     * Create the full url to for calling AlloCine REST API
     *
     * @param path       the path of the url
     * @param parameters list of url parameters
     * @return String representing the full URL.
     */
    public URI create(String path, List<Parameter> parameters) {
        final String tokens = buildParams(parameters);

        //sign parameters
        urlSigner.sign(tokens);

        //  base url / path ? parameters & url parameters & default parameters & sed & signature
        final String url = String.format("%s/%s?%s%s%s", BASE_URI, path, tokens, urlSigner.getSed(), urlSigner.getSig());

        log.debug("AlloCine API call: " + url);

        // return an URI so the url will not be encoded a second time.
        return URI.create(url);
    }
}
