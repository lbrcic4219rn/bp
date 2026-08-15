package app;

import database.DataAccessException;
import database.Database;
import database.DatabaseImplementation;
import database.MSSQLrepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;

import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;

import querybuilder.IQueryBuilder;
import querybuilder.QueryBuildResult;
import querybuilder.QueryBuilder;
import querybuilder.compiler.Compiler;
import querybuilder.validator.Validator;

import resource.data.Row;

import utils.Constants;

import java.util.List;

public class AppCore extends PublisherImplementation {

    private final Database database;
    private final IQueryBuilder queryBuilder;

    public AppCore() {
        this(
                new DatabaseImplementation(new MSSQLrepository(defaultSettings())),
                new QueryBuilder(new Validator(), new Compiler()));
    }

    public AppCore(Database database, IQueryBuilder queryBuilder) {
        this.database = database;
        this.queryBuilder = queryBuilder;
    }

    private static Settings defaultSettings() {
        Settings settings = new SettingsImplementation();
        settings.addParameter("mssql_ip", Constants.MSSQL_IP);
        settings.addParameter("mssql_database", Constants.MSSQL_DATABASE);
        settings.addParameter("mssql_username", Constants.MSSQL_USERNAME);
        settings.addParameter("mssql_password", Constants.MSSQL_PASSWORD);
        return settings;
    }

    public Notification runQuery(String source) {
        QueryBuildResult built = queryBuilder.build(source);

        if (!built.validationErrors().isEmpty()) {
            return new Notification(NotificationCode.VALIDATOR_ERROR, built.validationErrors());
        }
        if (!built.isSuccess()) {
            return new Notification(NotificationCode.ERROR, built.compileError());
        }

        try {
            List<Row> rows = database.readDataFromTable(built.sql());
            return new Notification(NotificationCode.DATA_UPDATED, rows);
        } catch (DataAccessException e) {
            return new Notification(NotificationCode.ERROR, e.getMessage());
        }
    }

    public void shutdown() {
        database.close();
    }
}
