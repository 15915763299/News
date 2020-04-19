package com.demo.news.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有请求的日志打印
 */
@Aspect
@Component
public class RequestLogAop {
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "execution(* com.demo.news.controller..*(..))")
    private void cutRequestLog() {

    }

    @Before("cutRequestLog()")
    public void beforeMethod(JoinPoint jp) {
        startTime.set(System.currentTimeMillis());
        Object[] args = jp.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        printContent(stringBuilder, args);
//        String methodName = jp.getSignature().getName();
//        logger.info("request method 【{}】 params: {}",methodName, stringBuilder.toString());
    }

    @AfterReturning(value = "cutRequestLog()", returning = "result")
    public void afterReturningMethod(JoinPoint jp, Object result) {
        String resultContent = "";
        if (null != result) {
            try {
                resultContent = new ObjectMapper().writeValueAsString(result);
            } catch (JsonProcessingException e) {
                resultContent = "";
            }
        }
        logger.info("request method 【{}】 time:{} response: {}", jp.getSignature().getName(), System.currentTimeMillis() - startTime.get(), resultContent);
    }

    @AfterThrowing(value = "cutRequestLog()", throwing = "e")
    public void afterThrowingMethod(JoinPoint jp, Exception e) {
        Object[] args = jp.getArgs();
        if (args.length > 0) {
            String methodName = jp.getSignature().getName();
            logger.error("request method 【{}】 response exception :{}", methodName, e.getMessage());
        }
    }

    private void printContent(StringBuilder stringBuilder, Object[] args) {
        for (Object object : args) {
            if ((object instanceof HttpServletRequest) || (object instanceof HttpServletResponse)
                    || (object instanceof Model) || (object instanceof Session) || (object instanceof MultipartFile)) {
                continue;
            }
            try {
                stringBuilder.append(new ObjectMapper().writeValueAsString(object));
                stringBuilder.append(",");
            } catch (Exception e) {
                stringBuilder.append(object);
                stringBuilder.append(",");
            }
        }
    }
}
