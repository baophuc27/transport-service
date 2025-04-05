package com.reeco.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final int MAX_PAYLOAD_LENGTH = 10000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());

        // Log the API request
        log.info("API Request: method={}, path={}, parameters={}",
                request.getMethod(), request.getRequestURI(), formatParameters(request.getParameterMap()));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long duration = System.currentTimeMillis() - (long) request.getAttribute("startTime");

        // Only log response body for certain content types if needed



        // If you need to log request/response body content, use this approach:
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = requestWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, MAX_PAYLOAD_LENGTH);
                String payload = new String(buf, 0, length, StandardCharsets.UTF_8);
                log.debug("Request body: {}", payload);
            }
        }

        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            byte[] buf = responseWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, MAX_PAYLOAD_LENGTH);
                String payload = new String(buf, 0, length, StandardCharsets.UTF_8);
                log.debug("Response body: {}", payload);
            }
        }

        // Log API response
        log.info("API Response: method={}, path={}, status={}, durationMs={}",
                request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
    }

    private String formatParameters(Map<String, String[]> parameterMap) {
        if (parameterMap.isEmpty()) {
            return "{}";
        }

        return parameterMap.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    String[] values = entry.getValue();
                    String valueStr = values.length == 1
                            ? values[0]
                            : "[" + String.join(", ", values) + "]";
                    return String.format("\"%s\": \"%s\"", key, valueStr);
                })
                .collect(Collectors.joining(", ", "{", "}"));
    }
}