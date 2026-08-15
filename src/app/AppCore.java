package app;

import database.Database;
import database.DatabaseImplementation;
import database.MSSQLrepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;

import gui.IGui;
import gui.swingimp.SwingGui;
import gui.swingimp.TableModel;

import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;

import querybuilder.IQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.compiler.Compiler;
import querybuilder.validator.Validator;

import resource.data.Row;

import utils.Constants;

import java.util.List;

public class AppCore extends PublisherImplementation {

    private static AppCore instance;
    private Database database;
    private Settings settings;
    private TableModel tableModel;
    private IGui gui;
    private IQueryBuilder queryBuilder;

    private AppCore() {}

    public static AppCore getInstance() {
        if (instance == null) {
            instance = new AppCore();
            instance.initAll();
        }
        return instance;
    }

    private void initAll() {
        this.settings = initSettings();
        this.database = new DatabaseImplementation(new MSSQLrepository(this.settings));
        this.tableModel = new TableModel();
        this.gui = new SwingGui();
        this.addSubscriber(gui);
        this.queryBuilder = new QueryBuilder(new Validator(), new Compiler());
    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mssql_ip", Constants.MSSQL_IP);
        settingsImplementation.addParameter("mssql_database", Constants.MSSQL_DATABASE);
        settingsImplementation.addParameter("mssql_username", Constants.MSSQL_USERNAME);
        settingsImplementation.addParameter("mssql_password", Constants.MSSQL_PASSWORD);
        return settingsImplementation;
    }

    public void readDataFromTable(String fromTable) { // anina metoda

        List<Row> rows;
        rows = this.database.readDataFromTable(fromTable);
        if (rows.size() == 1 && rows.getFirst().getFields() == null) {
            this.notifySubscribers(
                    new Notification(NotificationCode.ERROR, rows.getFirst().getName()));
            return;
        }
        tableModel.setRows(this.database.readDataFromTable(fromTable));
    }

    public Database getDatabase() {
        return database;
    }

    public Settings getSettings() {
        return settings;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public IGui getGui() {
        return gui;
    }

    public IQueryBuilder getQueryBuilder() {
        return queryBuilder;
    }
}
