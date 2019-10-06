package ee.telia.parking.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class ParkingRecords {

  private Customer customer;
  private List<ParkingRecord> records;
}
