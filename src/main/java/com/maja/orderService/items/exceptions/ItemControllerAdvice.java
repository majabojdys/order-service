package com.maja.orderService.items.exceptions;

import com.maja.orderService.commons.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ItemControllerAdvice {

    @ExceptionHandler(ItemNotFoundException.class)
    ResponseEntity<Error> handleItemNotFoundException(ItemNotFoundException e){
        var error = new Error("ITEM_NOT_FOUND", "Item with id " + e.getMessage() + " not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
