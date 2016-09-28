import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class DataTable {
    private String name ;

    private boolean selfRef = false;

    private DataColumn pkColumn;

    private String platformIdCol = "platform_id";

    private String condition;

    private List<DataColumn> columns = new ArrayList<DataColumn>();
    private Map<String, DataColumn> columnIndexes = new HashMap<String, DataColumn>();

    private List<DataRow> rows = new ArrayList<DataRow>();

    public void addColumn(DataColumn column) {
        this.columns.add(column);
        this.columnIndexes.put(column.getName(), column);
    }

    public DataColumn getColumn(String name) {
        return this.columnIndexes.get(name);
    }

    public void addRow(DataRow row) {
        this.rows.add(row);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataColumn> columns) {
        this.columns = columns;
    }

    public boolean isSelfRef() {
        return selfRef;
    }

    public void setSelfRef(boolean selfRef) {
        this.selfRef = selfRef;
    }

    public DataColumn getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(DataColumn pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getPlatformIdCol() {
        return platformIdCol;
    }

    public void setPlatformIdCol(String platformIdCol) {
        this.platformIdCol = platformIdCol;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        condition = condition.replaceAll("!=", "<>");
        this.condition = condition;
    }

    public String toXml() {
        String xml = "<table name=\"" + this.getName() + "\">";
        List<DataColumn> cols = this.getColumns();
        for(DataColumn column : cols) {
            xml += column.toXml();
        }

        List<DataRow> rows = this.getRows();
        if(rows != null && rows.size() > 0) {
            xml += "<rows>";
            for(DataRow row : rows) {
                xml += row.toXml();
            }
            xml += "</rows>";
        }

        xml += "</table>";
        return xml;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    public void setRows(List<DataRow> rows) {
        this.rows = rows;
    }
}
