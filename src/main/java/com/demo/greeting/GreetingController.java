package com.demo.greeting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greetings")
class GreetingController {

	private final GreetingService greetingService;

	GreetingController(GreetingService greetingService) {
		this.greetingService = greetingService;
	}

	@GetMapping
	GreetingResponse greet() {
		return greetingService.createGreeting();
	}

	record GreetingResponse(String message) {
	}

}
