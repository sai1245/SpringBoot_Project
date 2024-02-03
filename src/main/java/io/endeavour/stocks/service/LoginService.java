package io.endeavour.stocks.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {

    private String loginURL;
    /**
     * RestTemplate supports URI templates, allowing dynamic construction of URLs by replacing placeholders with actual values.
     * This is useful when dealing with RESTful APIs that use dynamic paths.
     */
    private RestTemplate restTemplate;

    /**
     * @Value annotation reads values from application.properties and injects it to the java variables
     * @param baseURL
     * @param userName
     * @param password
     */
    public LoginService(@Value("${client.stock-calculations.url}/login") String baseURL,
                        @Value("${client.login.username}") String userName,
                        @Value("${client.login.password}") String password){

        loginURL=baseURL;
        /**
         * RestTemplate is a client that can generate a HTTP request to the another webservices and
         * read the response that was sent
         */
        restTemplate = new RestTemplate();
        /**
         * Add an Interceptor to the RestTemplate to ensure that any call going through this restTemplate will have
         * Basic Authentication header added
         */
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName,password));
    }


    /**
     * Generates the bearer token to be used by the cummulative caluculation API
     * @return
     */
    public String getBearerToken(){

        /**
         * RestTemplate exchange method will fire the actual webservice call to the remote API, Its input parameters include:
         * 1)   The URL of the webservice call to hit
         * 2)   The Http method used to hit the remote API
         * 3)   The request object to be sent to the remote API(null in our case)
         * 4)   How should the responce received back be converted to (String in our case)
         */
        ResponseEntity<String> responce = restTemplate.exchange(loginURL,
                                                            HttpMethod.POST,
                                                            null,
                                                                 String.class);

        String token=responce.getBody();
        return token;
    }
}
