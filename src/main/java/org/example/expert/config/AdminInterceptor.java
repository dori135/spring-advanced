package org.example.expert.config;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        // 1. ADMIN 권한 체크
        if (userRole != UserRole.ADMIN) {
            throw new RuntimeException("ADMIN만 접근할 수 있습니다.");
        }

        Long userId = (Long) request.getAttribute("userId");

        // 2. 접근 로그 기록
        logger.info("[Admin Interceptor] userId={}, time={}, url={}",
                userId, LocalDateTime.now(), request.getRequestURI());

        return true;
    }
}
