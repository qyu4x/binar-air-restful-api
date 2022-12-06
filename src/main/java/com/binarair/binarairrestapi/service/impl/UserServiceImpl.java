package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.request.UserUpdateRequest;
import com.binarair.binarairrestapi.model.response.UserProfileResponse;
import com.binarair.binarairrestapi.model.response.UserRegisterResponse;
import com.binarair.binarairrestapi.model.response.UserResponse;
import com.binarair.binarairrestapi.model.response.UserUpdateResponse;
import com.binarair.binarairrestapi.repository.CityRepository;
import com.binarair.binarairrestapi.repository.RoleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import com.binarair.binarairrestapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final CityRepository cityRepository;

    private final FirebaseStorageFileService firebaseStorageFileService;

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CityRepository cityRepository, FirebaseStorageFileService firebaseStorageFileService, PasswordEncoderConfiguration passwordEncoderConfiguration) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.firebaseStorageFileService = firebaseStorageFileService;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }

    @Override
    public UserRegisterResponse save(UserRegisterRequest userRegisterRequest) {
        Role buyerRole = roleRepository.findById(RoleType.BUYER)
                .orElseThrow(() -> new DataNotFoundException("Role buyers are not available"));
        Optional<User> isUserExist = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (!isUserExist.isEmpty()) {
           throw new DataAlreadyExistException(String.format("User with email %s already available", userRegisterRequest.getEmail()));
        }

        User user = User.builder()
                .id(String.format("ur-%s", UUID.randomUUID().toString()))
                .fullName(userRegisterRequest.getFullName())
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoderConfiguration.passwordEncoder().encode(userRegisterRequest.getPassword()))
                .role(buyerRole)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        log.info("Successfully registered user with name {} ", userRegisterRequest.getFullName());
        return UserRegisterResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(buyerRole.getRole().name())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public UserUpdateResponse update(UserUpdateRequest userUpdateRequest, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Account with id %s not found", userId)));

        City city = cityRepository.findById(userUpdateRequest.getCityId())
                        .orElseThrow(() -> new DataNotFoundException(String.format("City with city id %s not found", userUpdateRequest.getCityId())));

        user.setFullName(userUpdateRequest.getFullName());
        user.setBirthDate(userUpdateRequest.getBirthDate());
        user.setGender(userUpdateRequest.getGender());
        user.setCity(city);
        user.setUpdatedAt(LocalDateTime.now());
        User userUpdate = userRepository.save(user);
        log.info("Successful update user account");
        return UserUpdateResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(userUpdate.getGender())
                .birthdate(userUpdate.getBirthDate())
                .role(user.getRole().getRole().name())
                .cityCode(userUpdate.getCity().getCodeId())
                .city(userUpdate.getCity().getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserProfileResponse updateProfile(String userId, MultipartFile multipartFile) {
        User userResponse = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User account not found"));
        if (multipartFile.isEmpty()) {
            log.warn("image is empty");
            throw new DataNotFoundException("Please choose a picture first");
        }

        userResponse.setImageURL(firebaseStorageFileService.doUploadFile(multipartFile));
        userRepository.save(userResponse);
        log.info("Successfull update profile image");
        return UserProfileResponse.builder()
                .id(userResponse.getId())
                .imageURL(userResponse.getImageURL())
                .fullName(userResponse.getFullName())
                .email(userResponse.getEmail())
                .createdAt(userResponse.getCreatedAt())
                .updatedAt(userResponse.getUpdatedAt())
                .build();
    }

    @Override
    public Boolean delete(String userId) {
        boolean isExists = userRepository.existsById(userId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("User account with is %s not found", userId));
        }
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public UserResponse findById(String userId) {
        log.info("Do get user by id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("User account with id %s not found", userId)));
        log.info("Successful get user by id");
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getRole().name())
                .gender(user.getGender())
                .birthdate(user.getBirthDate())
                .imageURL(user.getImageURL())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
