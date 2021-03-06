package employee;

import employees.Employee;
import org.junit.jupiter.api.*;

import static employee.EmployeeAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    Employee employee;

    @BeforeEach
    void createEmployee() {
        System.out.println("init");

    }

    public EmployeeTest() {
        System.out.println("constructor");
    }

    @Nested
    class EmployeeWith1970 {

        @BeforeEach
        void createEmployee() {
            employee = new Employee("John Doe", 1970);
        }

        @Test
        @DisplayName("Test get age with 2019")
        void testGetAge() {
            int age = employee.getAge(2019);
            assertEquals(49, age);
//            assertThat(age).isGreaterThan(20).isLessThan(200);
            assertThat(employee).hasName("John Doe");
        }
    }



    @Test
    void testGetAgeWith2000() {
        employee = new Employee("John Doe", 1980);
        int age = employee.getAge(2000);
        assertEquals(20, age);
    }

    @Test
    public void testYearLowerThan1700() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Employee("John Doe", 1600));
        assertEquals("Can not be lower than 1700", e.getMessage());
    }
}
