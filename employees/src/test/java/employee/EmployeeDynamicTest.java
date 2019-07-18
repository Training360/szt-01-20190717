package employee;

import employees.Employee;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class EmployeeDynamicTest {

    private static DynamicTest convertToTest(int[] item) {
        return dynamicTest(
                String.format("In %d  the employee is %d years old ",
                        item[0], item[1]),
                () -> assertEquals(item[1],
                        new Employee("John", 1970)
                                .getAge(item[0]))
        );
    }

    @TestFactory
    Stream<DynamicTest> testGetAge() {
        return Arrays.stream(new int[][] {{2019, 49},
                {2010, 40}, {2000, 30}, {1970,0}})
                .map(EmployeeDynamicTest::convertToTest
                );
    }
}
