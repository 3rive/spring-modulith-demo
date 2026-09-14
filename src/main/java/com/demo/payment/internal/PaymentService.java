package com.demo.payment.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.demo.payment.InitiatePaymentCommand;
import com.demo.payment.PaymentApi;
import com.demo.payment.PaymentCompleted;
import com.demo.payment.PaymentFailed;
import com.demo.payment.PaymentView;

@Service
class PaymentService implements PaymentApi {

	private final PaymentStore store;
	private final ApplicationEventPublisher events;

	PaymentService(PaymentStore store, ApplicationEventPublisher events) {
		this.store = store;
		this.events = events;
	}

	@Override
	public PaymentView initiate(InitiatePaymentCommand command) {
		validate(command);
		Payment payment = Payment.pending(command.orderId().trim(), command.amountMinor(),
				command.currency().trim().toUpperCase(),
				command.description() == null ? "" : command.description().trim());
		return store.save(payment).toView();
	}

	@Override
	public PaymentView complete(UUID paymentId) {
		Payment payment = requirePayment(paymentId);
		payment.markCompleted();
		store.save(payment);
		events.publishEvent(new PaymentCompleted(payment.id(), payment.orderId(), payment.amountMinor(),
				payment.currency()));
		return payment.toView();
	}

	@Override
	public PaymentView fail(UUID paymentId, String reason) {
		Payment payment = requirePayment(paymentId);
		payment.markFailed();
		store.save(payment);
		String failureReason = (reason == null || reason.isBlank()) ? "Payment failed" : reason.trim();
		events.publishEvent(new PaymentFailed(payment.id(), payment.orderId(), failureReason));
		return payment.toView();
	}

	@Override
	public Optional<PaymentView> findById(UUID paymentId) {
		return store.findById(paymentId).map(Payment::toView);
	}

	@Override
	public List<PaymentView> findAll() {
		return store.findAll().stream().map(Payment::toView).toList();
	}

	private Payment requirePayment(UUID paymentId) {
		return store.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment %s was not found".formatted(paymentId)));
	}

	private static void validate(InitiatePaymentCommand command) {
		if (command == null) {
			throw new InvalidPaymentRequestException("Payment request is required");
		}
		if (command.orderId() == null || command.orderId().isBlank()) {
			throw new InvalidPaymentRequestException("orderId is required");
		}
		if (command.amountMinor() <= 0) {
			throw new InvalidPaymentRequestException("amountMinor must be greater than zero");
		}
		if (command.currency() == null || !command.currency().trim().matches("[A-Za-z]{3}")) {
			throw new InvalidPaymentRequestException("currency must be a 3-letter ISO code");
		}
	}

}
