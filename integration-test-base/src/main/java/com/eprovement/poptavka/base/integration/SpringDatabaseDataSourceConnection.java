package com.eprovement.poptavka.base.integration;


import org.dbunit.database.DatabaseDataSourceConnection;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * We want to setup DbUnit to not commit its data, so that it would automatically be rolled back
 * if we used Springs ATJ4SCT.
 *
 * @see http://blog.avegagroup.se/MikaelAmborn/archive/2011/03/09/how-we-improved-the-speed-of-our-dbunit-tests.aspx
 *
 * @author Juraj Martinka
 *         Date: 3.4.11
 *
 */
public class SpringDatabaseDataSourceConnection extends DatabaseDataSourceConnection {
    private final DataSource dataSource;

    public SpringDatabaseDataSourceConnection(DataSource dataSource) throws SQLException {
        super(dataSource);
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        return new SpringConnection(dataSource, conn);
    }
}
