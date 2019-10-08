package ee.telia.parking.service;

import ee.telia.parking.domain.entity.Customer;
import ee.telia.parking.domain.entity.Invoice;
import ee.telia.parking.domain.entity.ParkingRecord;
import ee.telia.parking.domain.entity.ParkingRecords;
import ee.telia.parking.domain.type.CustomerType;
import ee.telia.parking.helper.exception.CustomerParseException;
import ee.telia.parking.helper.exception.ParkingTimeThresholdCrossedException;
import ee.telia.parking.repository.InvoiceRepository;
import ee.telia.parking.repository.ParkingRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceService {

  private final InvoiceRepository invoiceRepository;

  @Value("${pricing.timing.daily-tariff}")
  @DateTimeFormat(iso = ISO.TIME)
  protected LocalTime dailyTariffTime;
  @Value("${pricing.timing.nightly-tariff}")
  @DateTimeFormat(iso = ISO.TIME)
  protected LocalTime nightlyTariffTime;

  @Value("${pricing.regular.day-half-hour}")
  protected BigDecimal priceRegularDayHalfHour;
  @Value("${pricing.regular.night-half-hour}")
  protected BigDecimal priceRegularNightHalfHour;

  @Value("${pricing.premium.day-half-hour}")
  protected BigDecimal pricePremiumDayHalfHour;
  @Value("${pricing.premium.night-half-hour}")
  protected BigDecimal pricePremiumNightHalfHour;
  @Value("${pricing.premium.monthly-fee}")
  protected BigDecimal pricePremiumMonthlyFee;
  @Value("${pricing.premium.max-amount}")
  protected BigDecimal pricePremiumMaxAmount;

  @Value("${pricing.currency.default}")
  private String currency;

  public Invoice findInvoice(Long invoiceId) {
    return invoiceRepository.findInvoice(invoiceId);
  }

  public List<Invoice> findCustomerInvoices(Long customerId, LocalDate fromDate,
      LocalDate toDate) {
    return invoiceRepository.findCustomerInvoices(customerId, fromDate, toDate);
  }

  public Invoice insertInvoice(ParkingRecords parkingRecords) {
    return generateInvoice(parkingRecords);
  }

  public Invoice generateInvoice(ParkingRecords parkingRecords) throws CustomerParseException {

    BigDecimal invoiceSum = BigDecimal.ZERO;
    for (ParkingRecord record : parkingRecords.getRecords()) {
      CustomerType customerType = record.getCustomerType();
      int halfHoursParked = getDurationHalfHoursBetweenDates(record.getBeginAt(),
          record.getEndBy());

      invoiceSum = invoiceSum
          .add(getInvoiceSumForCustomer(customerType, record, halfHoursParked));
    }

    Customer customer = parkingRecords.getCustomer();
    CustomerType currentCustomerStatus = customer.getCustomerType();
    if (CustomerType.PREMIUM.equals(currentCustomerStatus)) {
      invoiceSum = invoiceSum.add(pricePremiumMonthlyFee);
      if (invoiceSum.compareTo(pricePremiumMaxAmount) > 1) {
        invoiceSum = pricePremiumMaxAmount;
      }
    }

    return Invoice.builder()
        .customerId(customer.getId())
        .amount(invoiceSum)
        .currency(currency)
        .created_at(OffsetDateTime.now())
        .build();
  }

  private BigDecimal getInvoiceSumForCustomer(CustomerType customerType, ParkingRecord record,
      int halfHoursParked) {
    BigDecimal calculatedPrice = BigDecimal.ZERO;
    switch (customerType) {
      case REGULAR:
        calculatedPrice = getPriceAccordingToTimeOfDay(record, halfHoursParked,
            priceRegularDayHalfHour,
            priceRegularNightHalfHour);
        break;
      case PREMIUM:
        calculatedPrice = getPriceAccordingToTimeOfDay(record, halfHoursParked,
            pricePremiumDayHalfHour,
            pricePremiumNightHalfHour);
        break;
      default:
        throw new CustomerParseException("Incorrect Customer type: " + customerType.toString());
    }
    return calculatedPrice;
  }

  public BigDecimal getPriceAccordingToTimeOfDay(ParkingRecord record, int halfHoursParked,
      BigDecimal dayPrice, BigDecimal nightPrice) {
    if (isDailyTariffConditionsSatisfied(record)) {
      return dayPrice.multiply(BigDecimal.valueOf(halfHoursParked));
    } else if (isNightlyTariffConditionsSatisfied(record)) {
      return nightPrice.multiply(BigDecimal.valueOf(halfHoursParked));
    } else {
      throw new ParkingTimeThresholdCrossedException(
          "Parking overflowed into another tariff. beginAt= " + record.getBeginAt() + " endBy= "
              + record.getEndBy());
    }
  }

  public boolean isDailyTariffConditionsSatisfied(ParkingRecord record) {
    return dailyTariffTime.isBefore(record.getBeginAt().toLocalTime())
        && nightlyTariffTime.isAfter(record.getBeginAt().toLocalTime());
  }

  public boolean isNightlyTariffConditionsSatisfied(ParkingRecord record) {
    return nightlyTariffTime.isBefore(record.getBeginAt().toLocalTime())
        && (dailyTariffTime.isBefore(record.getBeginAt().toLocalTime())
        || !dailyTariffTime.isAfter(record.getBeginAt().toLocalTime()));
  }

  public int getDurationHalfHoursBetweenDates(OffsetDateTime beginAt, OffsetDateTime endBy) {
    long minutesBetween = ChronoUnit.MINUTES.between(beginAt, endBy);
    return (int) Math.ceil((double) minutesBetween / 30);
  }

}
