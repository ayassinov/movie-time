package com.ninjas.movietime.repository;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author ayassinov on 05/09/2014.
 */
@Repository
public class ImporterRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ImporterRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public void importFromFile(String collectionName, String jsonFile) {
        final DB db = mongoTemplate.getDb();
        DBObject dbObject = (DBObject) JSON.parse("{'name':'mkyong', 'age':30}");
        db.getCollection("dummyColl").save(dbObject);
    }

}
