package employee;

import employees.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {

    @Test
    public void testGetAge() {
        Employee employee = new Employee("John Doe", 1970);
        int age = employee.getAge(2019);
        assertEquals(47, age);
    }
}
