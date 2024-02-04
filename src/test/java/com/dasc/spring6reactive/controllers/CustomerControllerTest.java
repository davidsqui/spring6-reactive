package com.dasc.spring6reactive.controllers;

import com.dasc.spring6reactive.model.CustomerDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  @Order(1)
  void testListCustomers() {
    webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody().jsonPath("$.size()").isEqualTo(3);
  }

  @Test
  @Order(2)
  void testGetCustomerById() {
    webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody(CustomerDTO.class);
  }

  @Test
  void testGetCustomerByIdNotFound() {
    webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testSaveNewCustomer() {
    webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().location("http://localhost:8080/api/v2/customers/4");
  }

  @Test
  void testSaveNewCustomerBad() {
    var customerToSave = getTestCustomerDTO();
    customerToSave.setName("");
    webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
        .body(Mono.just(customerToSave), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testUpdateCustomer() {
    webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testUpdateCustomerBad() {
    var customerToSave = getTestCustomerDTO();
    customerToSave.setName("");
    webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(customerToSave), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testUpdateCustomerNotFound() {
    webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testPatchCustomer() {
    webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testPatchCustomerBad() {
    var customerToSave = getTestCustomerDTO();
    customerToSave.setName("");
    webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(customerToSave), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testPatchCustomerNotFound() {
    webTestClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(999)
  void testDeleteCustomer() {
    webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testDeleteCustomerNotFound() {
    webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .exchange()
        .expectStatus().isNotFound();
  }

  public static CustomerDTO getTestCustomerDTO() {
    return CustomerDTO.builder()
        .name("Lois")
        .build();
  }

}