package com.ninjas.movietime.conf;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.ninjas.movietime.MovieTimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * @author ayassinov on 26/08/2014.
 */
@Configuration
public class MongoConfig {

    @Autowired
    private MovieTimeConfig config;

    /**
     * @return MongoDB factory to get a mongo client connection.
     * @throws Exception When the URL mongo server is empty/null or Malformed
     */
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        //parse the uri
        final MongoClientURI mongoClientURI = new MongoClientURI(config.getMongo().getUrl());
        //create mongodb client
        final MongoClient mongoClient = new MongoClient(mongoClientURI);
        //create spring mongodbFactory
        return new SimpleMongoDbFactory(mongoClient, mongoClientURI.getDatabase());
    }

    /**
     * @return Spring Mongo Template
     * @throws Exception if the MongoDB factory creation was not successfully
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
