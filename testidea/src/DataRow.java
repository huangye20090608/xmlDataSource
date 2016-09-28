import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class DataRow {
    private String rowIndex;

    private List<DataCell> cells = new ArrayList<DataCell>();

    public void addCell(DataCell cell) {
        this.cells.add(cell);
    }

    public List<DataCell> getCells() {
        return cells;
    }

    public void setCells(List<DataCell> cells) {
        this.cells = cells;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String toXml() {
        String xml = "<row>";
        if(this.cells != null && this.cells.size() > 0) {
            for(DataCell cell : this.cells) {
                xml += cell.toXml();
            }
        }
        xml += "</row>";

        return xml;
    }

}
