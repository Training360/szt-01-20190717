package empapp;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Stateless
public class LogEntryDaoBean {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveLogEntry(String message) {
        em.persist(new LogEntry(message));
    }
}
