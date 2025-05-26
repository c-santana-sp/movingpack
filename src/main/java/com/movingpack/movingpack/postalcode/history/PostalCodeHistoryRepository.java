package com.movingpack.movingpack.postalcode.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalCodeHistoryRepository extends JpaRepository<PostalCodeHistory, Long> {
    PostalCodeHistory findByPostalCode(String code);
}
