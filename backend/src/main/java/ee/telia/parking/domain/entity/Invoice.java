package ee.telia.parking.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

  private Long id;
  private Long referenceNumber;
  private BigDecimal amount;
  private String currency;
}
