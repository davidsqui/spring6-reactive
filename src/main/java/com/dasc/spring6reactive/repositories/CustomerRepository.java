package com.dasc.spring6reactive.repositories;

import com.dasc.spring6reactive.domain.Beer;
import com.dasc.spring6reactive.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

}
