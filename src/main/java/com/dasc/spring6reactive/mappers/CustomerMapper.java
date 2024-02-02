package com.dasc.spring6reactive.mappers;

import com.dasc.spring6reactive.domain.Customer;
import com.dasc.spring6reactive.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

  Customer customerDtoToCustomer(CustomerDTO dto);

  CustomerDTO customerToCustomerDto(Customer customer);

}
