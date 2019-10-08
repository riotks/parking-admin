package ee.telia.parking.service;

import ee.telia.parking.domain.entity.ParkingRecord;
import ee.telia.parking.domain.entity.ParkingRecords;
import ee.telia.parking.domain.type.CustomerType;
import ee.telia.parking.repository.CustomerRepository;
import ee.telia.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static ee.telia.parking.domain.type.ParkingStatus.NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingService {

  private final ParkingRepository parkingRepository;
  private final CustomerRepository customerRepository;

  public int insertParkingRecord(Long customerId) {
    CustomerType customerType = getCustomerType(customerId);
    ParkingRecord parkingRecord = buildNewParkingRecord(customerId, customerType);
    return parkingRepository.insertParkingRecord(parkingRecord);
  }

  public int completeParkingRecord(Long parkingRecordId) {
    return parkingRepository.completeParkingRecord(parkingRecordId);
  }

  public int completeCustomerParkingRecord(Long customerId) {
    return parkingRepository.completeCustomerParkingRecord(customerId);
  }

  public ParkingRecord findParkingRecord(Long parkingRecordId) {
    return parkingRepository.findParkingRecord(parkingRecordId);
  }

  public ParkingRecords findCustomerParkingRecords(Long customerId, LocalDate fromDate,
      LocalDate toDate) {
    return ParkingRecords.builder()
        .records(parkingRepository
            .findCustomerParkingRecords(customerId, fromDate, toDate))
        .build();
  }

  CustomerType getCustomerType(Long customerId) {
    return customerRepository.getCustomerType(customerId);
  }

  ParkingRecord buildNewParkingRecord(Long customerId, CustomerType customerType) {
    return ParkingRecord.builder()
        .customerId(customerId)
        .customerType(customerType)
        .status(NEW)
        .beginAt(OffsetDateTime.now())
        .build();
  }

}
