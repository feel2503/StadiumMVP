package com.thiscat.stadiumamp.system.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RestControllerLoggingAspect {

    private static final Logger logger = LogManager.getLogger(RestControllerLoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Before("restControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); // request 정보를 가져온다.
            logger.info(
                    "\n============== Request Info ==============\n" +
                            "time           : " +getCurrentTime()+"\n"+
                            "remote Address : "+ request.getRemoteAddr()+"\n"+
                            "method         : "+ request.getMethod() + "\n"+
                            "url            : "+ request.getRequestURI() + "\n"+
                            "param          : "+ request.getQueryString() + "\n"+
                            "body           : "+ getParams(joinPoint)+ "\n"+
                            "============================================="
            );

        } catch (Throwable throwable) {
            throw throwable;
        }

    }

    @AfterReturning(pointcut = "restControllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info(
                "\n============== Result Info ==============\n" +
                        "time           : " +getCurrentTime()+"\n"+
                        "param          : "+ joinPoint.getSignature().toShortString()+ "\n"+
                        "body           : "+ getParams(joinPoint)+ "\n"+
                        "============================================="
        );
    }

    @AfterThrowing(pointcut = "restControllerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("‼ Exception in: {} with message={}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage(), ex);
    }


    private Map getParams(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }


    private String getCurrentTime() {
        DateTimeFormatter format = DateTimeFormat.forPattern( "yyyy-MM-dd HH:mm:ss");
        DateTime now = new DateTime(DateTimeZone.getDefault());
        return format.print(now);
    }
}
