package com.binarair.binarairrestapi.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE users SET active = FALSE WHERE id = ?")
@Table(name = "users")
@Where(clause = "active=true")
public class User {

    @Id
    private String id;

    private String imageURL;

    private String fullName;

    private String email;

    private String password;

    private LocalDate birthDate;

    private String gender;

    private boolean active = Boolean.TRUE;

    @ManyToOne
    @JoinColumn(name = "city_code_id", referencedColumnName = "codeId")
    private City city;

    @ManyToOne
    @JoinColumn(name = "role_name_id", referencedColumnName = "role")
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Passenger> passengers;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
