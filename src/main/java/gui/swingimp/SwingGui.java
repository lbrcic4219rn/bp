package gui.swingimp;

import app.AppCore;

import gui.IGui;

import lombok.Getter;

import observer.Notification;
import observer.enums.NotificationCode;

import resource.data.Row;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class SwingGui implements IGui {

    private static final Logger LOGGER = Logger.getLogger(SwingGui.class.getName());

    private final AppCore core;
    private final TableModel tableModel = new TableModel();

    @Getter private MainFrame mainFrame;

    public SwingGui(AppCore core) {
        this.core = core;
    }

    @Override
    public void start() {
        this.mainFrame = new MainFrame(tableModel, this::submit);
    }

    private void submit(String source) {
        new SwingWorker<Notification, Void>() {

            @Override
            protected Notification doInBackground() {
                return core.runQuery(source);
            }

            @Override
            protected void done() {
                try {
                    core.notifySubscribers(get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Query execution failed", e);
                    showMessage(String.valueOf(e.getCause()));
                }
            }
        }.execute();
    }

    @Override
    public void update(Notification notification) {
        if (notification.getCode() == NotificationCode.ERROR) {
            tableModel.setRows(null);
            showMessage(String.valueOf(notification.getData()));
        } else if (notification.getCode() == NotificationCode.VALIDATOR_ERROR) {
            tableModel.setRows(null);
            showMessage(joinMessages(notification.getData()));
        } else {
            tableModel.setRows(asRows(notification.getData()));
        }
    }

    private String joinMessages(Object data) {
        if (!(data instanceof List<?> messages)) {
            return String.valueOf(data);
        }
        StringBuilder fullMessage = new StringBuilder();
        for (Object message : messages) {
            fullMessage.append(message).append('\n');
        }
        return fullMessage.toString();
    }

    private List<Row> asRows(Object data) {
        if (data instanceof List<?> list) {
            return list.stream().filter(Row.class::isInstance).map(Row.class::cast).toList();
        }
        return List.of();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message);
    }
}
