package org.asname.mvc.controllers;

import org.asname.controllers.dto.ErrorDTO;
import org.asname.controllers.dto.ErrorResponseDTO;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception e, WebRequest request) throws IOException {
        return new ResponseEntity<>(new ErrorResponseDTO(new ErrorDTO("1", e.getMessage(),
                new IntegrationService().getExceptionString(e))),
                HttpStatus.OK);
    }
}