package com.demo;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTest {

	private static final ApplicationModules MODULES = ApplicationModules.of(SpringModulithDemoApplication.class);

	@Test
	void verifiesModularStructure() {
		MODULES.verify();
	}

}
