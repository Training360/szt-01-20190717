package empapp;

import org.dbunit.assertion.DbUnitAssert;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EmployeeDaoBeanIntegrationTest {

    @Inject
    private EmployeeDaoBean employeeDaoBean;

//    @Inject
//    private AdminRunnerBean adminRunnerBean;

    @Resource(lookup = "java:/jdbc/EmployeeDS")
    private DataSource dataSource;

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        WebArchive webArchive =
                ShrinkWrap.create(WebArchive.class)
                        .addClasses(Employee.class, EmployeeDaoBean.class,
                                AdminRunnerBean.class,
                                DbMigrator.class)
                        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                        .addAsResource("employees.xml", "employees.xml")

                        .addAsLibraries(Maven.configureResolver().loadPomFromFile("pom.xml").resolve("org.flywaydb:flyway-core").withoutTransitivity().asSingleFile())
                        .addAsLibraries(Maven.configureResolver().loadPomFromFile("pom.xml").resolve("org.dbunit:dbunit").withoutTransitivity().asSingleFile())
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

    @Test
    public void testFindEmployees() throws Exception {
//        try (Connection c = dataSource.getConnection();
//             PreparedStatement psDelete = c.prepareStatement("delete from employees");
//             PreparedStatement ps = c.prepareStatement("insert into employees(emp_name) values (?)")) {
//            psDelete.executeUpdate();
//            ps.setString(1, "John Doe");
//            ps.executeUpdate();
//        }

        DatabaseOperation.CLEAN_INSERT.execute(new DatabaseDataSourceConnection(dataSource),
                new XmlDataSet(EmployeeDaoBeanIntegrationTest.class.getResourceAsStream("/employees.xml")));

        List<Employee> employees = employeeDaoBean.findEmployees();
        assertEquals(Arrays.asList("Jack Doe", "John Doe"), employees.stream()
                .map(Employee::getName).collect(Collectors.toList()));
    }

    @Test
    public void testSaveEmployee() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(new DatabaseDataSourceConnection(dataSource),
                new XmlDataSet(EmployeeDaoBeanIntegrationTest.class.getResourceAsStream("/employees.xml")));


        employeeDaoBean.saveEmployee(new Employee("John Doe"));
        employeeDaoBean.saveEmployee(new Employee("Jack Doe"));

        ITable expectedTable = DefaultColumnFilter
                .includedColumnsTable((new XmlDataSet(EmployeeDaoBeanIntegrationTest.class
                .getResourceAsStream("/employees.xml"))
                .getTable("employees")), new String[]{"emp_name"});
        ITable databaseTable = DefaultColumnFilter
                .includedColumnsTable(new DatabaseDataSourceConnection(dataSource)
                .createDataSet().getTable("employees"), new String[]{"emp_name"});

        new DbUnitAssert().assertEquals(expectedTable, databaseTable);
    }

}
