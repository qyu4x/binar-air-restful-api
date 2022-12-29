package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AgeCategoryRequest;
import com.binarair.binarairrestapi.model.response.AgeCategoryResponse;

import java.util.List;

public interface AgeCategoryService {

    AgeCategoryResponse save(AgeCategoryRequest ageCategoryRequest);

    List<AgeCategoryResponse> getAll();

    AgeCategoryResponse findById(String ageCategoryId);

    Boolean delete(String ageCategoryId);

}
