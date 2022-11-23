package com.binarair.binarairrestapi.model.entity;

import com.binarair.binarairrestapi.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleType role;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
