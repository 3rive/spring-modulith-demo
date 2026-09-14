package com.demo.payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentApi {

	PaymentView initiate(InitiatePaymentCommand command);

	PaymentView complete(UUID paymentId);

	PaymentView fail(UUID paymentId, String reason);

	Optional<PaymentView> findById(UUID paymentId);

	List<PaymentView> findAll();

}
