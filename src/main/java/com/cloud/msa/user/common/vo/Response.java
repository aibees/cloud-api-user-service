package com.cloud.msa.user.common.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Response {

    private String message;
    private Object data;
}
