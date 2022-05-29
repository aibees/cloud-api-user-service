package com.cloud.msa.user.domain.entities.pk;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityPK implements Serializable {
    private Long uuid;
    private Long seq;
}
