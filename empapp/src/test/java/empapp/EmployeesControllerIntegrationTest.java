package empapp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
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
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EmployeesControllerIntegrationTest {

    @Resource(lookup = "java:/jdbc/EmployeeDS")
    private DataSource dataSource;

    @Inject
    private EmployeesController employeesController;

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        WebArchive webArchive =
                ShrinkWrap.create(WebArchive.class)
                        .addPackage(Employee.class.getPackage())
                        .addClass(StubFacesContextProvider.class)
//                        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                        .addAsWebInfResource(new StringAsset("<beans><alternatives><class>empapp.StubFacesContextProvider</class></alternatives></beans>"),
                                "beans.xml")
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
        employeesController.setName("John Doe");
        employeesController.addEmployee();

        employeesController.init();
        List<Employee> employees = employeesController.getEmployees();
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
    }

}
