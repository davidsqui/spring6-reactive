package com.dasc.spring6reactive.controllers;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

import com.dasc.spring6reactive.model.BeerDTO;
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
class BeerControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  @Order(1)
  void testListBeers() {
    webTestClient
        .mutateWith(mockOAuth2Login())
        .get().uri(BeerController.BEER_PATH)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody().jsonPath("$.size()").isEqualTo(3);
  }

  @Test
  @Order(2)
  void testGetBeerById() {
    webTestClient
        .mutateWith(mockOAuth2Login())
        .get().uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody(BeerDTO.class);
  }

  @Test
  void testGetBeerByIdNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerController.BEER_PATH_ID, 100)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testSaveNewBeer() {
    webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerController.BEER_PATH)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().location("http://localhost:8080/api/v2/beers/4");
  }

  @Test
  void testSaveNewBeerBad() {
    var beerToSave = getTestBeerDTO();
    beerToSave.setBeerName("");
    webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerController.BEER_PATH)
        .body(Mono.just(beerToSave), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @Order(3)
  void testUpdateBeer() {
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @Order(4)
  void testUpdateBeerBad() {
    var beerToSave = getTestBeerDTO();
    beerToSave.setBeerName("");
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(beerToSave), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testUpdateBeerNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerController.BEER_PATH_ID, 100)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(5)
  void testPatchBeer() {
    webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @Order(6)
  void testPatchBeerBad() {
    var beerToSave = getTestBeerDTO();
    beerToSave.setBeerName("");
    webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(beerToSave), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testPatchBeerNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerController.BEER_PATH_ID, 100)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(999)
  void testDeleteBeer() {
    webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testDeleteBeerNotFound() {
    webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerController.BEER_PATH_ID, 100)
        .exchange()
        .expectStatus().isNotFound();
  }

  public static BeerDTO getTestBeerDTO() {
    return BeerDTO.builder()
        .beerName("Space Dust")
        .beerStyle("IPA")
        .price(BigDecimal.TEN)
        .quantityOnHand(12)
        .upc("12345")
        .build();
  }

}