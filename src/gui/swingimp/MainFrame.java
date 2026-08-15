package gui.swingimp;

import app.AppCore;

import lombok.Getter;

import java.awt.*;

import javax.swing.*;

@Getter
public class MainFrame extends JFrame {

    private static MainFrame instance = null;
    private JLabel jLabel;
    private JTextArea jTextArea;
    private JTable jTable;
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
}
