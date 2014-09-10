package com.ninjas.movietime.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author ayassinov on 10/09/14.
 */
@Setter
@Getter
@ToString
public class BaseAuditingDocument {

    @LastModifiedDate
    private DateTime lastUpdatedAt;

    @CreatedDate
    private DateTime createdAt;
}
