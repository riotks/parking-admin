
package ee.telia.parking.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class LocalDao extends JdbcDaoSupport {

    @Autowired
    public LocalDao( @Qualifier("localDataSource") DataSource ds ){
        this.setDataSource( ds );
    }

}
