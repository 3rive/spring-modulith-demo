package com.demo.payment.internal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
class PaymentStore {

	private final Map<UUID, Payment> payments = new ConcurrentHashMap<>();

	Payment save(Payment payment) {
		payments.put(payment.id(), payment);
		return payment;
	}

	Optional<Payment> findById(UUID id) {
		return Optional.ofNullable(payments.get(id));
	}

	List<Payment> findAll() {
		return List.copyOf(payments.values());
	}

}
