package empapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeesControllerTest {

    @Mock
    private EmployeeServiceBean employeeServiceBean;

    @Mock
    private FacesContextProvider facesContextProvider;

    @InjectMocks
    private EmployeesController employeesController;

//    @Test
//    public void testSave() {
//        FacesContext facesContext = mock(FacesContext.class);
//        ExternalContext externalContext = mock(ExternalContext.class);
//        Flash flash = mock(Flash.class);
//
//        when(facesContextProvider.getFacesContext()).thenReturn(facesContext);
//        when(facesContext.getExternalContext()).thenReturn(externalContext);
//        when(externalContext.getFlash()).thenReturn(flash);
//
//        employeesController.setName("John Doe");
//        employeesController.addEmployee();
//
//        verify(employeeServiceBean).saveEmployee(eq("John Doe"));
//        verify(flash).put(eq("successMessage"), eq("Employee has created!"));
//    }

    @Test
    public void testSave() {
        employeesController.setName("John Doe");
        employeesController.addEmployee();

        verify(employeeServiceBean).saveEmployee(eq("John Doe"));
        verify(facesContextProvider).setFlashAttribute(eq("successMessage"), eq("Employee has created!"));
    }
}
