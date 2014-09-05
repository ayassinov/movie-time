package com.ninjas.movietime.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjas.movietime.repository.ImporterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ayassinov on 05/09/2014.
 */
@Service
public class ImporterService {

    private final ObjectMapper mapper;
    private final ImporterRepository repository;

    @Autowired
    public ImporterService(ObjectMapper mapper, ImporterRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public void saveTo() {
        //read file
        //serialize with jackson
        //save to database
    }
}
