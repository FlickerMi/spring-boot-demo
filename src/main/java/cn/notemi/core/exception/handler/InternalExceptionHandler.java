package cn.notemi.core.exception.handler;

import cn.notemi.common.ErrorModel;
import cn.notemi.common.MessageService;
import cn.notemi.core.exception.BusinessException;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ControllerAdvice
public class InternalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InternalExceptionHandler.class);

    @Autowired
    MessageService messageService;

    @ExceptionHandler({
        NullPointerException.class,
        IOException.class,
        SQLException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public List<ErrorModel> processInternalError(Exception ex) {
        logger.error("Internal server error", ex);
        ErrorModel error = new ErrorModel(messageService.getMessage("error.internal.server.error"));
        return Lists.newArrayList(error);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public List<ErrorModel> processBusinessError(BusinessException ex) {
        logger.warn(ex.getMessage());
        logger.debug(ex.getMessage(), ex);
        return Lists.newArrayList(ex.getFieldError());
    }
}
