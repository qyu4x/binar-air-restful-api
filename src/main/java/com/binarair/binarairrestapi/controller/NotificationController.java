package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.response.NotificationDetailResponse;
import com.binarair.binarairrestapi.model.response.NotificationResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final static Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "get all notification data based on user id")
    @ResponseBody
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<NotificationDetailResponse>> getAll(@PathVariable("id") String id) {
        log.info("Calling controller getAll - notification detail response");
        NotificationDetailResponse notificationDetailResponse = notificationService.getAllByUserId(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                notificationDetailResponse
        );
        log.info("Successful get all notificationdata data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "update notification read status based on user id and notification id")
    @ResponseBody
    @PutMapping("/{userid}/{notificationid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<NotificationResponse>> updateReadStatus(@PathVariable("userid") String userid,@PathVariable("notificationid") String notificationid) {
        log.info("Calling controller getAll - notification detail response");
        NotificationResponse notificationDetailResponse = notificationService.updateIsRead(userid, notificationid);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                notificationDetailResponse
        );
        log.info("Successful update notification data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
