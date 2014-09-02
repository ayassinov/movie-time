package com.ninjas.movietime.repository;

import com.ninjas.movietime.core.domain.APICallLog;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author ayassinov on 02/09/2014.
 */
@Repository
public class IntegrationRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public IntegrationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void saveTheater(Collection<Theater> theaters) {
        for (final Theater theater : theaters) {
            saveTheater(theater);
        }
    }

    public void saveTheater(Theater theater) {
        try {
            mongoTemplate.save(theater.getTheaterChain());
            mongoTemplate.save(theater);
        } catch (Exception ex) {
            //ignore todo catch Exception
        }
    }

    public void saveAPICallLog(APICallLog apiCallLog) {
        mongoTemplate.save(apiCallLog);
    }

    public List<TheaterChain> listAllTheaterChain(boolean isOnlyTracked) {
        final List<TheaterChain> theaterChains;
        if (isOnlyTracked) {
            final Query theaterChainQuery = Query.query(Criteria.where("isTracked").is(true));
            theaterChains = mongoTemplate.find(theaterChainQuery, TheaterChain.class);
        } else {
            theaterChains = mongoTemplate.findAll(TheaterChain.class);
        }
        return theaterChains;
    }

    public List<Theater> listOpenTheaterByTheaterChain(TheaterChain theaterChain, boolean idOnly) {
        final Query theaterQuery = Query.query(Criteria.where("theaterChain").is(theaterChain).and("isOpen").is(true));
        if (idOnly)
            theaterQuery.fields().include("id");
        return mongoTemplate.find(theaterQuery, Theater.class);
    }
}
