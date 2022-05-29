package com.cloud.msa.user.service;

import com.cloud.msa.user.common.MESSAGE;
import com.cloud.msa.user.common.utils.DataUtilService;
import com.cloud.msa.user.common.vo.Response;
import com.cloud.msa.user.domain.dto.LoginDto;
import com.cloud.msa.user.domain.entities.UserAuthority;
import com.cloud.msa.user.domain.entities.UserDetail;
import com.cloud.msa.user.domain.entities.UserMaster;
import com.cloud.msa.user.domain.repo.UserDetailRepository;
import com.cloud.msa.user.domain.repo.UserRepository;
import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {

    private UserRepository userRepository;
    private final UserDetailRepository detailRepository;
    private final PasswordEncoder passwordEncoder;

    private final DataUtilService utilService;

    public ResponseEntity<Response> loginProcess(LoginDto dto) {
        log.info(dto.toString());


        // 1. email check
        String[] email_parse = dto.getEmail().split("@");
        if(email_parse.length != 2) {
            //HttpStatus.SERVICE_UNAVAILABLE
            return utilService.returnResult(
                    HttpStatus.BAD_REQUEST,
                    MESSAGE.MSG_ERR_INAPPROPRIATE_VALUE,
                    dto.getEmail()
            );
        }

        UserMaster user = userRepository.findByEmailAndEmailPostfix(email_parse[0], email_parse[1]);

        AtomicBoolean systemCheck = new AtomicBoolean(false);

        systemCheck.set(user.getAuthority().parallelStream()
                .anyMatch( data -> data.getAuthority().equals(dto.getSystem()) ));

        System.out.println("Can I access to System? : " + systemCheck);

        boolean password_chekger = this.checkPassword(dto, user);
        System.out.println("Password Checker : " + password_chekger);

        return utilService.returnOKResult(
                MESSAGE.MSG_OK,
                user
        );
    }

    private String encodePassword(String password, LocalDateTime updateTime) {
        String str_uptTime = updateTime.toString();
        log.info("encodePassword_join : " + String.join("_", password, str_uptTime));
        return passwordEncoder.encode(String.join("_", password, str_uptTime));
    }

    public boolean checkPassword(LoginDto dto, UserMaster user) {
        return passwordEncoder.matches(dto.getPassword(), user.getUserDetail().getPassword());
    }

    public ResponseEntity<Response> changePassword(LoginDto dto) {
        //salt로 updateTime을 넣을거야
        LocalDateTime updateTime = LocalDateTime.now();
        String encoded = encodePassword(dto.getPassword(), updateTime);
        log.info("double_encoded : " + encoded);

        String[] emails = dto.getEmail().split("@");
        UserDetail updatedDetail = userRepository.findByEmailAndEmailPostfix(emails[0], emails[1]).getUserDetail();
        updatedDetail.setPassword(encoded);

        UserDetail savedDetail = detailRepository.save(updatedDetail);

        return utilService.returnOKResult(savedDetail);
    }
}
