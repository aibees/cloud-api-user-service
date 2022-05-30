package com.cloud.msa.user.service;

import com.cloud.msa.user.common.MESSAGE;
import com.cloud.msa.user.common.VALUE;
import com.cloud.msa.user.common.utils.DataUtilService;
import com.cloud.msa.user.common.vo.Response;
import com.cloud.msa.user.domain.dto.LoginDto;
import com.cloud.msa.user.domain.entities.UserDetail;
import com.cloud.msa.user.domain.entities.UserMaster;
import com.cloud.msa.user.domain.repo.UserDetailRepository;
import com.cloud.msa.user.domain.repo.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final UserDetailRepository detailRepository;
    private final PasswordEncoder passwordEncoder;

    private final DataUtilService utilService;

    public ResponseEntity<Response> loginProcess(LoginDto dto) {
        log.info(dto.toString());

        // 1. email check
        String[] email_parse = dto.getEmail().split(VALUE.AUTH_PARSER);
        if(email_parse.length != 2) {
            //HttpStatus.SERVICE_UNAVAILABLE
            return utilService.returnResult(
                    HttpStatus.BAD_REQUEST,
                    MESSAGE.MSG_ERR_INAPPROPRIATE_VALUE,
                    dto.getEmail()
            );
        }

        UserMaster user = userRepository.findByEmailAndEmailPostfix(email_parse[0], email_parse[1]);

        // 2. Error Count 체크
        if(user.getUserDetail().getErrcnt() > 4) {
            return utilService.returnResult(
                    HttpStatus.BAD_REQUEST,
                    MESSAGE.MSG_ERR_ERRCNT_FAILED,
                    dto.getEmail()
            );
        }

        // 3. 접근하려는 api가 허용되는지
        AtomicBoolean systemCheck = new AtomicBoolean(false);
        systemCheck.set(user.getAuthority()
                            .parallelStream()
                            .anyMatch( data -> data.getAuthority().equals(dto.getSystem()) ));

        // 4. 패스워드 확인
        // OTP 값을 salting 값으로 한다. 어차피 회원가입 받을 것도 아니니
        if(!this.checkPassword(dto, user)) {
            // errcnt update
            detailRepository.save(
                    UserDetail
                            .builder()
                            .uuid(user.getUserDetail().getUuid())
                            .password(user.getUserDetail().getPassword())
                            .errcnt(user.getUserDetail().getErrcnt()+1)
                            .otp(user.getUserDetail().getOtp())
                            .build()
            );

            return utilService.returnResult(
                    HttpStatus.BAD_REQUEST,
                    MESSAGE.MSG_ERR_LOGIN_FAILED,
                    dto.getEmail()
            );
        }

        HttpHeaders headers = new HttpHeaders();
        //TODO : JWT 생성로직 추가
        headers.add(VALUE.AUTH_HEADER, "jwt");

        return utilService.returnOKResult(
                MESSAGE.MSG_OK,
                user,
                headers
        );
    }

    private String encodePassword(String password) {
        return this.encodePassword(password, null);
    }

    private String encodePassword(String password, String salt) {
        // password_salt
        String target = String.join(VALUE.AUTH_DELIMETER, password, (salt == null)?"" : salt);
        return passwordEncoder.encode(target);
    }

    public boolean checkPassword(LoginDto dto, UserMaster user) {
        return passwordEncoder.matches(
                String.join(VALUE.AUTH_DELIMETER, dto.getPassword(), user.getUserDetail().getOtp()),
                user.getUserDetail().getPassword());
    }

    public ResponseEntity<Response> changePassword(LoginDto dto) {
        //salt로 updateTime을 넣을거야 -> OTP가 된다.
        LocalDateTime updateTime = LocalDateTime.now();
        System.out.println(updateTime.toString());

        String otp = encodePassword(updateTime.toString());
        String encoded = encodePassword(dto.getPassword(), otp);

        String[] emails = dto.getEmail().split(VALUE.AUTH_PARSER);
        UserDetail updatedDetail = userRepository.findByEmailAndEmailPostfix(emails[0], emails[1]).getUserDetail();
        updatedDetail.setPassword(encoded);
        updatedDetail.setOtp(otp);

        UserDetail savedDetail = detailRepository.save(updatedDetail);

        return utilService.returnOKResult(savedDetail);
    }
}
