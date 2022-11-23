package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.response.UserRegisterResponse;

public interface UserService {

    UserRegisterResponse save(UserRegisterRequest userRegisterRequest);

}
