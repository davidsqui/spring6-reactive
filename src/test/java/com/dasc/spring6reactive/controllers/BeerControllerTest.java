package com.dasc.spring6reactive.controllers;

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
    webTestClient.get().uri(BeerController.BEER_PATH)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody().jsonPath("$.size()").isEqualTo(3);
  }

  @Test
  @Order(2)
  void testGetBeerById() {
    webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody(BeerDTO.class);
  }

  @Test
  void testSaveNewBeer() {
    webTestClient.post().uri(BeerController.BEER_PATH)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().location("http://localhost:8080/api/v2/beers/4");
  }

  @Test
  void testUpdateBeer() {
    webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testPatchBeer() {
    webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(getTestBeerDTO()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @Order(999)
  void testDeleteBeer() {
    webTestClient.delete().uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isNoContent();
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