package com.cloud.msa.user.common.utils;

import com.cloud.msa.user.common.MESSAGE;
import com.cloud.msa.user.common.vo.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DataUtilService {

    public ResponseEntity<Response> returnResult(HttpStatus status, String msg, Object data, HttpHeaders headers) {
        return new ResponseEntity<Response>(
                Response.builder()
                        .message(msg)
                        .data(data)
                        .build()
                ,headers
                ,status
        );
    }

    public ResponseEntity<Response> returnResult(HttpStatus status, String msg, Object data) {
        return this.returnResult(status, msg, data, null);
    }

    public ResponseEntity<Response> returnOKResult(String msg, Object data, HttpHeaders headers) {
        return this.returnResult(HttpStatus.OK, msg, data, headers);
    }

    public ResponseEntity<Response> returnOKResult(String msg, Object data) {
        return this.returnResult(HttpStatus.OK, msg, data, null);
    }

    public ResponseEntity<Response> returnOKResult(Object data) {
        return this.returnResult(HttpStatus.OK, MESSAGE.MSG_OK, data, null);
    }
}
