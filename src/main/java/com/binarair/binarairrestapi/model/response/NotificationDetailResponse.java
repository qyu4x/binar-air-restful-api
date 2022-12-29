package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDetailResponse {

    private Integer unreadCount;

    List<NotificationResponse> notifications;

}
