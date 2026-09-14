package com.demo.payment.internal;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.payment.InitiatePaymentCommand;
import com.demo.payment.PaymentApi;
import com.demo.payment.PaymentView;

@RestController
@RequestMapping("/api/payments")
class PaymentController {

	private final PaymentApi payments;

	PaymentController(PaymentApi payments) {
		this.payments = payments;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	PaymentView initiate(@RequestBody InitiatePaymentCommand command) {
		return payments.initiate(command);
	}

	@PostMapping("/{id}/complete")
	PaymentView complete(@PathVariable UUID id) {
		return payments.complete(id);
	}

	@PostMapping("/{id}/fail")
	PaymentView fail(@PathVariable UUID id, @RequestBody(required = false) FailPaymentRequest request) {
		String reason = request == null ? null : request.reason();
		return payments.fail(id, reason);
	}

	@GetMapping("/{id}")
	ResponseEntity<PaymentView> findById(@PathVariable UUID id) {
		return payments.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	List<PaymentView> findAll() {
		return payments.findAll();
	}

	record FailPaymentRequest(String reason) {
	}

}
