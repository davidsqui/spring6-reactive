package com.dasc.spring6reactive.controllers;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

import com.dasc.spring6reactive.model.CustomerDTO;
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
    webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerController.CUSTOMER_PATH)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody().jsonPath("$.size()").isEqualTo(3);
  }

  @Test
  @Order(2)
  void testGetCustomerById() {
    webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody(CustomerDTO.class);
  }

  @Test
  void testGetCustomerByIdNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testSaveNewCustomer() {
    webTestClient.mutateWith(mockOAuth2Login()).post().uri(CustomerController.CUSTOMER_PATH)
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
    webTestClient.mutateWith(mockOAuth2Login()).post().uri(CustomerController.CUSTOMER_PATH)
        .body(Mono.just(customerToSave), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testUpdateCustomer() {
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testUpdateCustomerBad() {
    var customerToSave = getTestCustomerDTO();
    customerToSave.setName("");
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(customerToSave), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testUpdateCustomerNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testPatchCustomer() {
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testPatchCustomerBad() {
    var customerToSave = getTestCustomerDTO();
    customerToSave.setName("");
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .body(Mono.just(customerToSave), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testPatchCustomerNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).patch()
        .uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .body(Mono.just(getTestCustomerDTO()), CustomerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(999)
  void testDeleteCustomer() {
    webTestClient.mutateWith(mockOAuth2Login()).delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testDeleteCustomerNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).delete()
        .uri(CustomerController.CUSTOMER_PATH_ID, 100)
        .exchange()
        .expectStatus().isNotFound();
  }

  public static CustomerDTO getTestCustomerDTO() {
    return CustomerDTO.builder()
        .name("Lois")
        .build();
  }

}