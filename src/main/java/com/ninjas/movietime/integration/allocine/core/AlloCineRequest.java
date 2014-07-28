/*
 * Copyright 2014 GlagSoftware
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ninjas.movietime.integration.allocine.core;

import com.google.common.base.Joiner;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//import java.util.Base64;

//import java.util.Base64;

/**
 * Utility class to create a signed allo cine request.
 *
 * @author ayassinov on 15/07/14
 */
public class AlloCineRequest {

    private static final String BASE_URI = "http://api.allocine.fr/rest/v3";
    private static final String SECRET = "29d185d98c984a359e6e6f26a0474269";
    private static final String DEFAULT_PARAMS = "format=json&partner=100043982026&profile=large";

    private static final Logger LOG = LoggerFactory.getLogger(AlloCineRequest.class);

    /**
     * Create the full url to call the BaseAlloCineAPI
     *
     * @param path       the path of the url
     * @param parameters list of url parameters
     * @return String representing the full URL.
     */
    public static URI create(String path, List<Parameter> parameters) {
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
    private static String buildParams(List<Parameter> parameters) {
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
    public static String getSignature(String tokens, String sed) {
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
