package ee.telia.parking.repository;

import ee.telia.parking.domain.entity.Customer;
import ee.telia.parking.domain.type.CustomerType;
import ee.telia.parking.helper.annotation.InjectFileContent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static org.springframework.dao.support.DataAccessUtils.requiredSingleResult;
import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

  private final JdbcTemplate jdbcTemplate;

  @InjectFileContent("classpath:sql/find_customer_by_id.sql")
  private String findCustomerById;

  @InjectFileContent("classpath:sql/find_customer_type_by_id.sql")
  private String getCustomerTypeById;

  public Customer getCustomer(Long id) {
    return requiredSingleResult(
        jdbcTemplate.query(findCustomerById, newInstance(Customer.class), id));
  }

  public CustomerType getCustomerType(Long id) {
    return requiredSingleResult(
        jdbcTemplate.query(getCustomerTypeById, newInstance(Customer.class), id)).getCustomerType();
  }

}
