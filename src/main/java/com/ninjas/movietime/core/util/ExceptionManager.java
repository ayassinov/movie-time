package com.ninjas.movietime.core.util;

import com.bugsnag.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ayassinov on 04/09/14.
 */
@Slf4j
@Component
public class ExceptionManager {

    private static Client bugSnagClient;

    @Autowired
    public ExceptionManager(Client client) {
        if (bugSnagClient == null)
            bugSnagClient = client;
    }


    public static void log(Throwable ex, String message, Object... objects) {
        if (bugSnagClient != null) {
            bugSnagClient.notify(ex);
        }

        log.error(String.format(message, objects), ex);
    }
}
