package database;

import database.settings.Settings;
import resource.data.Row;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MSSQLrepository implements Repository{

    private Settings settings;
    private Connection connection;

    public MSSQLrepository(Settings settings) {
        this.settings = settings;
    }

    private void initConnection() throws SQLException, ClassNotFoundException{
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String ip = (String) settings.getParameter("mssql_ip");
        String database = (String) settings.getParameter("mssql_database");
        String username = (String) settings.getParameter("mssql_username");
        String password = (String) settings.getParameter("mssql_password");
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+"/"+database,username,password);
    }

    private void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
    }

    @Override
    public List<Row> get(String sql_query) {

        List<Row> rows = new ArrayList<>();


        try{
            this.initConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);

            }
        }
        catch (SQLException sqle){//korisnik je uneo nesto sto ne postoji u bazi; posto metoda mora da vraca listu redova
                                //pravimo red u cijem nazivu cuvamo poruku o gresci, a polja mu setujemo na null; potom se subs obavestavaju u appcore-u;
            Row r = new Row();
            r.setName(sqle.getMessage());
            r.setFields(null);
            rows.add(r);
            return rows;
        }
        catch (Exception e) {//ovde nikad ne bi smelo da se dospe; znaci da postoji neka greska koja nije ispeglana u validatoru
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return rows;

    }

}
