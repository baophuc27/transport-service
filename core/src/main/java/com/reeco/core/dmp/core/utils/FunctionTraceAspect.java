package com.reeco.core.dmp.core.utils;


import com.reeco.core.dmp.core.model.postgres.FunctionTrace;
import com.reeco.core.dmp.core.repository.postgres.FunctionTraceRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class FunctionTraceAspect {

    @Autowired
    private FunctionTraceRepository functionTraceRepository;

    @Around("@annotation(com.reeco.core.dmp.core.annotations.Traceable)") // Assumes a custom annotation named Traceable
    public Object traceFunction(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            FunctionTrace trace = new FunctionTrace();
            trace.setFunctionName(joinPoint.getSignature().toShortString());
            trace.setExecutionTimeMilliseconds(executionTime);
            trace.setTimestamp(LocalDateTime.now());

            functionTraceRepository.save(trace);
        }
    }
}
