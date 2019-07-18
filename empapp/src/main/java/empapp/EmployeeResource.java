package empapp;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("employees")
public class EmployeeResource {

    @Inject
    private EmployeeServiceBean employeeServiceBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> listEmployees(@QueryParam("start") @DefaultValue("0") long start) {
        System.out.println("Start value: " + start);
        return employeeServiceBean.findEmployees();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Employee findEmployeeById(@PathParam("id") long id) {
        return employeeServiceBean.findEmployeeById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmployee(Employee employeeData) {
        Employee employee = employeeServiceBean.saveEmployee(employeeData.getName());
        return Response.status(201).entity(employee).build();
    }

}
