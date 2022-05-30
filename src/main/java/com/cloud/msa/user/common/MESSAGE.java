package com.cloud.msa.user.common;

public interface MESSAGE {
    public static final String MSG_OK = "정상";

    public static final String MSG_OK_PROCESS = "정상처리 되었습니다.";

    public static final String MSG_ERR_INAPPROPRIATE_VALUE = "정상적인 요청데이터가 아닙니다.";

    public static final String MSG_ERR_LOGIN_FAILED = "잘못된 이메일/패스워드입니다.";

    public static final String MSG_ERR_ERRCNT_FAILED = "접근제한이 걸린 계정입니다.";
}
