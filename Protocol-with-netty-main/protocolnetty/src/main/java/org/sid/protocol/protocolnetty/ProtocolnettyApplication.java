package org.sid.protocol.protocolnetty;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ProtocolnettyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtocolnettyApplication.class, args);
	}
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		System.out.println("Application is listening on port 1226");
	}

}
