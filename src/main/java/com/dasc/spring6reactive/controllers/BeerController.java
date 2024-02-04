package com.dasc.spring6reactive.controllers;

import com.dasc.spring6reactive.model.BeerDTO;
import com.dasc.spring6reactive.services.BeerService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
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
    return beerService.getBeerById(beerId)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
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
  public Mono<ResponseEntity<Void>> updateBeer(@PathVariable Integer beerId,
      @Validated @RequestBody BeerDTO beerDTO) {
    return beerService.updateBeer(beerId, beerDTO)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(savedBeer -> ResponseEntity.noContent().build());
  }

  @PatchMapping(BEER_PATH_ID)
  public Mono<ResponseEntity<Void>> updateBeerPatch(@PathVariable Integer beerId,
      @Validated @RequestBody BeerDTO beerDTO) {
    return beerService.updateBeerPatch(beerId, beerDTO)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .map(savedBeer -> ResponseEntity.noContent().build());

  }

  @DeleteMapping(BEER_PATH_ID)
  public Mono<ResponseEntity<Void>> deleteBeer(@PathVariable Integer beerId) {
    return getBeerById(beerId)
        .map(foundBeer -> beerService.deleteBeer(beerId))
        .thenReturn(ResponseEntity.noContent().build());
  }

}
