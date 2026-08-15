package gui.swingImp;

import app.AppCore;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CnfButtonController implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        String query = MainFrame.getInstance().getjTextArea().getText();
        AppCore.getInstance().getQueryBuilder().getValidator().check(query);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
