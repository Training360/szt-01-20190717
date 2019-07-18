package empapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EmployeeServiceBeanTest {

    @Test
    public void testSave() {
        EmployeeDaoBean dao = mock(EmployeeDaoBean.class);
        LogEntryDaoBean log = mock(LogEntryDaoBean.class);
        NameTrimmer trimmer = mock(NameTrimmer.class);

        when(dao.saveEmployee(any())).thenReturn(new Employee("John DoeXXX"));
        when(trimmer.trimName(any())).thenReturn("John Doe TRIMMED");

        EmployeeServiceBean bean = new EmployeeServiceBean(dao, log, trimmer);
        Employee employee = bean.saveEmployee("John Doe");
        assertEquals("John DoeXXX", employee.getName());

        verify(trimmer).trimName(eq("John Doe"));

        verify(dao).saveEmployee("John Doe TRIMMED");
        verify(log).saveLogEntry("Save employee with name: John Doe TRIMMED");
    }
}
