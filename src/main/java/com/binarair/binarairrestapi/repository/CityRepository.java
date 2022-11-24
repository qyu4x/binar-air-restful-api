package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, String> {
}
