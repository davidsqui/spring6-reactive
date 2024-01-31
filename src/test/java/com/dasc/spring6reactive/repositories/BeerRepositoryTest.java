package com.dasc.spring6reactive.repositories;

import com.dasc.spring6reactive.config.DatabaseConfig;
import com.dasc.spring6reactive.domain.Beer;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void saveNewBeer() {
    var newBeer = Beer.builder()
        .beerName("Space Dust")
        .beerStyle("IPA")
        .price(BigDecimal.TEN)
        .quantityOnHand(12)
        .upc("12345")
        .build();
    beerRepository.save(newBeer).subscribe(System.out::println);
  }

}