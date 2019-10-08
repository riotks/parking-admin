package ee.telia.parking.repository;

import static org.springframework.dao.support.DataAccessUtils.requiredSingleResult;
import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

import ee.telia.parking.domain.entity.Invoice;
import ee.telia.parking.helper.annotation.InjectFileContent;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InvoiceRepository {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @InjectFileContent("classpath:sql/find_invoice_by_id.sql")
  private String findInvoiceByIdSql;

  @InjectFileContent("classpath:sql/find_invoices_by_customer_and_dates.sql")
  private String findInvoicesByCustomerAndDatesSql;

  @InjectFileContent("classpath:sql/insert_invoice.sql")
  private String insertInvoiceSql;

  public Invoice findInvoice(Long id) {
    return requiredSingleResult(
        jdbcTemplate.query(insertInvoiceSql, newInstance(Invoice.class), id));
  }

  public List<Invoice> findCustomerInvoices(Long customerId,
      LocalDate fromDate, LocalDate toDate) {
    MapSqlParameterSource parameterSource = getMapSqlParameterSource(customerId, fromDate, toDate);
    return namedParameterJdbcTemplate
        .query(findInvoicesByCustomerAndDatesSql, parameterSource,
            new BeanPropertyRowMapper<Invoice>(Invoice.class));
  }

  public Invoice insertInvoice(Invoice invoice) {
    Long id = namedParameterJdbcTemplate
        .queryForObject(insertInvoiceSql, new BeanPropertySqlParameterSource(invoice), Long.class);

    invoice.setId(id);
    return invoice;
  }

  private MapSqlParameterSource getMapSqlParameterSource(Long customerId, LocalDate fromDate,
      LocalDate toDate) {
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValue("customerId", customerId);
    parameterSource.addValue("fromDate", fromDate);
    parameterSource.addValue("toDate", toDate);
    return parameterSource;
  }

}
