package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, String> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM city WHERE country_code_id = :countryId"
    )
    List<City> findAllByCountry(String countryId);

}
