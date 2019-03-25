package com.nuvole.flow.service.exception;

/**
 * Created by chenlong
 * Date：2017/9/12
 * time：11:32
 */
public class InternalServerErrorException extends BaseModelerRestException {
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException() {
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable t) {
        super(message, t);
    }
}
