package ee.telia.parking.service;

import ee.telia.parking.domain.entity.ParkingRecord;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static ee.telia.parking.domain.type.CustomerType.PREMIUM;
import static ee.telia.parking.domain.type.CustomerType.REGULAR;
import static ee.telia.parking.domain.type.ParkingStatus.NEW;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

@Transactional
@RunWith(MockitoJUnitRunner.class)
public class ParkingServiceTest {

  private static final Long REGULAR_CUSTOMER_ID = 123L;

  @Spy
  @InjectMocks
  ParkingService parkingService;

  @Test
  public void buildParkingRecord_forRegularCustomer() {
    ParkingRecord parkingRecord = ParkingRecord.builder().customerId(123L).customerType(REGULAR)
        .build();
    ParkingRecord response = parkingService
        .buildNewParkingRecord(parkingRecord.getCustomerId(), parkingRecord.getCustomerType());

    assertThat(response.getCustomerId(), is(123L));
    assertThat(response.getCustomerType(), is(REGULAR));
    assertThat(response.getStatus(), is(NEW));
    assertThat(response.getBeginAt().toString(), response.getBeginAt()
        .isAfter(OffsetDateTime.now().minusSeconds(5L))); // generic amount to account for timing
    assertThat(response.getEndBy(), is(nullValue()));
  }

  @Test
  public void buildParkingRecord_forPremiumCustomer() {
    ParkingRecord parkingRecord = ParkingRecord.builder().customerId(678L).customerType(PREMIUM)
        .build();
    ParkingRecord response = parkingService
        .buildNewParkingRecord(parkingRecord.getCustomerId(), parkingRecord.getCustomerType());

    assertThat(response.getCustomerId(), is(678L));
    assertThat(response.getCustomerType(), is(PREMIUM));
    assertThat(response.getStatus(), is(NEW));
    assertThat(response.getBeginAt().toString(), response.getBeginAt()
        .isAfter(OffsetDateTime.now().minusSeconds(5L))); // generic amount to account for timing
    assertThat(response.getEndBy(), is(nullValue()));
  }

}