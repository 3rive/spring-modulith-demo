package com.demo.payment.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.demo.payment.PaymentCompleted;
import com.demo.payment.PaymentFailed;

@Component
class PaymentAuditListener {

	private static final Logger log = LoggerFactory.getLogger(PaymentAuditListener.class);

	@EventListener
	void onCompleted(PaymentCompleted event) {
		log.info("Payment completed: paymentId={}, orderId={}, amount={} {}", event.paymentId(), event.orderId(),
				event.amountMinor(), event.currency());
	}

	@EventListener
	void onFailed(PaymentFailed event) {
		log.info("Payment failed: paymentId={}, orderId={}, reason={}", event.paymentId(), event.orderId(),
				event.reason());
	}

}
