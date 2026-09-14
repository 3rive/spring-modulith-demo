package com.demo.payment.internal;

class InvalidPaymentStateException extends RuntimeException {

	InvalidPaymentStateException(String message) {
		super(message);
	}

}
