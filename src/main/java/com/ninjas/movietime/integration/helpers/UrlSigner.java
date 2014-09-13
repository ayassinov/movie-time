package com.ninjas.movietime.integration.helpers;

import com.google.common.base.Joiner;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* @author ayassinov on 13/09/14.
*/
public class UrlSigner {

    @Getter
    private String sed;

    @Getter
    private String sig;

    private final String secret;


    public UrlSigner(String secret) {
        this.secret = secret;
    }


    public void sign(String tokens) {
        this.sed = String.format("&sed=%s",DateTime.now().toString("yyyyMMdd"));
        this.sig = getSignature(tokens);
    }

    /**
     * Sign the url parameters and add the sig parameters mandatory to get authorised when calling
     * AlloCine API
     *
     * @param tokens structured url parameters
     * @return String representing the sed and the sign parameters ready to append to the URL
     */
    private String getSignature(String tokens) {
        try {
            //join the secret key with parameters and sed.
            final String toDigest = Joiner.on("").join(this.secret, tokens, this.sed);

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
