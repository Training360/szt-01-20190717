package empapp;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Model
public class EmployeesController {

    private EmployeeServiceBean employeeServiceBean;

    private FacesContextProvider facesContextProvider;

    private List<Employee> employees;

    @NotBlank
    private String name;

    @Inject
    public EmployeesController(EmployeeServiceBean employeeServiceBean, FacesContextProvider facesContextProvider) {
        this.employeeServiceBean = employeeServiceBean;
        this.facesContextProvider = facesContextProvider;
    }

    @PostConstruct
    public void init() {
        employees = employeeServiceBean.findEmployees();
    }

    public String addEmployee() {
        employeeServiceBean.saveEmployee(name);
        facesContextProvider.setFlashAttribute("successMessage", "Employee has created!");
        return "employees.xhtml?faces-redirect=true";
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
