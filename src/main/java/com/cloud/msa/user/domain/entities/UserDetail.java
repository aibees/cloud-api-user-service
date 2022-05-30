package com.cloud.msa.user.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="user_detail")
public class UserDetail {
    @Id
    private long uuid;

    // length: 45
    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private Integer errcnt;

    @Column
    private String otp;
}
