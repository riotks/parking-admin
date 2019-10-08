package ee.telia.parking.repository;

import ee.telia.parking.domain.type.ParkingStatus;
import ee.telia.parking.helper.annotation.InjectFileContent;
import ee.telia.parking.domain.entity.ParkingRecord;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.requiredSingleResult;
import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Repository
@RequiredArgsConstructor
public class ParkingRepository {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @InjectFileContent("classpath:sql/insert_parking_record.sql")
  private String insertParkingRecordSql;

  @InjectFileContent("classpath:sql/complete_parking_record.sql")
  private String completeParkingRecordSql;

  @InjectFileContent("classpath:sql/complete_customer_parking_record.sql")
  private String completeCustomerParkingRecordSql;

  @InjectFileContent("classpath:sql/find_parking_record_by_id.sql")
  private String findParkingRecordByIdSql;

  @InjectFileContent("classpath:sql/find_parking_record_by_customer_and_dates.sql")
  private String findParkingRecordByCustomerAndDatesSql;


  public int insertParkingRecord(ParkingRecord parkingRecord) {
    return namedParameterJdbcTemplate
        .update(insertParkingRecordSql, getSqlParameterSource(parkingRecord));
  }

  @Transactional
  public int completeParkingRecord(Long parkingRecordId) {
    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("status", ParkingStatus.COMPLETED, Types.VARCHAR)
        .addValue("end_by", OffsetDateTime.now(), Types.TIMESTAMP)
        .addValue("id", parkingRecordId);
    return namedParameterJdbcTemplate.update(completeParkingRecordSql, params);
  }

  @Transactional
  public int completeCustomerParkingRecord(Long customerId) {
    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("status", ParkingStatus.COMPLETED, Types.VARCHAR)
        .addValue("end_by", OffsetDateTime.now(), Types.TIMESTAMP)
        .addValue("customer_id", customerId);
    return namedParameterJdbcTemplate.update(completeCustomerParkingRecordSql, params);
  }

  public ParkingRecord findParkingRecord(Long id) {
    return requiredSingleResult(
        jdbcTemplate.query(findParkingRecordByIdSql, newInstance(ParkingRecord.class), id));
  }

  public List<ParkingRecord> findCustomerParkingRecords(Long customerId,
      LocalDate fromDate, LocalDate toDate) {
    MapSqlParameterSource parameterSource = getMapSqlParameterSource(customerId, fromDate, toDate);
    return namedParameterJdbcTemplate
        .query(findParkingRecordByCustomerAndDatesSql, parameterSource,
            new BeanPropertyRowMapper<ParkingRecord>(ParkingRecord.class));
  }

  private MapSqlParameterSource getMapSqlParameterSource(Long customerId, LocalDate fromDate,
      LocalDate toDate) {
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValue("customerId", customerId);
    parameterSource.addValue("fromDate", fromDate);
    parameterSource.addValue("toDate", toDate);
    return parameterSource;
  }

  private SqlParameterSource getSqlParameterSource(ParkingRecord parkingRecord) {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(
        parkingRecord);
    parameterSource.registerSqlType("customerId", Types.VARCHAR);
    parameterSource.registerSqlType("customerType", Types.VARCHAR);
    parameterSource.registerSqlType("status", Types.VARCHAR);
    parameterSource.registerSqlType("beginAt", Types.TIMESTAMP);
    return parameterSource;
  }

}
