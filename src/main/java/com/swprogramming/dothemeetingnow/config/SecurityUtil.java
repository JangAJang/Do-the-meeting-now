package com.swprogramming.dothemeetingnow.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Long getCurrentMemberId(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || authentication.getName() == null){
            throw new RuntimeException("Security Context에 인증 정보가 존재하지 않습니다. ");
        }
        return Long.parseLong(authentication.getName());
    }
}
