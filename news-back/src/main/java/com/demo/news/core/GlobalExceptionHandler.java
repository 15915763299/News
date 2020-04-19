package com.demo.news.core;

import com.demo.news.Code;
import com.demo.news.core.exception.LogicException;
import com.demo.news.entity.base.BaseRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 统一异常捕获
 *
 * @author 尉涛
 * @date 2020-01-20 10:30
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 逻辑异常
     */
    @ExceptionHandler({LogicException.class})
    @ResponseBody
    public BaseRes logicExceptionHandler(LogicException ex) {
        logger.error(ex.getCode() + ":" + ex.getMsg(), ex);
        return new BaseRes(ex.getCode(), ex.getMsg());
    }

    /**
     * 参数校验异常@Min、@Max等
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public BaseRes constraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
        String info = "";
        if (set.size() > 0) {
            info = set.iterator().next().getMessage();
        }
        return new BaseRes(Code.PARAMS_ERROR, info);
    }

    /**
     * 方法参数校验异常@Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseRes methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new BaseRes(Code.PARAMS_ERROR, ex.getMessage());
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseRes exception(Exception ex) {
        ex.printStackTrace();
        return new BaseRes(Code.SERVER_UNKNOWN_ERROR, "服务器未知异常");
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public BaseRes missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return new BaseRes(Code.PARAMS_ERROR, "MissingServletRequestParameterException");
    }
}
