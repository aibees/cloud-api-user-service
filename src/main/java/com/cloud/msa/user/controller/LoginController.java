package com.cloud.msa.user.controller;

import com.cloud.msa.user.common.vo.Response;
import com.cloud.msa.user.domain.dto.LoginDto;
import com.cloud.msa.user.domain.entities.UserMaster;
import com.cloud.msa.user.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/{system}")
    public ResponseEntity<Response> getLoginRequest(
            @RequestBody LoginDto dto ) {

        System.out.println(dto);


        return loginService.loginProcess(dto);
    }

    @PostMapping("/password")
    public ResponseEntity<Response> changePassword(
            @RequestBody LoginDto dto ) {

        return loginService.changePassword(dto);
    }
}
