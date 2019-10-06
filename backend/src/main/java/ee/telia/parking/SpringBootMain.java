package ee.telia.parking;

import ee.telia.logging.logia.spring.logback.filters.spring.config.LogiaSpringLogbackFiltersConfiguration;
import ee.telia.logging.logia.spring.web.config.LogiaSpringWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({LogiaSpringWebConfiguration.class, LogiaSpringLogbackFiltersConfiguration.class})
public class SpringBootMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class, args);
    }

}
