package employee;

import employees.Employee;
import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

public class EmployeeAssert extends AbstractAssert<EmployeeAssert, Employee> {

    public EmployeeAssert(Employee employee) {
        super(employee, EmployeeAssert.class);
    }
    public static EmployeeAssert assertThat(Employee employee) {
        return new EmployeeAssert(employee);
    }
    public EmployeeAssert hasName(String name) {
        if (!Objects.equals(actual.getName(), name)) {
            failWithMessage("Expected employees name to be <%s> but was <%s>",
                    name, actual.getName());
        }
        return this;
    }
}
