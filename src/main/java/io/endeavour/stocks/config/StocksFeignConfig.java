package io.endeavour.stocks.config;

import feign.RequestInterceptor;
import io.endeavour.stocks.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * This Config class is used to hit the actual cumulative return calculating webservice API
 * Hence it needs the login service to generate the Bearer token
 */
@Configuration
public class StocksFeignConfig {
    LoginService loginService;

    public StocksFeignConfig(LoginService loginService) {
        this.loginService = loginService;
    }


    /**
     * This generated RequestInterceptor adds an Authorization Header to any of the requests sent by using this config class
     * @return returns the token
     */

    @Bean
    public RequestInterceptor getRequestInterceptor(){
        return requestTemplate -> requestTemplate.header("Authorization","Bearer "+loginService.getBearerToken());
    }
}
