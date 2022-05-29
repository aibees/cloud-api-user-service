package com.cloud.msa.user.domain.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String system;
    private String email;
    private String password;
}
