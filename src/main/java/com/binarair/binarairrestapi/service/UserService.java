package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.request.UserUpdateRequest;
import com.binarair.binarairrestapi.model.response.UserProfileResponse;
import com.binarair.binarairrestapi.model.response.UserRegisterResponse;
import com.binarair.binarairrestapi.model.response.UserUpdateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserRegisterResponse save(UserRegisterRequest userRegisterRequest);

    UserUpdateResponse update(UserUpdateRequest userUpdateRequest);

    UserProfileResponse updateProfile(String userId, MultipartFile multipartFile);

    boolean delete(String userId);


}
