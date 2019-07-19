package empapp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.security.PrivilegedExceptionAction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EmployeeDaoBeanIntegrationTest {

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
                                DbMigrator.class, AdminRunnerBean.class)
                        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")

                        .addAsLibraries(Maven.resolver().resolve("org.flywaydb:flyway-core:5.2.4").withoutTransitivity().asSingleFile())
                ;

        Files.walk(Paths.get("src/main/resources"))
                .filter(p -> !p.toString().contains("META-INF"))
                .filter(p -> Files.isRegularFile(p))
                .map(p -> Paths.get("src/main/resources").relativize(p))
                .map(p -> p.toString().replace("\\", "/"))
                .peek(System.out::println)
                .forEach(s -> webArchive.addAsResource(s, s));

        System.out.println(webArchive.toString(true));

        return webArchive;
    }

    @Test
    public void testFindEmployees() throws Exception {
        try (Connection c = dataSource.getConnection();
             PreparedStatement psDelete = c.prepareStatement("delete from employees");
             PreparedStatement ps = c.prepareStatement("insert into employees(emp_name) values (?)")) {
            psDelete.executeUpdate();
            ps.setString(1, "John Doe");
            ps.executeUpdate();
        }

        List<Employee> employees = adminRunnerBean.call(employeeDaoBean::findEmployees);

        //List<Employee> employees = employeeDaoBean.findEmployees();
        assertEquals(Arrays.asList("John Doe"), employees.stream()
                .map(Employee::getName).collect(Collectors.toList()));
    }

}
