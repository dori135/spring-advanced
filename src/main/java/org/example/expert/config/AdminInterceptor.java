package org.example.expert.config;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

// interceptor 방식
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse httpResponse,
                             @NonNull Object handler) throws IOException {

        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        // 1. ADMIN 권한 체크
        if (userRole != UserRole.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "ADMIN만 접근할 수 있습니다.");
            return false;
        }

        Long userId = (Long) request.getAttribute("userId");

        // 2. 접근 로그 기록
        logger.info("[Admin Interceptor] userId={}, time={}, url={}",
                userId, LocalDateTime.now(), request.getRequestURI());

        return true;
    }
}
