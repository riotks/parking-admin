package ee.telia.parking.controller;

import ee.telia.parking.domain.entity.Invoice;
import ee.telia.parking.domain.entity.ParkingRecords;
import ee.telia.parking.service.InvoiceService;
import ee.telia.parking.service.ParkingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InvoiceController {

  private final InvoiceService invoiceService;
  private ParkingService parkingService;

  @GetMapping(path = "/invoices/{invoiceId}")
  @ApiOperation("Finds invoices")
  public Invoice findInvoice(
      @ApiParam(required = true, value = "invoiceId")
      @PathVariable Long invoiceId) {
    return invoiceService.findInvoice(invoiceId);
  }

  @GetMapping(path = "/invoices/customer/{customerId}")
  @ApiOperation("Finds invoices by customerId and dates")
  public List<Invoice> findCustomerInvoices(
      @ApiParam(value = "customerId")
      @PathVariable Long customerId,
      @RequestParam(name = "fromDate", required = false)
      @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
      @RequestParam(name = "toDate", required = false)
      @DateTimeFormat(iso = ISO.DATE) LocalDate toDate) {

    return invoiceService.findCustomerInvoices(customerId, fromDate, toDate);
  }

  @GetMapping(path = "/invoices")
  @ApiOperation("Finds all invoices available")
  public List<Invoice> findAllInvoices() {
    return invoiceService.findCustomerInvoices(null, null, null);
  }

  @GetMapping(path = "/insert-invoice")
  @ApiOperation("Inserts generated invoice for customerId and dates")
  public Invoice insertInvoice(
      @ApiParam(value = "customerId")
      @RequestParam Long customerId,
      @RequestParam(name = "fromDate")
      @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
      @RequestParam(name = "toDate")
      @DateTimeFormat(iso = ISO.DATE) LocalDate toDate) {

    ParkingRecords records = parkingService
        .findCustomerParkingRecords(customerId, fromDate, toDate);
    return invoiceService.insertInvoice(records);
  }

}
