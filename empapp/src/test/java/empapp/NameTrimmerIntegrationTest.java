package empapp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class NameTrimmerIntegrationTest {

    @Inject
    private NameTrimmer nameTrimmer;


    @Deployment
    public static JavaArchive createDeployment() {
        return
                ShrinkWrap.create(JavaArchive.class)
                .addClass(NameTrimmer.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testTrim() throws SQLException {
        System.out.println("Test trim");
        assertEquals("John Doe", nameTrimmer.trimName("     John Doe     "));
    }

}
