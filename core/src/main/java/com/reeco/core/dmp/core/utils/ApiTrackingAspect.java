package com.reeco.core.dmp.core.utils;


import com.reeco.core.dmp.core.model.postgres.ApiTracking;
import com.reeco.core.dmp.core.repository.postgres.ApiTrackingRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class ApiTrackingAspect {

    @Autowired
    private ApiTrackingRepository apiTrackingRepository;

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object trackApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Tracking API call");
        long startTime = System.currentTimeMillis();

        Object response = null;
        long duration;
        try {
            response = joinPoint.proceed();
        } finally {
            duration = System.currentTimeMillis() - startTime;
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                HttpServletResponse httpResponse = attributes.getResponse();

                if (httpResponse != null) {
                    ApiTracking tracking = new ApiTracking();
                    tracking.setApiEndpoint(request.getRequestURI());
                    tracking.setStatusCode(httpResponse.getStatus());
                    tracking.setResponseTimeMilliseconds(duration);
                    tracking.setTimestamp(LocalDateTime.now());

                    apiTrackingRepository.save(tracking);
                }
            }
        }

        return response;
    }

}
