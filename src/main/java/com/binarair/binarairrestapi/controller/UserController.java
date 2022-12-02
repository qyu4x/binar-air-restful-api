package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.HeroBannerRequest;
import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.request.UserUpdateRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.service.UserService;
import com.binarair.binarairrestapi.util.MapperHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<WebResponse<UserRegisterResponse>> signup(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        log.info("Call signup controller");
        UserRegisterResponse userRegisterResponse = userService.save(userRegisterRequest);
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                userRegisterResponse
        );
        log.info("Successful user registration");
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<WebResponse<UserUpdateResponse>> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Call update controller - user");
        UserUpdateResponse userUpdateResponse = userService.update(userUpdateRequest);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userUpdateResponse
        );
        log.info("Successful update user account");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }

    @PutMapping(path = "/avatar",
            consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<UserProfileResponse>> uploadProfile(@Valid @RequestPart("userId")String userId, @RequestPart("userProfileImage") MultipartFile userProfileImage) throws JsonProcessingException {
        log.info("Calling controller upload - hero banner");
        UserProfileResponse userProfileResponse = userService.updateProfile(userId, userProfileImage);
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                userProfileResponse
        );
        log.info("Successful upload hero banner");
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{userId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_BUYER')")
    public ResponseEntity<WebResponse<Boolean>> delete(@Valid @PathVariable("userId") String userId) {
        log.info("Call delete controller - user");
        Boolean deleteStatus = userService.delete(userId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete user account");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }

    @ResponseBody
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<UserResponse>> findUserById(@PathVariable("userId") String userId) {
        log.info("Call controller fund user by id - user");
        UserResponse userResponse = userService.findById(userId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userResponse
        );
        log.info("Successful user account data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
