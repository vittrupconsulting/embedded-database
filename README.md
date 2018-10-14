# embedded-database

By adding the database of choice and adding following to your web.xml the in-memory database will be made available. 

    <listener>
        <listener-class>org.corsaircode.moria.common.derby.DerbyContextListener</listener-class>
    </listener>