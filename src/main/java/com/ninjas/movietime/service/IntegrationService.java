package com.ninjas.movietime.service;

import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import com.ninjas.movietime.integration.AlloCineAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 27/08/2014.
 */
@Service
public class IntegrationService {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationService.class);

    private final MongoTemplate mongoTemplate;
    private final AlloCineAPI alloCineAPI;

    @Autowired
    public IntegrationService(MongoTemplate mongoTemplate, AlloCineAPI alloCineAPI) {
        this.mongoTemplate = mongoTemplate;
        this.alloCineAPI = alloCineAPI;
    }

    public void integrateTheaters() {
        final List<Theater> allByRegion = alloCineAPI.findAllInParis();
        for (final Theater theater : allByRegion) {
            mongoTemplate.save(theater.getTheaterChain());
            mongoTemplate.save(theater);
        }
    }

    public void updateShowtime() {
        //find official theaterChain
        final Query theaterChainQuery = Query.query(Criteria.where("isTracked").is(true));
        LOG.debug(theaterChainQuery.toString());


        final List<TheaterChain> theaterChains = mongoTemplate.find(theaterChainQuery, TheaterChain.class);
        for (final TheaterChain chain : theaterChains) {
            if (chain.getId().equalsIgnoreCase("81001")) {
                final Query theaterQuery = Query.query(Criteria.where("theaterChain").is(chain).and("isOpen").is(true));
                LOG.debug(theaterQuery.toString());
                final List<Theater> theaters = mongoTemplate.find(theaterQuery, Theater.class);
                final List<Showtime> showtimes = alloCineAPI.findShowtime(theaters);
                for (Showtime showtime : showtimes)
                    mongoTemplate.save(showtime);
            }

        }
    }

    public void updateMovies() {

    }

    public void integrateUpComingMovies() {

    }

}
