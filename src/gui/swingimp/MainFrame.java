package gui.swingimp;

import app.AppCore;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static MainFrame instance = null;
    private JLabel jLabel;
    private JTextArea jTextArea;
    private JTable jTable;
    private JScrollPane jsp;
    private JButton jButton;

    private MainFrame() {}

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
            instance.initialise();
        }
        return instance;
    }

    private void initialise() {

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        this.setSize(screenWidth, screenHeight);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Database app");

        this.jLabel = new JLabel("Enter your query: ");
        this.jTextArea = new JTextArea(20, 30);

        this.jTextArea.setForeground(new Color(0, 0, 255));
        this.jTextArea.setBackground(new Color(192, 192, 192));

        this.jTable = new JTable();

        this.jTable.setModel(AppCore.getInstance().getTableModel());

        this.jButton = new JButton("confirm");
        this.jButton.addMouseListener(new CnfButtonController());
        this.jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        this.jTable.setFillsViewportHeight(true);

        this.add(jLabel, BorderLayout.NORTH);
        this.add(jTextArea, BorderLayout.WEST);
        this.add(new JScrollPane(jTable), BorderLayout.CENTER);
        this.add(jButton, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void setInstance(MainFrame instance) {
        MainFrame.instance = instance;
    }

    public JTable getjTable() {
        return jTable;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

    public JScrollPane getJsp() {
        return jsp;
    }

    public void setJsp(JScrollPane jsp) {
        this.jsp = jsp;
    }

    public JLabel getjLabel() {
        return jLabel;
    }

    public void setjLabel(JLabel jLabel) {
        this.jLabel = jLabel;
    }

    public JTextArea getjTextArea() {
        return jTextArea;
    }

    public void setjTextArea(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    public JButton getjButton() {
        return jButton;
    }

    public void setjButton(JButton jButton) {
        this.jButton = jButton;
    }
}
