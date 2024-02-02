package com.dasc.spring6reactive.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

  private Integer id;
  private String name;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

}
