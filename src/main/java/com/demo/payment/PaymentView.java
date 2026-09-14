package com.demo.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentView(UUID id, String orderId, long amountMinor, String currency, PaymentStatus status,
		String description, Instant createdAt, Instant updatedAt) {
}
