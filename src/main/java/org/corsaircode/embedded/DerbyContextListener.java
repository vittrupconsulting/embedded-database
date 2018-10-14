package org.corsaircode.moria.common.derby;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyContextListener implements ServletContextListener {


//driverClassName=org.apache.derby.jdbc.EmbeddedDriver
//url=jdbc:derby:memory:myDb;create=true
//username=sa
//password=sa

    private static final Logger log = Logger.getLogger(DerbyContextListener.class.getName());

    public DerbyContextListener() {
        log.log(Level.INFO, "{0} instantiated", this.getClass().getName());
    }

    private static final String CREATE = "jdbc:derby:" + System.getProperty("java.io.tmpdir") + "/database;create=true";
    private static final String SHUTDOWN = "jdbc:derby:" + System.getProperty("java.io.tmpdir") + "/database;shutdown=true";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Connection cn = null;
        try {
            cn = DriverManager.getConnection(CREATE);
            log.info("Derby DB started using " + CREATE);
            log.log(Level.SEVERE, "Derby create failed (no exception occurred).");
        } catch (SQLException e) {
            log.log(Level.INFO, "Derby create failed or may not yet loaded. message: {0}", e.getMessage());
            log.log(Level.FINER, "Derby create failed", e);
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    log.log(Level.WARNING, "Database create error", e);
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Connection cn = null;
        try {
            cn = DriverManager.getConnection(SHUTDOWN);
            log.info("Derby DB shutdown using " + SHUTDOWN);
            log.log(Level.SEVERE, "Derby shutdown failed (no exception occurred).");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                log.log(Level.INFO, "Derby shutdown succeeded. SQLState={0}, message={1}",
                        new Object[]{e.getSQLState(), e.getMessage()});
                log.log(Level.FINEST, "Derby shutdown exception", e);
                return;
            }
            log.log(Level.INFO, "Derby shutdown failed or may not yet loaded. message: {0}", e.getMessage());
            log.log(Level.FINER, "Derby shutdown failed", e);
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    log.log(Level.WARNING, "Database closing error", e);
                }
            }
        }
    }
}
