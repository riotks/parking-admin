package ee.telia.parking.controller;

import ee.telia.parking.domain.entity.ParkingRecord;
import ee.telia.parking.service.ParkingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParkingUsageController {

  private final ParkingService parkingService;

  @GetMapping(path = "/records/{parkingId}")
  @ApiOperation("Finds parking records")
  public ParkingRecord findParkingRecord(
      @ApiParam(required = true, value = "parkingId")
      @PathVariable Long parkingId) {
    return parkingService.findParkingRecord(parkingId);
  }

  @GetMapping(path = "/records/customer/{customerId}")
  @ApiOperation("Finds parking records by customerId and/or dates")
  public List<ParkingRecord> findCustomerParkingRecords(
      @ApiParam(value = "customerId")
      @PathVariable Long customerId,
      @RequestParam(name = "fromDate", required = false)
      @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
      @RequestParam(name = "toDate", required = false)
      @DateTimeFormat(iso = ISO.DATE) LocalDate toDate) {

    return parkingService.findCustomerParkingRecords(customerId, fromDate, toDate).getRecords();
  }

  @GetMapping(path = "/records")
  @ApiOperation("Finds all parking records available")
  public List<ParkingRecord> findAllRecords() {
    return parkingService.findCustomerParkingRecords(null, null, null).getRecords();
  }

  @PostMapping(path = "/start/customer/{customerId}")
  @ApiOperation("Starts parking record for customer")
  public int insertRecord(
      @ApiParam(required = true, value = "CustomerId") @PathVariable Long customerId) {

    return parkingService.insertParkingRecord(customerId);
  }

  @PostMapping(path = "/finish/{parkingId}")
  @ApiOperation("Finishes parking by id")
  public int completeParking(
      @ApiParam(required = true, value = "Parking Id") @PathVariable Long parkingId) {

    return parkingService.completeParkingRecord(parkingId);
  }

  @PostMapping(path = "/finish/customer/{customerId}")
  @ApiOperation("Finishes customer parking record")
  public int completeCustomerParking(
      @ApiParam(required = true, value = "Customer Id") @PathVariable Long customerId) {

    return parkingService.completeCustomerParkingRecord(customerId);
  }

}
