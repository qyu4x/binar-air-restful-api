package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM notification\n" +
            "WHERE user_unique_id = :userId")
    List<Notification> findAllNotificationByUserId(@Param("userId")String userId);

    @Query(nativeQuery = true,
            value = "SELECT count(*) FROM notification WHERE is_read = false")
    Integer findUnreadNotification();

}
