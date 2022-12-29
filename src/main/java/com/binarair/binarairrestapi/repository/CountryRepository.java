package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {
}
