package employee;

import employees.Employee;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    Employee employee;

    @BeforeEach
    void createEmployee() {
        System.out.println("init");
        employee = new Employee("John Doe", 1970);
    }

    public EmployeeTest() {
        System.out.println("constructor");
    }

    @Test
    @DisplayName("Test get age with 2019")
    void testGetAge() {
        int age = employee.getAge(2019);
        assertEquals(49, age);
    }

    @Test
    void testGetAgeWith2000() {
        System.out.println("tc2");
        int age = employee.getAge(2000);
        assertEquals(30, age);
    }

    @Test
    public void testYearLowerThan1700() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Employee("John Doe", 1600));
        assertEquals("Can not be lower than 1700", e.getMessage());
    }
}
