package ee.telia.parking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DatabaseConfig {

  @Autowired
  private Environment env;

  @Bean(name = "localDataSource")
  public DataSource localDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("local.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("local.datasource.url"));
    dataSource.setUsername(env.getProperty("local.datasource.username"));
    dataSource.setPassword(env.getProperty("local.datasource.password"));
    return dataSource;
  }

}
