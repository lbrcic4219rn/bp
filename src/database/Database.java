package database;

import resource.data.Row;

import java.util.List;

public interface Database {

    List<Row> readDataFromTable(String sqlQuery);
}
