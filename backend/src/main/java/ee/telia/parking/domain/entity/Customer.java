package ee.telia.parking.domain.entity;

import ee.telia.parking.domain.type.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  private Long id;
  private CustomerType customerType;
  private String firstName;
  private String lastName;
}
