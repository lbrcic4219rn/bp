package gui.swingimp;

import app.AppCore;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CnfButtonController extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        String query = MainFrame.getInstance().getJTextArea().getText();
        AppCore.getInstance().getQueryBuilder().getValidator().check(query);
    }
}
