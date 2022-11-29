package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.NotificationRequest;
import com.binarair.binarairrestapi.model.response.NotificationDetailResponse;
import com.binarair.binarairrestapi.model.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationDetailResponse getAllByUserId(String userId);

    NotificationResponse updateIsRead(String userId, String notificationId);

    void pushNotification(NotificationRequest notificationRequest, String userId);



}
