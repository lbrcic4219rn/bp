package database;

import resource.data.Row;

import java.util.List;

public class DatabaseImplementation implements Database {

    private final Repository repository;

    public DatabaseImplementation(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Row> readDataFromTable(String sqlQuery) {
        return repository.get(sqlQuery);
    }
}
