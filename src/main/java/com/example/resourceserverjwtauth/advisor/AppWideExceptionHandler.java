package com.example.resourceserverjwtauth.advisor;

import com.example.resourceserverjwtauth.dto.APIResponse;
import com.example.resourceserverjwtauth.dto.ErrorDTO;
import com.example.resourceserverjwtauth.enums.APIResponseStatus;
import com.example.resourceserverjwtauth.exception.InternalServerErrorException;
import com.example.resourceserverjwtauth.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });
        serviceResponse.setStatus(APIResponseStatus.ERROR.name());
        serviceResponse.setErrors(errors);
        return serviceResponse;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public APIResponse<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        ErrorDTO errorDTO = new ErrorDTO("username", exception.getMessage());
        errors.add(errorDTO);
        serviceResponse.setStatus(APIResponseStatus.ERROR.name());
        serviceResponse.setErrors(errors);
        return serviceResponse;
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<?> handleInternalServerErrorException(InternalServerErrorException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        ErrorDTO errorDTO = new ErrorDTO("internal server error", exception.getMessage());
        errors.add(errorDTO);
        serviceResponse.setStatus(APIResponseStatus.ERROR.name());
        serviceResponse.setErrors(errors);
        return serviceResponse;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse<?> handleBadCredentialsException(BadCredentialsException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        ErrorDTO errorDTO = new ErrorDTO("bad credentials", exception.getMessage());
        errors.add(errorDTO);
        serviceResponse.setStatus(APIResponseStatus.ERROR.name());
        serviceResponse.setErrors(errors);
        return serviceResponse;
    }

}
