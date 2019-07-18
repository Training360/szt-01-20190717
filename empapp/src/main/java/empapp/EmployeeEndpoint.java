package empapp;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@Stateless
@WebService(targetNamespace = "http://training360.com/empapp")
public class EmployeeEndpoint {

    @Inject
    private EmployeeServiceBean employeeServiceBean;

    @WebMethod
    @WebResult(name = "employee")
    public List<Employee> listEmployees() {
        return employeeServiceBean.findEmployees();
    }
}
