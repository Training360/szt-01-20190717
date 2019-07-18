package empapp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
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

    @Resource(lookup = "java:/jdbc/EmployeeDS")
    private DataSource dataSource;

    @Deployment
    public static JavaArchive createDeployment() {
        return
                ShrinkWrap.create(JavaArchive.class)
                        .addClasses(Employee.class, EmployeeDaoBean.class,
                                DbMigrator.class)
                        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "persistence.xml");
    }

    @Test
    public void testFindEmployees() throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("insert into employees(emp_name) values (?)")) {
            ps.setString(1, "John Doe");
            ps.executeUpdate();
        }

        List<Employee> employees = employeeDaoBean.findEmployees();
        assertEquals(Arrays.asList("John Doe"), employees.stream()
                .map(Employee::getName).collect(Collectors.toList()));
    }

}
