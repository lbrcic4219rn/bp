package database;

import database.settings.Settings;

import resource.data.Row;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSSQLrepository implements Repository {

    private static final Logger LOGGER = Logger.getLogger(MSSQLrepository.class.getName());

    private final Settings settings;
    private Connection connection;

    public MSSQLrepository(Settings settings) {
        this.settings = settings;
    }

    private void initConnection() throws SQLException {
        String ip = (String) settings.getParameter("mssql_ip");
        String database = (String) settings.getParameter("mssql_database");
        String username = (String) settings.getParameter("mssql_username");
        String password = (String) settings.getParameter("mssql_password");
        connection =
                DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://" + ip + "/" + database, username, password);
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to close connection", e);
        } finally {
            connection = null;
        }
    }

    @Override
    public List<Row> get(String sqlQuery) {

        List<Row> rows = new ArrayList<>();

        try {
            this.initConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {

                    Row row = new Row();

                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                    }
                    rows.add(row);
                }
            }
        } catch (
                SQLException
                        sqle) { // korisnik je uneo nesto sto ne postoji u bazi; posto metoda mora
            // da vraca listu redova
            // pravimo red u cijem nazivu cuvamo poruku o gresci, a polja mu setujemo na null; potom
            // se subs obavestavaju u appcore-u;
            Row r = new Row();
            r.setName(sqle.getMessage());
            r.setFields(null);
            rows.add(r);
            return rows;
        } catch (Exception e) { // ovde nikad ne bi smelo da se dospe; znaci da postoji neka greska
            // koja nije ispeglana u validatoru
            LOGGER.log(Level.SEVERE, "Unexpected error while reading table", e);
        } finally {
            this.closeConnection();
        }

        return rows;
    }
}
