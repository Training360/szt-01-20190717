package empapp;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import java.util.concurrent.Callable;

@Stateless
@RunAs("admin")
@PermitAll
public class AdminRunnerBean {

    public <V> V call(Callable<V> callable) throws Exception {
        return callable.call();
    }
}
