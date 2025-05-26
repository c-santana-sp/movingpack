package com.movingpack.movingpack.postalcode.history;

import org.springframework.stereotype.Service;

@Service
public class PostalCodeHistoryService {

    private final PostalCodeHistoryRepository repository;

    public PostalCodeHistoryService(PostalCodeHistoryRepository repository) {
        this.repository = repository;
    }

    public void save(PostalCodeHistory toSave) {
        repository.save(toSave);
    }
}
