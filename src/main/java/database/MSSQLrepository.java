package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import database.settings.Settings;

import resource.data.Row;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MSSQLrepository implements Repository, AutoCloseable {

    private static final int MAX_POOL_SIZE = 5;
    private static final long CONNECTION_TIMEOUT_MS = 10_000L;

    private final HikariDataSource dataSource;

    public MSSQLrepository(Settings settings) {
        this.dataSource = new HikariDataSource(toHikariConfig(settings));
    }

    private static HikariConfig toHikariConfig(Settings settings) {
        String host = settings.getParameter("mssql_ip");
        String database = settings.getParameter("mssql_database");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:sqlserver://"
                        + host
                        + ";databaseName="
                        + database
                        + ";encrypt=true;trustServerCertificate=true");
        config.setUsername(settings.getParameter("mssql_username"));
        config.setPassword(settings.getParameter("mssql_password"));
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setConnectionTimeout(CONNECTION_TIMEOUT_MS);
        config.setPoolName("bp-mssql");
        config.setInitializationFailTimeout(-1);
        return config;
    }

    @Override
    public List<Row> get(String sqlQuery) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet rs = preparedStatement.executeQuery()) {
            return readRows(rs);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private List<Row> readRows(ResultSet rs) throws SQLException {
        List<Row> rows = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Row row = new Row();
            for (int i = 1; i <= columnCount; i++) {
                row.addField(metaData.getColumnName(i), rs.getString(i));
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
