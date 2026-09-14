package com.demo.payment;

import java.util.UUID;

public record PaymentFailed(UUID paymentId, String orderId, String reason) {
}
