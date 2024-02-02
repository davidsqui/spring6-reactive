package com.dasc.spring6reactive.mappers;

import com.dasc.spring6reactive.domain.Beer;
import com.dasc.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

  Beer beerDtoToBeer(BeerDTO beerDTO);

  BeerDTO beerToBeerDto(Beer beer);

}
