package ee.telia.parking.controller;

import ee.telia.parking.domain.entity.Customer;
import ee.telia.parking.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping(path = "/customers")
  @ApiOperation("Finds customer record")
  public Customer getCustomer(
      @ApiParam(required = true, value = "id")
      @RequestParam Long id) {
    return customerService.getCustomer(id);
  }

}
