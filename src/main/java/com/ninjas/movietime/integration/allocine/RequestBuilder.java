package com.ninjas.movietime.integration.allocine;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class that help building an URI to call AlloCine REST API
 *
 * @author ayassinov on 24/08/14.
 */
public class RequestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBuilder.class);

    private final List<Parameter> parameters;
    private final String path;

    /**
     * Private constructor used only by the Builder
     *
     * @param path the path of the url
     */
    private RequestBuilder(final @NonNull String path) {
        this.path = path;
        this.parameters = new ArrayList<>();
    }

    /**
     * The enter point to use the builder
     *
     * @param path the path parameter
     * @return the RequestBuilder
     */
    public static RequestBuilder create(final String path) {
        final RequestBuilder requestBuilder = new RequestBuilder(path);
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
        final URI uri = UrlBuilder.create(this.path, this.parameters);
        LOG.trace("URI created {}", uri.toString());
        return uri;
    }

    /**
     * Class to create and sign uri to use for calling AlloCine REST API
     */
    private static class UrlBuilder {
        private static final String BASE_URI = "http://api.allocine.fr/rest/v3";
        private static final String SECRET = "29d185d98c984a359e6e6f26a0474269";
        private static final String DEFAULT_PARAMS = "format=json&partner=100043982026&profile=large";

        private static final Logger LOG = LoggerFactory.getLogger(UrlBuilder.class);

        /**
         * Create the full url to for calling AlloCine REST API
         *
         * @param path       the path of the url
         * @param parameters list of url parameters
         * @return String representing the full URL.
         */
        public static URI create(String path, List<RequestBuilder.Parameter> parameters) {
            //build parameters
            final String sed = getSed();

            final String tokens = buildParams(parameters);

            //sign parameters
            final String sig = getSignature(tokens, sed);

            //  base url / path ? parameters & url parameters & default parameters & sed & signature
            final String url = String.format("%s/%s?%s%s%s", BASE_URI, path, tokens, sed, sig);

            LOG.debug("AlloCine API call: " + url);

            // return an URI so the url will not be encoded a second time.
            return URI.create(url);
        }

        /**
         * Build url parameters, generate a string from <code>List<Parameter></code>
         * and add default url parameters.  <br/><br/>
         * <i>exp: code=143067&format=json&partner=V2luZG93czg&profile=large</i>
         *
         * @param parameters list of parameters to add to the URL.
         * @return structured parameters to add to the URL
         */
        private static String buildParams(List<RequestBuilder.Parameter> parameters) {
            if (parameters.size() > 0) {
                return Joiner.on("&").join(parameters) + "&" + DEFAULT_PARAMS;
            }

            //no params we return the default url parameters.
            return DEFAULT_PARAMS;
        }

        /**
         * Required to sign the request.
         *
         * @return a formatted date yyyyMMdd
         */
        private static String getSed() {
            final String sed = DateTime.now().toString("yyyyMMdd");
            return "&sed=" + sed;
        }

        /**
         * Sign the url parameters and add the sig parameters mandatory to get authorised when calling
         * AlloCine API
         *
         * @param tokens structured url parameters
         * @return String representing the sed and the sign parameters ready to append to the URL
         */
        private static String getSignature(String tokens, String sed) {
            try {
                //join the secret key with parameters and sed.
                final String toDigest = Joiner.on("").join(SECRET, tokens, sed);

                //do the hashing
                final MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(toDigest.getBytes());
                final String sig = Base64.encodeBase64String(md.digest());

                //encode and return the sig url parameter
                return String.format("&sig=%s", URLEncoder.encode(sig, "UTF-8"));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Represent an URL parameters with two properties name, value
     * Use the static one to return a list with only one parameters
     * The toString is surcharged to build an URL core like name=value.
     */
    @Getter
    private static class Parameter {

        private final String name;
        private final String value;

        public Parameter(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        /***
         * Useful to have a format ready url parameters
         */
        public String toString() {
            return String.format("%s=%s", name, value);
        }
    }
}
