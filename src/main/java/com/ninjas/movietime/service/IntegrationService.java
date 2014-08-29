package com.ninjas.movietime.service;

import com.ninjas.movietime.core.domain.UpdateTracking;
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
        //track update
        mongoTemplate.save(new UpdateTracking(UpdateTracking.OperationEnum.THEATER_UPDATE, true,
                "DONE WITH SUCCESS"));
    }

    public void updateShowtime() {
        //find official theaterChain
        final List<TheaterChain> theaterChains = listAllTheaterChain(true);
        //iterate over every chain to get the theaters list
        for (final TheaterChain theaterChain : theaterChains) {
            if (theaterChain.getId().equalsIgnoreCase("81001")) {
                final List<Theater> theaters = listOpenTheaterByTheaterChain(theaterChain);
                final List<Showtime> showtimes = alloCineAPI.findShowtime(theaters);

                //todo bulk save
                for (Showtime showtime : showtimes) {
                    mongoTemplate.save(showtime.getMovie());
                    mongoTemplate.save(showtime);
                }
            }
        }
    }

    private List<TheaterChain> listAllTheaterChain(boolean isOnlyTracked) {
        final List<TheaterChain> theaterChains;
        if (isOnlyTracked) {
            final Query theaterChainQuery = Query.query(Criteria.where("isTracked").is(true));
            theaterChains = mongoTemplate.find(theaterChainQuery, TheaterChain.class);
        } else {
            theaterChains = mongoTemplate.findAll(TheaterChain.class);
        }
        return theaterChains;
    }

    private List<Theater> listOpenTheaterByTheaterChain(TheaterChain theaterChain) {
        final Query theaterQuery = Query.query(Criteria.where("theaterChain").is(theaterChain).and("isOpen").is(true));
        return mongoTemplate.find(theaterQuery, Theater.class);
    }

   /* public void updateMovies() {

    }

    public void integrateUpComingMovies() {

    }*/

}
