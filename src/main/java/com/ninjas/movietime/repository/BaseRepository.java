package com.ninjas.movietime.repository;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author ayassinov on 20/09/14.
 */
public abstract class BaseRepository {

    @Getter(AccessLevel.PROTECTED)
    private final MongoTemplate mongoTemplate;

    protected BaseRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    protected <T> Page<T> findPaged(Pageable pageRequest, Query query, Class<T> clazz) {
        Preconditions.checkArgument(pageRequest.getOffset() >= 0, "The current page should be greater or equal to zero");
        Preconditions.checkArgument(pageRequest.getPageSize() > 0, "The page size should be greater than zero");

        final long count = mongoTemplate.count(query, clazz);
        query.with(pageRequest);

        final List<T> content = mongoTemplate.find(query, clazz);
        return new PageImpl<T>(content, pageRequest, count);
    }

}
