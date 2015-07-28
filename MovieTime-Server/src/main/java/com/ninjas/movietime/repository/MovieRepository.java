package com.ninjas.movietime.repository;

import com.ninjas.movietime.core.domain.date.Week;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ayassinov on 29/08/2014.
 */
@Repository
public class MovieRepository extends BaseRepository {

    @Autowired
    public MovieRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public Page<Movie> listComingSoon(int page, int countPerPage) {
        final DateTime nowParisDateTime = DateUtils.nowParisDateTime();
        final Query query = Query.query(Criteria.where("releaseDate").gt(nowParisDateTime));
        final Pageable pageable = new PageRequest(page, countPerPage, Sort.Direction.ASC, "releaseDate");
        return findPaged(pageable, query, Movie.class);
    }

    public Page<Movie> listOpeningThisWeek(int page, int countPerPage) {
        final Week nowParisDateTime = DateUtils.getCurrentWeek();
        final Criteria datesCriteria = new Criteria().andOperator(
                Criteria.where("releaseDate").gte(nowParisDateTime.getMonday()),
                Criteria.where("releaseDate").lte(nowParisDateTime.getSunday())
        );

        final Query query = Query.query(datesCriteria);
        final Pageable pageable = new PageRequest(page, countPerPage, Sort.Direction.ASC, "releaseDate");
        return findPaged(pageable, query, Movie.class);
    }
}
