package com.ninjas.movietime.core.domain.api;

import com.ninjas.movietime.core.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ayassinov on 29/08/2014.
 */

@Data
@TypeAlias("apiCallLog")
@Document(collection = "apiCallLog")
@EqualsAndHashCode(of = "id")
public class APICallLog {

    @Id
    private int id;

    private OperationEnum operation;

    private Date callDate;

    private boolean status;

    private String message;

    public APICallLog() {
    }

    public APICallLog(OperationEnum operation, boolean status, String message) {
        this.operation = operation;
        this.callDate = DateUtils.now();
        this.status = status;
        this.message = message;
    }

    public enum OperationEnum {
        THEATER_UPDATE, SHOWTIME_UPDATE, MOVIE_RATING_UPDATE, MOVIE_INFORMATION_UPDATE
    }

    public static APICallLog fail(OperationEnum operation) {
        return new APICallLog(operation, false, null);
    }

    public static APICallLog success(OperationEnum operation) {
        return new APICallLog(operation, true, null);
    }

}
