package com.dasc.spring6reactive.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
  @NotBlank
  @Size(min = 3, max = 255)
  private String name;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

}
