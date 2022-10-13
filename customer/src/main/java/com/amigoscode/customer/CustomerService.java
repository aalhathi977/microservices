package com.amigoscode.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public record CustomerService(CustomerRepository customerRepository, CustomerConfig restTemplate) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = restTemplate.restTemplate().getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        if (fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }
        log.info("customer request is processed");
    }
}
