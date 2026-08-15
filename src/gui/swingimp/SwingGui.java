package gui.swingimp;

import app.AppCore;

import gui.IGui;

import lombok.Getter;

import observer.Notification;
import observer.enums.NotificationCode;

import java.util.Stack;

import javax.swing.*;
import javax.swing.table.TableModel;

@Getter
public class SwingGui implements IGui {

    private MainFrame mainFrame;

    public SwingGui() {
        start();
    }

    @Override
    public void start() {
        this.mainFrame = MainFrame.getInstance();
    }

    @Override
    public void update(Notification notification) {

        if (notification.getCode().equals(NotificationCode.ERROR)) {

            AppCore.getInstance().getTableModel().setRows(null);

            JOptionPane.showMessageDialog(this.mainFrame, notification.getData());
        } else if (notification.getCode().equals(NotificationCode.VALIDATOR_ERROR)) {

            AppCore.getInstance().getTableModel().setRows(null);

            Stack<?> validationMessage = (Stack<?>) notification.getData();
            StringBuilder fullMessage = new StringBuilder();
            while (!validationMessage.isEmpty()) {
                fullMessage.append(validationMessage.pop()).append("\n");
            }
            JOptionPane.showMessageDialog(this.mainFrame, fullMessage.toString());
        } else {
            this.mainFrame.getJTable().setModel((TableModel) notification.getData());
        }
    }
}
