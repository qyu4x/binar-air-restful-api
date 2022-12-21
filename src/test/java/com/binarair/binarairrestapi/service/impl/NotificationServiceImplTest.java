package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Notification;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.request.NotificationRequest;
import com.binarair.binarairrestapi.model.response.NotificationDetailResponse;
import com.binarair.binarairrestapi.model.response.NotificationResponse;
import com.binarair.binarairrestapi.repository.NotificationRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testGetAllNotificationByUserIdSuccess() {
        String userId = "rendom-user-id";
        Notification notification = Notification.builder()
                .id("random-id")
                .title("best romance anime")
                .detail("best romance anime is ...")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);
        Mockito.when(notificationRepository.findAllNotificationByUserId(userId))
                .thenReturn(notifications);
        Mockito.when(notificationRepository.findUnreadNotification(userId))
                .thenReturn(1);

        NotificationDetailResponse notificationDetailResponses = notificationService.getAllByUserId(userId);
        Assertions.assertNotNull(notificationDetailResponses);
        Assertions.assertEquals(1, notificationDetailResponses.getUnreadCount());

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(notificationRepository).findAllNotificationByUserId(userId);
        Mockito.verify(notificationRepository).findUnreadNotification(userId);
    }

    @Test
    void testGetAllNotificationByUserIdButEmptySuccess() {
        String userId = "random-user-id";
        List<Notification> notifications = new ArrayList<>();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);
        Mockito.when(notificationRepository.findAllNotificationByUserId(userId))
                .thenReturn(notifications);

        NotificationDetailResponse notificationDetailResponses = notificationService.getAllByUserId(userId);
        Assertions.assertNotNull(notificationDetailResponses);
        Assertions.assertEquals(0, notificationDetailResponses.getUnreadCount());

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(notificationRepository).findAllNotificationByUserId(userId);
    }

    @Test
    void testGetAllNotificationByUserIdNotFound() {
        String userId = "random-user-id";
        List<Notification> notifications = new ArrayList<>();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
           notificationService.getAllByUserId(userId);
        });

        Mockito.verify(userRepository).existsById(userId);
    }


    @Test
    void testUpdateIsReadSuccess() {
        String userId = "random-user-id";
        String notificationId = "random-notification-id";

        Notification notification = Notification.builder()
                .id("random-id")
                .title("best romance anime")
                .detail("best romance anime is ...")
                .createdAt(LocalDateTime.now())
                .build();
        Mockito.when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));
        Mockito.when(notificationRepository.save(ArgumentMatchers.any(Notification.class)))
                .thenReturn(notification);

        NotificationResponse notificationResponse = notificationService.updateIsRead(userId, notificationId);
        Assertions.assertNotNull(notificationResponse);
        Assertions.assertTrue(notificationResponse.isRead());

        Mockito.verify(notificationRepository).findById(notificationId);
        Mockito.verify(notificationRepository).save(ArgumentMatchers.any(Notification.class));

    }

    @Test
    void testUpdateIsReadNotFound() {
        String userId = "random-user-id";
        String notificationId = "random-notification-id";

        Mockito.when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            notificationService.updateIsRead(userId, notificationId);
        });

        Mockito.verify(notificationRepository).findById(notificationId);

    }

    @Test
    void testPushNotificationSuccess() {
        String userId = "userId";

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("best romance anime")
                .description("best romance anime is ...")
                .build();

        Notification notification = Notification.builder()
                .id("random-id")
                .title(notificationRequest.getTitle())
                .detail(notificationRequest.getDescription())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        User user  = User.builder()
                .id("random-user-id")
                .fullName("Kaguya shinomiya")
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(notificationRepository.save(ArgumentMatchers.any(Notification.class)))
                .thenReturn(notification);

        notificationService.pushNotification(notificationRequest, userId);

        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(notificationRepository).save(ArgumentMatchers.any(Notification.class));

    }

    @Test
    void testPushNotificationNotFound() {
        String userId = "userId";
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("best romance anime")
                .description("best romance anime is ...")
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            notificationService.pushNotification(notificationRequest, userId);
        });

        Mockito.verify(userRepository).findById(userId);

    }
}