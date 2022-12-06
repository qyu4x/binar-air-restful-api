package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.AgeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgeCategoryRepository extends JpaRepository<AgeCategory, String> {

    Optional<AgeCategory> findByCategoryName(String categoryName);

}
