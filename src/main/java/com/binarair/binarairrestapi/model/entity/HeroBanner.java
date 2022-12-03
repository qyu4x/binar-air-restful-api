package com.binarair.binarairrestapi.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE hero_banner SET active = FALSE WHERE id = ?")
@Where(clause = "active=true")
public class HeroBanner {

    @Id
    private String id;

    private String title;

    private String imageURL;

    private String description;

    private boolean active = Boolean.TRUE;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
