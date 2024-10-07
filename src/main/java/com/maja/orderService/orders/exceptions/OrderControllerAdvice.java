package com.maja.orderService.orders.exceptions;

import com.maja.orderService.commons.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class OrderControllerAdvice {

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<Error> handleOrderNotFoundException(OrderNotFoundException e){
        var error = new Error("ORDER_NOT_FOUND", "Order with id " + e.getMessage() + " not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CancelCompletedOrderException.class)
    ResponseEntity<Error> handleCancelCompletedOrderException(){
        var error = new Error("CANCEL_COMPLETED_CONFLICT", "Completed order can not be cancelled");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CancelCancelledOrderException.class)
    ResponseEntity<Error> handleCancelCancelledOrderException(){
        var error = new Error("CANCEL_CANCELLED_CONFLICT", "Order has already been cancelled");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CompleteCompletedOrderException.class)
    ResponseEntity<Error> handleCompleteCompleteddOrderException(){
        var error = new Error("COMPLETE_COMPLETED_CONFLICT", "Order has already been completed");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CompleteNotConfirmedOrderException.class)
    ResponseEntity<Error> handleCompleteNotConfirmedOrderException(){
        var error = new Error("COMPLETE_NOT_CONFIRMED_CONFLICT", "Only confirmed order can be completed");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CompleteCancelledOrderException.class)
    ResponseEntity<Error> handleCompleteCancelledOrderException(){
        var error = new Error("COMPLETE_CANCELLED_CONFLICT", "Can not complete cancelled order");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConfirmNotNewOrderException.class)
    ResponseEntity<Error> handleConfirmNotNewOrderException(){
        var error = new Error("CONFIRM_NOT_NEW_CONFLICT", "Order only order in status NEW can be confirmed");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectConfirmationCodeException.class)
    ResponseEntity<Error> handleIncorrectConfirmationCodeException(){
        var error = new Error("INCORRECT_CONFIRMATION_CODE", "Confirmation code is incorrect");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
