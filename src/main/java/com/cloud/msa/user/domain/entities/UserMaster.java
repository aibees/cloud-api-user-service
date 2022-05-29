package com.cloud.msa.user.domain.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user_master")
public class UserMaster {
    @Id
    @Column(name="uuid")
    private Long uuid;
    @Column(name="email")
    private String email;

    @Column(name="email_postfix")
    private String emailPostfix;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

//  @OneToMany(mappedBy = "user")
    @OneToMany
    @JoinColumn(name="uuid")
    private List<UserAuthority> authority = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="uuid")
    private UserDetail userDetail;

    @Column(name="create_date")
    private LocalDateTime creatdate;
}
