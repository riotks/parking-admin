package ee.telia.parking.domain.entity;

import java.time.OffsetDateTime;
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
  private Long customerId;
  private BigDecimal amount;
  private String currency;
  private OffsetDateTime created_at;
}
