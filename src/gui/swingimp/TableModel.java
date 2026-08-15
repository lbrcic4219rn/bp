package gui.swingimp;

import resource.data.Row;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    private transient List<Row> rows;

    private void updateModel() {
        if (rows == null || rows.isEmpty()) {
            Vector<Object> columnV = new Vector<>();
            Vector<Vector<Object>> dataV = new Vector<>(0);
            setDataVector(dataV, columnV);
            return;
        }

        int columnCount = rows.getFirst().getFields().size();
        Vector<Object> columnVector = new Vector<>(rows.getFirst().getFields().keySet());

        Vector<Vector<Object>> dataVector = new Vector<>(columnCount);

        for (Row row : rows) {
            dataVector.add(new Vector<>(row.getFields().values()));
        }
        setDataVector(dataVector, columnVector);
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
        updateModel();
    }
}
