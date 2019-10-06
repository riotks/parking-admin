package ee.telia.parking.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PricelistConstants {
  //TODO pricelist from Database, using CustomerType and PricingType to differentiate values in InvoiceFactory

  public static final BigDecimal REGULAR_MONTHLY_FEE = BigDecimal.valueOf(0);
  public static final BigDecimal REGULAR_DAYTIME_HALF_HOUR_FEE = BigDecimal.valueOf(1.50);
  public static final BigDecimal REGULAR_OVERNIGHT_HALF_HOUR_FEE = BigDecimal.valueOf(1.00);

  public static final BigDecimal PREMIUM_MONTHLY_FEE = BigDecimal.valueOf(20);
  public static final BigDecimal PREMIUM_DAYTIME_HALF_HOUR_FEE = BigDecimal.valueOf(1.00);
  public static final BigDecimal PREMIUM_OVERNIGHT_HALF_HOUR_FEE = BigDecimal.valueOf(0.75);
  public static final BigDecimal PREMIUM_MAXIMUM_INVOICE = BigDecimal.valueOf(300);
}
