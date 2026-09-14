package com.demo.payment.internal;

import java.time.Instant;
import java.util.UUID;

import com.demo.payment.PaymentStatus;
import com.demo.payment.PaymentView;

class Payment {

	private final UUID id;
	private final String orderId;
	private final long amountMinor;
	private final String currency;
	private final String description;
	private final Instant createdAt;
	private PaymentStatus status;
	private Instant updatedAt;

	Payment(UUID id, String orderId, long amountMinor, String currency, String description, PaymentStatus status,
			Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.orderId = orderId;
		this.amountMinor = amountMinor;
		this.currency = currency;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	static Payment pending(String orderId, long amountMinor, String currency, String description) {
		Instant now = Instant.now();
		return new Payment(UUID.randomUUID(), orderId, amountMinor, currency, description, PaymentStatus.PENDING, now,
				now);
	}

	UUID id() {
		return id;
	}

	String orderId() {
		return orderId;
	}

	long amountMinor() {
		return amountMinor;
	}

	String currency() {
		return currency;
	}

	PaymentStatus status() {
		return status;
	}

	void markCompleted() {
		requirePending();
		this.status = PaymentStatus.COMPLETED;
		this.updatedAt = Instant.now();
	}

	void markFailed() {
		requirePending();
		this.status = PaymentStatus.FAILED;
		this.updatedAt = Instant.now();
	}

	PaymentView toView() {
		return new PaymentView(id, orderId, amountMinor, currency, status, description, createdAt, updatedAt);
	}

	private void requirePending() {
		if (status != PaymentStatus.PENDING) {
			throw new InvalidPaymentStateException(
					"Payment %s cannot leave %s".formatted(id, status));
		}
	}

}
