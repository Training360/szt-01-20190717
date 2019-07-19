package empapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmployeesControllerTest {

    @Mock
    private EmployeeServiceBean employeeServiceBean;

    @InjectMocks
    private EmployeesController employeesController;

    @Test
    public void testSave() {
        employeesController.setName("John Doe");
        employeesController.addEmployee();

        verify(employeeServiceBean).saveEmployee(eq("John Doe"));
    }
}
