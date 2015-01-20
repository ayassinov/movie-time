package com.ninjas.movietime.repository;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author ayassinov on 20/09/14.
 */
@Repository
public class TheaterChainRepository extends BaseRepository {

    @Autowired
    protected TheaterChainRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public Collection<TheaterChain> listOfficialTheaterChainIds() {
        final Query query = Query.query(Criteria.where("isTracked").is(true));
        query.fields().include("id");
        return getMongoTemplate().find(query, TheaterChain.class);
    }

    public Page<TheaterChain> listAll(PageRequest pageRequest) {
        return findPaged(pageRequest, new Query(), TheaterChain.class);
    }

    public Optional<TheaterChain> getById(String id) {
        Preconditions.checkNotNull(id, "Id cannot be null");
        return Optional.fromNullable(getMongoTemplate().findById(id, TheaterChain.class));
    }
}
