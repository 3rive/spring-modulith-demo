package com.demo.payment.internal;

class PaymentNotFoundException extends RuntimeException {

	PaymentNotFoundException(String message) {
		super(message);
	}

}
