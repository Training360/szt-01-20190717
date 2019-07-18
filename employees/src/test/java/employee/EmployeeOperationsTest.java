package employee;

import employees.Employee;
import employees.EmployeeOperations;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeOperationsTest {

    @Test
    void testFilter() {
        List<Employee> employees = Arrays.asList(
                new Employee("John Doe", 2001),
                new Employee("John Doe", 2012),
                new Employee("John Doe", 2000),
                new Employee("John Doe", 1980),
                new Employee("Jack Doe", 2000)
                );
        List<Employee> result = new EmployeeOperations().filterYearOfBirthEquals(2000, employees);

//        assertEquals(2, result.size());
//        assertEquals("John Doe", result.get(0).getName());

        assertEquals(Arrays.asList("John Doe", "Jack Doe"), result.stream().map(Employee::getName)
                .collect(Collectors.toList()));

    }
}
