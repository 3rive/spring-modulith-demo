package com.demo.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentApiWebTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void paymentLifecycleViaHttp() throws Exception {
		MvcResult created = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content("""
				{"orderId":"web-order-1","amountMinor":2599,"currency":"usd","description":"Headphones"}
				""")).andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("PENDING"))
				.andExpect(jsonPath("$.currency").value("USD")).andExpect(jsonPath("$.amountMinor").value(2599))
				.andReturn();

		JsonNode body = objectMapper.readTree(created.getResponse().getContentAsString());
		String id = body.get("id").asText();

		mockMvc.perform(get("/api/payments/{id}", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value("web-order-1"));

		mockMvc.perform(post("/api/payments/{id}/complete", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("COMPLETED"));

		mockMvc.perform(get("/api/payments")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
	}

	@Test
	void initiateRejectsMissingOrderId() throws Exception {
		mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content("""
				{"orderId":"","amountMinor":100,"currency":"USD"}
				""")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("orderId is required"));
	}

	@Test
	void completeUnknownPaymentReturnsNotFound() throws Exception {
		mockMvc.perform(post("/api/payments/{id}/complete", "11111111-1111-1111-1111-111111111111"))
				.andExpect(status().isNotFound());
	}

	@Test
	void greetingEndpointStillWorks() throws Exception {
		mockMvc.perform(get("/api/greetings")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hello from the greeting module"));
	}

}
