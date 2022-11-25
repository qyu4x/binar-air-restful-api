package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.request.UserUpdateRequest;
import com.binarair.binarairrestapi.model.response.UserRegisterResponse;
import com.binarair.binarairrestapi.model.response.UserUpdateResponse;

public interface UserService {

    UserRegisterResponse save(UserRegisterRequest userRegisterRequest);

    UserUpdateResponse update(UserUpdateRequest userUpdateRequest);

}
