package com.binarair.binarairrestapi.service;
import com.binarair.binarairrestapi.model.request.TitelRequest;
import com.binarair.binarairrestapi.model.response.TitelResponse;

import java.util.List;
public interface TitelService {
    TitelResponse save(TitelRequest titelRequest);

    List<TitelResponse> getAll();

}
