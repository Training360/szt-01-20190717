package empapp;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class EmployeeDaoBean {

    @Inject
    private NameTrimmer nameTrimmer;

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    public List<Employee> findEmployees() {
        return em.createQuery("select e from Employee e", Employee.class).getResultList();
    }

    @Transactional
    public Employee saveEmployee(String name) {
        Employee employee = new Employee(name);
        em.persist(employee);

        if (name.equals("")) {
            sessionContext.setRollbackOnly();
        }

        return employee;
    }

    public Employee findEmployeeById(long id) {
        return em.find(Employee.class, id);
    }
}
