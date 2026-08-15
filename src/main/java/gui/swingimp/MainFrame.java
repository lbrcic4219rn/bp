package gui.swingimp;

import lombok.Getter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Serial;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {

    @Serial private static final long serialVersionUID = 1L;

    private final transient Consumer<String> onSubmit;

    @Getter private final transient JTextArea queryArea = new JTextArea(20, 30);

    @Getter private final transient JTable resultTable = new JTable();

    public MainFrame(TableModel tableModel, Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;
        initialise(tableModel);
    }

    private void initialise(TableModel tableModel) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Database app");

        queryArea.setForeground(new Color(0, 0, 255));
        queryArea.setBackground(new Color(192, 192, 192));

        resultTable.setModel(tableModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        resultTable.setFillsViewportHeight(true);

        JButton confirmButton = new JButton("confirm");
        confirmButton.addActionListener(e -> onSubmit.accept(queryArea.getText()));

        add(new JLabel("Enter your query: "), BorderLayout.NORTH);
        add(queryArea, BorderLayout.WEST);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
