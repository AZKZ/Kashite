package com.azkz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.google.zxing.oned.EAN13Reader;

@Configuration
public class AppConfig {

	@Bean
	EAN13Reader eAN13Reader() {
		return new EAN13Reader();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
