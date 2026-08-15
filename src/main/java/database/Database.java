package database;

import resource.data.Row;

import java.util.List;

public interface Database extends AutoCloseable {

    List<Row> readDataFromTable(String sqlQuery);

    @Override
    void close();
}
