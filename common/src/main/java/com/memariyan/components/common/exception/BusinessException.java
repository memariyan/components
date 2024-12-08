package com.memariyan.components.common.exception;

public abstract class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(message);
    }

    public abstract int getErrorCode();
}
