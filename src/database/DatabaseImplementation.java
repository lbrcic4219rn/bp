package database;

import resource.data.Row;

import java.util.List;

public class DatabaseImplementation implements Database {

    private Repository repository;

    public DatabaseImplementation(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Row> readDataFromTable(String sql_query) {
        return repository.get(sql_query);
    }
}
