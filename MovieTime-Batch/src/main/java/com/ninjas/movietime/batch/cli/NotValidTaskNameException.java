package com.ninjas.movietime.batch.cli;

/**
 * @author ayassinov on 28/07/15.
 */
public class NotValidTaskNameException extends RuntimeException {
    public NotValidTaskNameException(String s) {
        super(s);
    }

    public NotValidTaskNameException(Exception ex) {
        super(ex);
    }
}
