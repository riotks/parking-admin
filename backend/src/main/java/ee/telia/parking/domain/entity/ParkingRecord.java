package ee.telia.parking.domain.entity;

import ee.telia.parking.domain.type.CustomerType;
import ee.telia.parking.domain.type.ParkingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRecord {

  private Long id;
  private Long customerId;
  private CustomerType customerType;
  private ParkingStatus status;
  private OffsetDateTime beginAt;
  private OffsetDateTime endBy;


/*
  {
    "timestamp": "2019-10-01T13:41:30.592+0000",
      "status": 500,
      "error": "Internal Server Error",
      "message": "No value supplied for the SQL parameter 'type': Invalid property 'type' of bean class [ee.telia.parking.domain.entity.ParkingRecord]: Bean property 'type' is not readable or has an invalid getter method: Does the return type of the getter match the parameter type of the setter?",
      "path": "/parking-api/start/1"
  }
  */
}
