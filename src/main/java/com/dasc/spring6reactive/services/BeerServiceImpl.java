package com.dasc.spring6reactive.services;

import com.dasc.spring6reactive.mappers.BeerMapper;
import com.dasc.spring6reactive.model.BeerDTO;
import com.dasc.spring6reactive.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public Flux<BeerDTO> listBeers() {
    return beerRepository.findAll()
        .map(beerMapper::beerToBeerDto);
  }

  @Override
  public Mono<BeerDTO> getBeerById(Integer beerId) {
    return beerRepository.findById(beerId)
        .map(beerMapper::beerToBeerDto);
  }

  @Override
  public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
    return beerRepository.save(beerMapper.beerDtoToBeer(beerDTO))
        .map(beerMapper::beerToBeerDto);
  }

  @Override
  public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {
    return beerRepository.findById(beerId)
        .map(foundBeer -> {
          foundBeer.setBeerName(beerDTO.getBeerName());
          foundBeer.setBeerStyle(beerDTO.getBeerStyle());
          foundBeer.setPrice(beerDTO.getPrice());
          foundBeer.setUpc(beerDTO.getUpc());
          foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
          return foundBeer;
        })
        .flatMap(beerRepository::save)
        .map(beerMapper::beerToBeerDto);
  }

  @Override
  public Mono<BeerDTO> updateBeerPatch(Integer beerId, BeerDTO beerDTO) {
    return beerRepository.findById(beerId)
        .map(foundBeer -> {
          if (StringUtils.hasText(beerDTO.getBeerName())) {
            foundBeer.setBeerName(beerDTO.getBeerName());
          }
          if (StringUtils.hasText(beerDTO.getBeerStyle())) {
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
          }
          if (StringUtils.hasText(beerDTO.getPrice().toString())) {
            foundBeer.setPrice(beerDTO.getPrice());
          }
          if (StringUtils.hasText(beerDTO.getUpc())) {
            foundBeer.setUpc(beerDTO.getUpc());
          }
          if (StringUtils.hasText(beerDTO.getQuantityOnHand().toString())) {
            foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
          }

          return foundBeer;
        })
        .flatMap(beerRepository::save)
        .map(beerMapper::beerToBeerDto);
  }

  @Override
  public Mono<Void> deleteBeer(Integer beerId) {
    return beerRepository.deleteById(beerId);
  }
}
