package com.demo.payment.internal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = PaymentController.class)
class PaymentExceptionHandler {

	@ExceptionHandler(PaymentNotFoundException.class)
	ResponseEntity<ErrorResponse> notFound(PaymentNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(InvalidPaymentStateException.class)
	ResponseEntity<ErrorResponse> conflict(InvalidPaymentStateException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(InvalidPaymentRequestException.class)
	ResponseEntity<ErrorResponse> badRequest(InvalidPaymentRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
	}

	record ErrorResponse(String message) {
	}

}
