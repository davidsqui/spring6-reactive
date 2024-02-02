package com.dasc.spring6reactive.controllers;

import com.dasc.spring6reactive.model.BeerDTO;
import com.dasc.spring6reactive.services.BeerService;
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
public class BeerController {

  public static final String BEER_PATH = "/api/v2/beers";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

  private final BeerService beerService;

  @GetMapping(BEER_PATH)
  public Flux<BeerDTO> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(BEER_PATH_ID)
  public Mono<BeerDTO> getBeerById(@PathVariable Integer beerId) {
    return beerService.getBeerById(beerId);
  }

  @PostMapping(BEER_PATH)
  ResponseEntity<Void> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
    AtomicInteger atomicInteger = new AtomicInteger();
    beerService.saveNewBeer(beerDTO).subscribe(savedBeer -> atomicInteger.set(savedBeer.getId()));

    return ResponseEntity.created(UriComponentsBuilder
            .fromHttpUrl("http://localhost:8080/" + BEER_PATH + "/" + atomicInteger.get())
            .build().toUri())
        .build();
  }

  @PutMapping(BEER_PATH_ID)
  public ResponseEntity<Void> updateBeer(@PathVariable Integer beerId,
      @Validated @RequestBody BeerDTO beerDTO) {
    beerService.updateBeer(beerId, beerDTO).subscribe();
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(BEER_PATH_ID)
  public ResponseEntity<Void> updateBeerPatch(@PathVariable Integer beerId,
      @Validated @RequestBody BeerDTO beerDTO) {
    beerService.updateBeerPatch(beerId, beerDTO).subscribe();
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(BEER_PATH_ID)
  public ResponseEntity<Void> deleteBeer(@PathVariable Integer beerId) {
    beerService.deleteBeer(beerId).subscribe();
    return ResponseEntity.noContent().build();
  }
}
