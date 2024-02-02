package com.dasc.spring6reactive.controllers;

import com.dasc.spring6reactive.model.CustomerDTO;
import com.dasc.spring6reactive.services.CustomerService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

  public static final String CUSTOMER_PATH = "/api/v2/customers";
  public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

  private final CustomerService customerService;

  @GetMapping(CUSTOMER_PATH)
  public Flux<CustomerDTO> listCustomers() {
    return customerService.listCustomers();
  }

  @GetMapping(CUSTOMER_PATH_ID)
  public Mono<CustomerDTO> getCustomerById(@PathVariable Integer customerId) {
    return customerService.getCustomerById(customerId);
  }

  @PostMapping(CUSTOMER_PATH)
  ResponseEntity<Void> createNewCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
    AtomicInteger atomicInteger = new AtomicInteger();
    customerService.saveNewCustomer(customerDTO)
        .subscribe(savedCustomer -> atomicInteger.set(savedCustomer.getId()));

    return ResponseEntity.created(UriComponentsBuilder
            .fromHttpUrl("http://localhost:8080/" + CUSTOMER_PATH + "/" + atomicInteger.get())
            .build().toUri())
        .build();
  }

  @PutMapping(CUSTOMER_PATH_ID)
  public ResponseEntity<Void> updateCustomer(@PathVariable Integer customerId,
      @Validated @RequestBody CustomerDTO customerDTO) {
    customerService.updateCustomer(customerId, customerDTO).subscribe();
    return ResponseEntity.ok().build();
  }

  @PatchMapping(CUSTOMER_PATH_ID)
  public ResponseEntity<Void> updateCustomerPatch(@PathVariable Integer customerId,
      @Validated @RequestBody CustomerDTO customerDTO) {
    customerService.updateCustomerPatch(customerId, customerDTO).subscribe();
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(CUSTOMER_PATH_ID)
  public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
    customerService.deleteCustomer(customerId).subscribe();
    return ResponseEntity.noContent().build();
  }
}
