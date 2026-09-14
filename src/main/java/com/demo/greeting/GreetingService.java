package com.demo.greeting;

import org.springframework.stereotype.Service;

@Service
class GreetingService {

	GreetingController.GreetingResponse createGreeting() {
		return new GreetingController.GreetingResponse("Hello from the greeting module");
	}

}
