package empapp;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.flywaydb.core.Flyway;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EmployeeDaoBeanBusinessIntegrationTest {

    @Inject
    private EmployeeDaoBean employeeDaoBean;

    @Inject
    private AdminRunnerBean adminRunnerBean;

    @Resource(lookup = "java:/jdbc/EmployeeDS")
    private DataSource dataSource;

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        WebArchive webArchive =
                ShrinkWrap.create(WebArchive.class)
                        .addClasses(Employee.class, EmployeeDaoBean.class,
                                AdminRunnerBean.class, DbMigrator.class)
                        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                        .addAsResource("employees.xml", "employees.xml")

                        .addAsLibraries(Maven.resolver().resolve("org.flywaydb:flyway-core:5.2.4").withoutTransitivity().asSingleFile())
                        .addAsLibraries(Maven.resolver().resolve("org.dbunit:dbunit:2.6.0").withoutTransitivity().asSingleFile())
                ;

        Files.walk(Paths.get("src/main/resources"))
                .filter(p -> !p.toString().contains("META-INF"))
                .filter(p -> Files.isRegularFile(p))
                .map(p -> Paths.get("src/main/resources").relativize(p))
                .map(p -> p.toString().replace("\\", "/"))
//                .peek(System.out::println)
                .forEach(s -> webArchive.addAsResource(s, s));

//        System.out.println(webArchive.toString(true));

        return webArchive;
    }

    @Before
    public void purge() throws Exception {
        try (Connection c = dataSource.getConnection();
             PreparedStatement psDelete = c.prepareStatement("delete from employees")) {
             psDelete.executeUpdate();
        }
    }

    @Test
    public void testSaveThenList() {
        // Given
        employeeDaoBean.saveEmployee(new Employee("John Doe"));
        employeeDaoBean.saveEmployee(new Employee("Jane Doe"));

        // When
        List<Employee> employees = employeeDaoBean.findEmployees();
        assertEquals(2, employees.size());

        Optional<Employee> employee = employeeDaoBean.findEmployeeByName("John Doe");
        assertEquals("John Doe", employee.get().getName());

        Employee loaded = employeeDaoBean.findEmployeeById(employee.get().getId());
        assertEquals("John Doe", loaded.getName());
    }

}
