package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Notification;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.request.NotificationRequest;
import com.binarair.binarairrestapi.model.response.NotificationDetailResponse;
import com.binarair.binarairrestapi.model.response.NotificationResponse;
import com.binarair.binarairrestapi.repository.NotificationRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import com.binarair.binarairrestapi.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final static Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public NotificationDetailResponse getAllByUserId(String userId) {
        boolean isExists = userRepository.existsById(userId);
        if (!isExists) {
            throw new DataNotFoundException("User not found");
        }
        List<Notification> notifications = notificationRepository.findAllNotificationByUserId(userId);
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        if (notifications.isEmpty()) {
            log.info("Notification is empty");
            return NotificationDetailResponse.builder()
                    .unreadCount(0)
                    .notifications(notificationResponses)
                    .build();
        }
        Integer unreadNotification = notificationRepository.findUnreadNotification();
        notifications.stream().forEach(notification -> {
            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .description(notification.getDetail())
                    .isRead(notification.isRead())
                    .createdAt(notification.getCreatedAt())
                    .updatedAt(notification.getUpdatedAt())
                    .build();
            notificationResponses.add(notificationResponse);
        });
        log.info("Successful get all notification");
        return NotificationDetailResponse.builder()
                .unreadCount(unreadNotification)
                .notifications(notificationResponses)
                .build();
    }

    @Override
    public NotificationResponse updateIsRead(String userId,String notificationId) {
        log.info("Do update is read notification");
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Notification with id %s not found", notificationId)));
        notification.setRead(true);
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        log.info("Successful update notifications");
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .description(notification.getDetail())
                .isRead(notification.isRead())
                .createdAt(notification.getUpdatedAt())
                .build();
    }

    @Override
    public void pushNotification(NotificationRequest notificationRequest, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User account not found"));
        Notification notification = Notification.builder()
                .id(String.format("nn-%s", UUID.randomUUID().toString()))
                .user(user)
                .title(notificationRequest.getTitle())
                .detail(notificationRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save notification data");
        notificationRepository.save(notification);
        log.info("Successful save notification data");
    }
}
