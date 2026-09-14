package com.demo.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.AssertablePublishedEvents;

@ApplicationModuleTest
class PaymentModuleTests {

	@Autowired
	PaymentApi payments;

	@Test
	void initiateCreatesPendingPayment() {
		PaymentView view = payments.initiate(new InitiatePaymentCommand("order-1", 1999, "usd", "Widget"));

		assertThat(view.id()).isNotNull();
		assertThat(view.orderId()).isEqualTo("order-1");
		assertThat(view.amountMinor()).isEqualTo(1999);
		assertThat(view.currency()).isEqualTo("USD");
		assertThat(view.status()).isEqualTo(PaymentStatus.PENDING);
		assertThat(payments.findById(view.id())).contains(view);
	}

	@Test
	void completePublishesPaymentCompleted(AssertablePublishedEvents events) {
		PaymentView pending = payments.initiate(new InitiatePaymentCommand("order-2", 500, "EUR", "Book"));

		PaymentView completed = payments.complete(pending.id());

		assertThat(completed.status()).isEqualTo(PaymentStatus.COMPLETED);
		events.assertThat().contains(PaymentCompleted.class)
				.matching(PaymentCompleted::paymentId, pending.id())
				.matching(PaymentCompleted::orderId, "order-2")
				.matching(PaymentCompleted::amountMinor, 500L)
				.matching(PaymentCompleted::currency, "EUR");
	}

	@Test
	void failPublishesPaymentFailed(AssertablePublishedEvents events) {
		PaymentView pending = payments.initiate(new InitiatePaymentCommand("order-3", 100, "GBP", "Fee"));

		payments.fail(pending.id(), "card declined");

		events.assertThat().contains(PaymentFailed.class)
				.matching(PaymentFailed::paymentId, pending.id())
				.matching(PaymentFailed::reason, "card declined");
	}

	@Test
	void completeRejectsUnknownPayment() {
		assertThatThrownBy(() -> payments.complete(UUID.randomUUID()))
				.hasMessageContaining("was not found");
	}

	@Test
	void completeRejectsAlreadyCompletedPayment() {
		PaymentView pending = payments.initiate(new InitiatePaymentCommand("order-4", 250, "USD", "Addon"));
		payments.complete(pending.id());

		assertThatThrownBy(() -> payments.complete(pending.id()))
				.hasMessageContaining("cannot leave COMPLETED");
	}

	@Test
	void initiateRejectsInvalidAmount() {
		assertThatThrownBy(() -> payments.initiate(new InitiatePaymentCommand("order-5", 0, "USD", "Bad")))
				.hasMessageContaining("amountMinor");
	}

}
