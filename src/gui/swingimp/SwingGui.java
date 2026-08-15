package gui.swingimp;

import app.AppCore;

import gui.IGui;

import observer.Notification;
import observer.enums.NotificationCode;

import java.util.Stack;

import javax.swing.*;
import javax.swing.table.TableModel;

public class SwingGui implements IGui {

    private MainFrame mainFrame;

    public SwingGui() {
        start();
    }

    @Override
    public void start() {
        this.mainFrame = MainFrame.getInstance(); // ovde se inicijalizuje gui
    }

    @Override
    public void update(Notification notification) {

        if (notification.getCode().equals(NotificationCode.ERROR)) {

            AppCore.getInstance().getTableModel().setRows(null); // dodato

            JOptionPane.showMessageDialog(this.mainFrame, notification.getData());
        } else if (notification.getCode().equals(NotificationCode.VALIDATOR_ERROR)) {

            AppCore.getInstance().getTableModel().setRows(null); // dodato

            Stack<String> validationMessage = (Stack<String>) notification.getData();
            StringBuilder fullMessage = new StringBuilder();
            while (!validationMessage.isEmpty()) {
                fullMessage.append(validationMessage.pop()).append("\n");
            }
            JOptionPane.showMessageDialog(this.mainFrame, fullMessage.toString());
        } else {
            this.mainFrame.getjTable().setModel((TableModel) notification.getData());
        }
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
