package empapp;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless
public class EmployeeDaoBean {

    @PersistenceContext
    private EntityManager em;

    public List<Employee> findEmployees() {
        return em.createQuery("select e from Employee e", Employee.class).getResultList();
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        em.persist(employee);
        return employee;
    }

    public Employee findEmployeeById(long id) {
        return em.find(Employee.class, id);
    }

    public Optional<Employee> findEmployeeByName(String name) {
        List<Employee> employees = em.createQuery("select e from Employee e where e.name = :name", Employee.class)
                .setParameter("name", name)
                .getResultList();
        if (employees.size() == 0) {
            return Optional.empty();
        }
        else if (employees.size() == 1) {
            return Optional.of(employees.get(0));
        }
        else {
            throw new IllegalStateException("Can not be employee with same name");
        }
    }
}
