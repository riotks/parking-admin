package ee.telia.parking.service;

import ee.telia.parking.domain.entity.Invoice;
import ee.telia.parking.domain.entity.ParkingRecord;
import ee.telia.parking.domain.entity.ParkingRecords;
import ee.telia.parking.helper.TestDataBuilder;
import ee.telia.parking.helper.exception.ParkingTimeThresholdCrossedException;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {

  //values from application.yml
  private static final LocalTime DAILY_TARIFF_TIME = LocalTime.parse("07:00:00.000");
  private static final LocalTime NIGHTLY_TARIFF_TIME = LocalTime.parse("19:00:00.000");

  private static final BigDecimal PRICE_REGULAR_DAY_HALF_HOUR = BigDecimal.valueOf(1.50);
  private static final BigDecimal PRICE_REGULAR_NIGHT_HALF_HOUR = BigDecimal.valueOf(1.00);

  private static final BigDecimal PRICE_PREMIUM_DAY_HALF_HOUR = BigDecimal.valueOf(1.00);
  private static final BigDecimal PRICE_PREMIUM_NIGHT_HALF_HOUR = BigDecimal.valueOf(0.75);
  private static final BigDecimal PRICE_PREMIUM_MONTHLY_FEE = BigDecimal.valueOf(20);
  private static final BigDecimal PRICE_PREMIUM_MAX_AMOUNT = BigDecimal.valueOf(300);


  @Spy
  @InjectMocks
  private InvoiceService invoiceService;

  @Before
  public void setUp() {
    invoiceService.dailyTariffTime = DAILY_TARIFF_TIME;
    invoiceService.nightlyTariffTime = NIGHTLY_TARIFF_TIME;

    invoiceService.priceRegularDayHalfHour = PRICE_REGULAR_DAY_HALF_HOUR;
    invoiceService.priceRegularNightHalfHour = PRICE_REGULAR_NIGHT_HALF_HOUR;

    invoiceService.pricePremiumDayHalfHour = PRICE_PREMIUM_DAY_HALF_HOUR;
    invoiceService.pricePremiumNightHalfHour = PRICE_PREMIUM_NIGHT_HALF_HOUR;
    invoiceService.pricePremiumMonthlyFee = PRICE_PREMIUM_MONTHLY_FEE;
    invoiceService.pricePremiumMaxAmount = PRICE_PREMIUM_MAX_AMOUNT;
  }

  ParkingRecords regularParkingRecords = TestDataBuilder.buildRegularCustomerParkingRecords();
  ParkingRecords premiumParkingRecords = TestDataBuilder.buildPremiumCustomerParkingRecords();

  @Test
  public void generateInvoice_withRegularCustomerData() {
    Invoice invoice = invoiceService.generateInvoice(regularParkingRecords);
    assertThat(invoice.getAmount(), is(BigDecimal.valueOf(11.00)));
  }

  @Test
  public void generateInvoice_withPremiumCustomerData() {
    Invoice invoice = invoiceService.generateInvoice(premiumParkingRecords);
    assertThat(invoice.getAmount(), is(BigDecimal.valueOf(38.25)));
  }
/*
  @Test
  public void generateInvoice_withRegCustomerData() {
    Invoice invoice = invoiceService.generateInvoice(premiumParkingRecords);
    assertThat(invoice.getAmount(), is(BigDecimal.valueOf(38.25)));
  }
*/
  @Test
  public void calculateHalfHoursParked() {
    ParkingRecord parkingRecord1 = regularParkingRecords.getRecords().get(0);
    ParkingRecord parkingRecord2 = regularParkingRecords.getRecords().get(1);

    ParkingRecord parkingRecord3 = premiumParkingRecords.getRecords().get(0);
    ParkingRecord parkingRecord4 = premiumParkingRecords.getRecords().get(1);
    ParkingRecord parkingRecord5 = premiumParkingRecords.getRecords().get(2);
    ParkingRecord parkingRecord6 = premiumParkingRecords.getRecords().get(3);

    int durationParked1 = invoiceService
        .getDurationHalfHoursBetweenDates(parkingRecord1.getBeginAt(), parkingRecord1.getEndBy());
    int durationParked2 = invoiceService
        .getDurationHalfHoursBetweenDates(parkingRecord2.getBeginAt(), parkingRecord2.getEndBy());

    int durationParked3 = invoiceService
        .getDurationHalfHoursBetweenDates(parkingRecord3.getBeginAt(), parkingRecord3.getEndBy());
    int durationParked4 = invoiceService
        .getDurationHalfHoursBetweenDates(parkingRecord4.getBeginAt(), parkingRecord4.getEndBy());
    int durationParked5 = invoiceService
        .getDurationHalfHoursBetweenDates(parkingRecord5.getBeginAt(), parkingRecord5.getEndBy());
    int durationParked6 = invoiceService
        .getDurationHalfHoursBetweenDates(parkingRecord6.getBeginAt(), parkingRecord6.getEndBy());

    assertThat(durationParked1, is(6));
    assertThat(durationParked2, is(2));

    assertThat(durationParked3, is(6));
    assertThat(durationParked4, is(10));
    assertThat(durationParked5, is(1));
    assertThat(durationParked6, is(2));
  }

  @Test
  public void isDailyTariffConditionsSatisfied_whenDaily() {
    ParkingRecord dailyParking = regularParkingRecords.getRecords().get(0);
    assertThat(invoiceService.isDailyTariffConditionsSatisfied(dailyParking), is(true));
  }

  @Test
  public void isDailyTariffConditionsSatisfied_whenNightly_shouldReturnFalse() {
    ParkingRecord nightParking = regularParkingRecords.getRecords().get(1);
    assertThat(invoiceService.isDailyTariffConditionsSatisfied(nightParking), is(false));
  }

  @Test(expected = ParkingTimeThresholdCrossedException.class)
  public void getPriceAccordingToTimeOfDay_whenNightlyTariffThresholdCrossed_shouldReturnFalse() {
    ParkingRecord record = TestDataBuilder.buildRegularCustomerParkingRecord_withNightlyTariffThresholdCrossed().getRecords().get(0);
    assertThat(invoiceService.isDailyTariffConditionsSatisfied(record), is(false));
    assertThat(invoiceService.isNightlyTariffConditionsSatisfied(record), is(false));
    //TODO handle when one kind of record overflows into another.
/*
    3 hours 6:00-09:00
     4 halfHours x 1.00
     +
     2 halfHours x 1.50
     =
     7.00 EUR
*/
    assertThat(invoiceService.getPriceAccordingToTimeOfDay(record, 6, PRICE_REGULAR_DAY_HALF_HOUR, PRICE_REGULAR_NIGHT_HALF_HOUR), is(BigDecimal.valueOf(7L)));

  }

}