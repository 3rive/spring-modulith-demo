package com.demo.payment.internal;

class InvalidPaymentRequestException extends RuntimeException {

	InvalidPaymentRequestException(String message) {
		super(message);
	}

}
