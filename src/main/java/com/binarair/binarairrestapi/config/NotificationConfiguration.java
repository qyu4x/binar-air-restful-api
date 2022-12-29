package com.binarair.binarairrestapi.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class NotificationConfiguration {

    @Value("${notification.order.title}")
    private String title;

    @Value("${notification.order.description}")
    private String description;

}
