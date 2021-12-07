package com.foodapp.backend;

import com.foodapp.backend.security.interceptor.GatewayInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		System.out.println("Application Started");
	}
	@Autowired
	private GatewayInterceptor gatewayInterceptor;

	@PostConstruct
	public void initDataGateWay() {
		gatewayInterceptor.initData();
	}

}
