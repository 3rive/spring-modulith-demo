package com.demo.payment;

public record InitiatePaymentCommand(String orderId, long amountMinor, String currency, String description) {
}
