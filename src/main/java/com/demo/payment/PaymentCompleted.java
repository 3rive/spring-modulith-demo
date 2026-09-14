package com.demo.payment;

import java.util.UUID;

public record PaymentCompleted(UUID paymentId, String orderId, long amountMinor, String currency) {
}
