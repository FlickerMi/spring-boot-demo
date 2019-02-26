package cn.notemi.common;

import lombok.Data;

/**
 * Title：ErrorModel
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/

@Data
public class ErrorModel {
    public static final String DEFAULT_ERROR_FIELD = "Default";
    public static final String DEFAULT_ERROR_MESSAGE = "Internal server error";

    private String field = DEFAULT_ERROR_FIELD;
    private String message = DEFAULT_ERROR_MESSAGE;

    public ErrorModel() {}

    public ErrorModel(String message) {
        this.message = message;
    }

    public ErrorModel(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
