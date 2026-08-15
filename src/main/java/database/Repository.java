package database;

import resource.data.Row;

import java.util.List;

public interface Repository extends AutoCloseable {

    List<Row> get(String sqlQuery);

    @Override
    void close();
}
