package com.ninjas.movietime.integration.allocine.request;

import com.google.common.base.Joiner;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 24/08/14.
 */
public class RequestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBuilder.class);

   // @Autowired
   // private RestTemplate restTemplate;

   // @Autowired
//    private MetricRegistry registry;

  //  @Autowired
   // private Client bugSnag;

    private final List<Parameter> parameters;
    private final String path;

    private RequestBuilder(String path) {
        this.path = path;
        this.parameters = new ArrayList<>();
    }

    public static RequestBuilder create(final String path) {
        return new RequestBuilder(path);
    }

    public RequestBuilder add(String name, String value) {
        parameters.add(new Parameter(name, value));
        return this;
    }

    public <T> T execute(RestTemplate restTemplate, Class<T> clazz) {
        final URI uri = UrlBuilder.create(this.path, this.parameters);
        //metrics
        //final Timer.Context timerContext = timer("execute").time();
        try {
            //if (method.equals(MethodEnum.GET)) We have only GETs
            return restTemplate.getForObject(uri, clazz);
        } catch (RestClientException restEx) {
            //notify bugSnag
            //bugSnag.notify(restEx);
            //log error
            LOG.error("HTTP GET request ended with error", restEx);
            //rethrow
            throw restEx;
        } finally {
            //get duration
            //final long duration = timerContext.stop();
            //LOG.trace("GET HTTP request executed in {} ms", duration);
        }
    }

//    private Timer timer(String methodName) {
//        return registry.timer(this.getClass().getCanonicalName() + methodName + "(" + path + ")");
//    }

    private static class UrlBuilder {
        private static final String BASE_URI = "http://api.allocine.fr/rest/v3";
        private static final String SECRET = "29d185d98c984a359e6e6f26a0474269";
        private static final String DEFAULT_PARAMS = "format=json&partner=100043982026&profile=large";

        private static final Logger LOG = LoggerFactory.getLogger(UrlBuilder.class);

        /**
         * Create the full url to call the BaseAlloCineAPI
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
        public String toString() {
            return String.format("%s=%s", name, value);
        }
    }
}
