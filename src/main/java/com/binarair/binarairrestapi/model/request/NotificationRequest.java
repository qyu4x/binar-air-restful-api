package com.binarair.binarairrestapi.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {

    private String title;

    private String description;

}
