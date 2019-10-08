package ee.telia.parking.service;

import ee.telia.parking.domain.entity.Customer;
import ee.telia.parking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  public Customer getCustomer(Long customerId) {
    return customerRepository.getCustomer(customerId);
  }

}
