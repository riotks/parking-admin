package ee.telia.parking.helper;

import ee.telia.parking.domain.entity.Customer;
import ee.telia.parking.domain.entity.ParkingRecord;
import ee.telia.parking.domain.entity.ParkingRecords;
import ee.telia.parking.domain.type.CustomerType;

import java.time.OffsetDateTime;

import static ee.telia.parking.domain.type.CustomerType.PREMIUM;
import static ee.telia.parking.domain.type.CustomerType.REGULAR;
import static ee.telia.parking.domain.type.ParkingStatus.COMPLETED;
import static java.util.Arrays.asList;

public class TestDataBuilder {

  public static ParkingRecords buildRegularCustomerParkingRecords() {
    Customer customer = getRegularCustomer();
    Long customerId = customer.getId();
    CustomerType customerType = customer.getCustomerType();

    return ParkingRecords.builder()
        .customer(customer)
        .records(asList(
            ParkingRecord.builder()
                .id(11L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-08T08:12Z"))
                .endBy(OffsetDateTime.parse("2019-08-08T10:45Z"))
                .status(COMPLETED)
                .build()
            ,
            ParkingRecord.builder()
                .id(12L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-09T19:40Z"))
                .endBy(OffsetDateTime.parse("2019-08-09T20:35Z"))
                .status(COMPLETED)
                .build()
        ))
        .build();
  }

  public static ParkingRecords buildRegularCustomerParkingRecord_withDailyTariffThresholdCrossed() {
    Customer customer = getRegularCustomer();
    Long customerId = customer.getId();
    CustomerType customerType = customer.getCustomerType();

    return ParkingRecords.builder()
        .customer(customer)
        .records(asList(
            ParkingRecord.builder()
                .id(13L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-08T08:00Z"))
                .endBy(OffsetDateTime.parse("2019-08-08T20:00Z"))
                .status(COMPLETED)
                .build()
        ))
        .build();
  }

  public static ParkingRecords buildRegularCustomerParkingRecord_withNightlyTariffThresholdCrossed() {
    Customer customer = getRegularCustomer();
    Long customerId = customer.getId();
    CustomerType customerType = customer.getCustomerType();

    return ParkingRecords.builder()
        .customer(customer)
        .records(asList(
            ParkingRecord.builder()
                .id(13L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-08T06:00Z"))
                .endBy(OffsetDateTime.parse("2019-08-08T09:00Z"))
                .status(COMPLETED)
                .build()
        ))
        .build();
  }

  public static ParkingRecords buildPremiumCustomerParkingRecords() {
    Customer customer = getPremiumCustomer();
    Long customerId = customer.getId();
    CustomerType customerType = customer.getCustomerType();

    return ParkingRecords.builder()
        .customer(customer)
        .records(asList(
            ParkingRecord.builder()
                .id(21L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-08T08:12Z"))
                .endBy(OffsetDateTime.parse("2019-08-08T10:45Z"))
                .status(COMPLETED)
                .build()
            ,
            ParkingRecord.builder()
                .id(22L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-09T07:02Z"))
                .endBy(OffsetDateTime.parse("2019-08-09T11:56Z"))
                .status(COMPLETED)
                .build()
            ,
            ParkingRecord.builder()
                .id(23L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-08T22:10Z"))
                .endBy(OffsetDateTime.parse("2019-08-08T22:35Z"))
                .status(COMPLETED)
                .build()
            ,
            ParkingRecord.builder()
                .id(24L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-08-09T19:40Z"))
                .endBy(OffsetDateTime.parse("2019-08-09T20:35Z"))
                .status(COMPLETED)
                .build()
        ))
        .build();
  }

  public static ParkingRecords buildPremiumCustomerParkingRecords_504h() {
    Customer customer = getPremiumCustomer();
    Long customerId = customer.getId();
    CustomerType customerType = customer.getCustomerType();

    return ParkingRecords.builder()
        .customer(customer)
        .records(asList(
            ParkingRecord.builder()
                .id(21L)
                .customerId(customerId)
                .customerType(customerType)
                .beginAt(OffsetDateTime.parse("2019-09-01T08:00Z"))
                .endBy(OffsetDateTime.parse("2019-09-22T08:00Z"))
                .status(COMPLETED)
                .build()
        ))
        .build();
  }

  public static Customer getRegularCustomer() {
    return Customer.builder()
        .id(123L)
        .customerType(REGULAR)
        .firstName("Regular")
        .lastName("Customer")
        .build();
  }

  public static Customer getPremiumCustomer() {
    return Customer.builder()
        .id(789L)
        .customerType(PREMIUM)
        .firstName("Premium")
        .lastName("Customer")
        .build();
  }
}