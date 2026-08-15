package gui.swingImp;

import app.AppCore;
import gui.IGui;
import observer.Notification;
import observer.enums.NotificationCode;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.Stack;


public class SwingGui implements IGui {

    private MainFrame mainFrame;


    public SwingGui(){
        start();
    }

    @Override
    public void start() {
        this.mainFrame = MainFrame.getInstance();//ovde se inicijalizuje gui
    }

    @Override
    public void update(Notification notification) {

        if(notification.getCode().equals(NotificationCode.ERROR)){

            AppCore.getInstance().getTableModel().setRows(null);//dodato

            JOptionPane.showMessageDialog(this.mainFrame,(String) notification.getData());
        }else if(notification.getCode().equals(NotificationCode.VALIDATOR_ERROR)){

            AppCore.getInstance().getTableModel().setRows(null);//dodato

            Stack<String> validationMessage = (Stack<String>) notification.getData();
            String fullMessage = "";
            while (!validationMessage.isEmpty()){
                fullMessage += validationMessage.pop() + "\n";
            }
            JOptionPane.showMessageDialog(this.mainFrame, fullMessage);
        } else {
            this.mainFrame.getjTable().setModel((TableModel) notification.getData());
        }
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

}
