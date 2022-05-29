package com.cloud.msa.user.domain.entities;

import com.cloud.msa.user.domain.entities.pk.UserAuthorityPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@Getter
@EqualsAndHashCode
@Table(name="user_authority")
@IdClass(UserAuthorityPK.class)
public class UserAuthority {
    @Id
    @Column(name="uuid")
    private Long uuid;

    @Id
    @Column(name="seq")
    private Long seq;

    @Column(name="authority")
    private String authority;
}
