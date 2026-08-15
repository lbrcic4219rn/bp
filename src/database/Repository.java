package database;

import resource.data.Row;

import java.util.List;

public interface Repository {

    List<Row> get(String sql_query);
}
