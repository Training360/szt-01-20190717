package empapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceBeanTest {

    @Mock
    EmployeeDaoBean dao;

    @Mock
    LogEntryDaoBean log;

    @Mock
    NameTrimmer trimmer;

    @InjectMocks
    EmployeeServiceBean bean;

    @Test
    public void testSave() {
        when(dao.saveEmployee(any())).thenReturn(new Employee("John DoeXXX"));
        when(trimmer.trimName(any())).thenReturn("John Doe TRIMMED");

        Employee employee = bean.saveEmployee("John Doe");
        assertEquals("John DoeXXX", employee.getName());

        verify(trimmer).trimName(eq("John Doe"));

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(dao).saveEmployee(employeeArgumentCaptor.capture());

        assertEquals("John Doe TRIMMED", employeeArgumentCaptor.getValue().getName());

        verify(log).saveLogEntry("Save employee with name: John Doe TRIMMED");
    }

    @Test
    public void testSaveWhenEmployeeExists() {
        when(dao.findEmployeeByName(any())).thenReturn(Optional.of(new Employee(1L, "John DoeXXX")));

        Employee employee = bean.saveEmployee("John Doe");
        assertEquals("John DoeXXX", employee.getName());
        verify(dao, never()).saveEmployee(any());
    }
}
