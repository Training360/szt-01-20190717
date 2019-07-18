package empapp;

import org.flywaydb.core.Flyway;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DbMigrator {

    // Wildfly: @Resource(lookup = "java:/jdbc/EmployeeDS")
    // Wildfly: @Resource(mappedName = "java:/jdbc/EmployeeDS")
    // Payara: @Resource(lookup = "jdbc/EmployeeDS")
    @Resource(lookup = "java:comp/env/jdbc/EmployeeDS")
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }
}
