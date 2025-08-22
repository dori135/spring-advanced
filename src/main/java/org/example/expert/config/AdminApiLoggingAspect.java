package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.user.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AdminApiLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AdminApiLoggingAspect.class);
    private final ObjectMapper objectMapper=new ObjectMapper();

    // 어드민 API 메서드 실행 전후에 요청/응답 데이터를 로깅
    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Long userId = (Long) request.getAttribute("userId");
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        String methodName = joinPoint.getSignature().toShortString();
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());

        logger.info("[Admin API Request] userId={}, role={}, method={}, time={}, body={}",
                userId, userRole, methodName, LocalDateTime.now(), requestBody);

        Object result = joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(result);
        logger.info("[Admin API Response] userId={}, method={}, response={}",
                userId, methodName, responseBody);

        return result;
    }
}
