package com.ly.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringCloudApplication
@EnableZuulProxy
public class LyGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(LyGatewayApplication.class, args);
	}

}
