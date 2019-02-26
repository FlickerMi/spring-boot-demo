package cn.notemi.core.exception.handler;

import cn.notemi.common.ErrorModel;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorModel> processValidationError(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed");
        logger.debug(ex.getMessage(), ex);

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return this.processFieldError(fieldErrors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<Error> processArgumentError(IllegalArgumentException ex) {
        logger.warn("Illegal argument");
        logger.debug(ex.getMessage(), ex);
        ex.printStackTrace();

        Error error = new Error(ex.getMessage());
        return Lists.newArrayList(error);
    }

    private List<ErrorModel> processFieldError(List<FieldError> fieldErrors) {
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return null;
        }
        return fieldErrors.stream()
            .map(fieldError -> new ErrorModel(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    }
}
