package com.devchallenge.keyvalueprocessor.exception.handler;


import com.devchallenge.keyvalueprocessor.exception.ValidationException;
import com.devchallenge.keyvalueprocessor.exception.dto.GenericError;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public GenericError unhandledError(Exception ex, HttpServletResponse response) {
        String stackTrace = ExceptionUtils.getFullStackTrace(ex);
        logger.error("error occurred: " + stackTrace);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new GenericError(ex.getMessage(), ExceptionUtils.getFullStackTrace(ex));
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public GenericError handleValidationError(ValidationException ex, HttpServletResponse response) {
        logger.info("handling validation exception: " + ExceptionUtils.getFullStackTrace(ex));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new GenericError(ex.getMessage(), ExceptionUtils.getFullStackTrace(ex));
    }

}
