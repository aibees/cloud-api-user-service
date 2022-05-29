package com.cloud.msa.user.domain.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserData {
    private String email;
    private String session_key;
    private boolean login_flag;
}
