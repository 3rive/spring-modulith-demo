package com.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTest {

	private static final ApplicationModules MODULES = ApplicationModules.of(SpringModulithDemoApplication.class);

	@Test
	void verifiesModularStructure() {
		MODULES.verify();
	}

	@Test
	void exposesGreetingAndPaymentModules() {
		assertThat(MODULES.stream().map(module -> module.getIdentifier().toString())).containsExactlyInAnyOrder(
				"greeting", "payment");
	}

}
