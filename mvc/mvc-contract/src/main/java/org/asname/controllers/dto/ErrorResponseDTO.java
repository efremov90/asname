package org.asname.controllers.dto;

public class ErrorResponseDTO {
    ErrorDTO Error;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(ErrorDTO error) {
        Error = error;
    }

    public ErrorDTO getError() {
        return Error;
    }

    public void setError(ErrorDTO error) {
        Error = error;
    }
}
