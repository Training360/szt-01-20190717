package empapp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
public class EmployeeServiceBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EmployeeDaoBean employeeDaoBean;

    private LogEntryDaoBean logEntryDaoBean;

    private NameTrimmer nameTrimmer;

    @Inject
    public EmployeeServiceBean(EmployeeDaoBean employeeDaoBean, LogEntryDaoBean logEntryDaoBean, NameTrimmer nameTrimmer) {
        this.employeeDaoBean = employeeDaoBean;
        this.logEntryDaoBean = logEntryDaoBean;
        this.nameTrimmer = nameTrimmer;
    }


    @PostConstruct
    public void init() {
        System.out.println("Employee Service bean has created");
    }

    // @Schedule(second = "*/10", minute = "*", hour = "*")
    public void printTime() {
        System.out.println("The time is: " + LocalDateTime.now());
    }

    public List<Employee> findEmployees() {
        logger.info("List employees");
        logger.debug("List employees without parameters");

        return employeeDaoBean.findEmployees();
    }

    @Transactional
    public Employee saveEmployee(String name) {
        name = nameTrimmer.trimName(name);

        Optional<Employee> employee = employeeDaoBean.findEmployeeByName(name);

        if (employee.isPresent()) {
            logEntryDaoBean.saveLogEntry("Already exists: " + name);
            return employee.get();
        }
        else {
            logEntryDaoBean.saveLogEntry("Save employee with name: " + name);
            Employee newEmployee = new Employee(name);
            return employeeDaoBean.saveEmployee(newEmployee);
        }
    }

    public Employee findEmployeeById(long id) {
        return employeeDaoBean.findEmployeeById(id);
    }
}
