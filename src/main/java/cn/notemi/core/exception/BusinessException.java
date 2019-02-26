package cn.notemi.core.exception;

import cn.notemi.common.ErrorModel;

public class BusinessException extends Exception {
    private ErrorModel error = new ErrorModel();

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.error.setMessage(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.error.setMessage(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.error.setMessage(message);
    }

    public BusinessException(String message, ErrorModel error) {
        super(message);
        this.error = error;
    }

    public ErrorModel getFieldError() {
        return this.error;
    }
}
