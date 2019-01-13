package no.toreb.hateoasapi.api.common.controller.advice;

import no.toreb.hateoasapi.api.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException(
            final HttpMediaTypeNotAcceptableException ex,
            final HttpServletRequest request) {

        final HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        final String message = "Allowed MediaTypes: " + MediaType.toString(ex.getSupportedMediaTypes());
        return new ResponseEntity<>(new ErrorResponse(System.currentTimeMillis(),
                                                      status,
                                                      message,
                                                      request.getRequestURI()), status);
    }

}
