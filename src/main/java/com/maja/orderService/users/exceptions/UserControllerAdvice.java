package com.maja.orderService.users.exceptions;

import com.maja.orderService.commons.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<Error> handleUserNotFoundException(UserNotFoundException e){
        var error = new Error("USER_NOT_FOUND", "User with id " + e.getMessage() + " not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<Error> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        var error = new Error("USER_ALREADY_EXISTS", "User with email " + e.getMessage() + " already exists");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
