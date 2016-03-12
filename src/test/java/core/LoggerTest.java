package core;

import junit.framework.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


/**
 * Created by JKyte on 3/12/2016.
 */
public class LoggerTest {

    @Test
    public void testLogger() {

        Logger log = LogManager.getLogger(getClass());

        log.info("INFO");
        log.debug("DEBUG");
        log.error("ERROR");
        log.fatal("FATAL");

        Assert.assertTrue(true);
    }
}
