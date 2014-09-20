package com.ninjas.movietime.repository;

import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.springframework.beans.factory.annotation.Autowired;
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
}
