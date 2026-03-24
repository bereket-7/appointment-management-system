package com.beki.appointment.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneralException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private HttpStatus status;

    public GeneralException(HttpStatus status, String message) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public GeneralException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
